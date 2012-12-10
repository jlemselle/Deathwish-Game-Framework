//Copyright (c) <2012> <James Emselle (James.e.0111.2@gmail.com)>
//
//This software is provided 'as-is', without any express or implied
//warranty. In no event will the authors be held liable for any damages
//arising from the use of this software.
//
//Permission is granted to anyone to use this software for any purpose,
//including commercial applications, and to alter it and redistribute it
//freely, subject to the following restrictions:
//
//   1. The origin of this software must not be misrepresented; you must not
//   claim that you wrote the original software. If you use this software
//   in a product, an acknowledgment in the product documentation would be
//   appreciated but is not required.
//
//   2. Altered source versions must be plainly marked as such, and must not be
//   misrepresented as being the original software.
//
//   3. This notice may not be removed or altered from any source
//   distribution.

package com.deathwish.framework;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements Runnable {
	
	// Internal view thread
	Thread vThread = null;
	// Internal view SurfaceHolder
	SurfaceHolder vHolder;
	// Running variable
	boolean isRunning = false;
	// Activity
	DwActivity activity;

	public GameView(DwActivity parent) {
		// View constructor
		super(parent);
		init(parent);
		
	}
	
	public GameView(DwActivity parent, AttributeSet attrs) {
		// View constructor
		super(parent, attrs);
		init(parent);
	}
	
	public GameView(DwActivity parent, AttributeSet attrs, int defStyle) {
		// View constructor
		super(parent, attrs, defStyle);
		init(parent);
	}
	
	private void init(DwActivity parent) {
		if (!(isInEditMode())) {
			// Get surface's holder.
			vHolder = getHolder();
			// Activity
			activity = parent;
		}
	}

	@Override
	public void run() {			
		// Running loop
		while (isRunning) {
			activity.fpsTracker.startTimer();
			// If the surface isn't ready, skip drawing (but keep looping)
			if (!vHolder.getSurface().isValid()) {
				continue;
			}
			Loop();
			for (Service service : activity.serviceList) {
				service.onLoop();
			}
			activity.onGameLoop();
			// Unlock, draw, then re-lock the canvas.
			Canvas c = vHolder.lockCanvas();
			// Clear the screen.
			c.drawColor(Color.BLACK);
			// Draw each service
			for (Service service : activity.serviceList) {
				service.onDraw(c);
			}
			// Draw the game
			activity.onGameDraw(c);
			// Draw engine related stuffs
			Draw(c);
			vHolder.unlockCanvasAndPost(c);
			activity.fpsTracker.stopTimer();
		}
	}
	
	////////////
	// CREATE //
	////////////
	public void Create() {
		isRunning = true;
		// Create and start a new thread.
		vThread = new Thread(this);
		vThread.start();
	}
	
	//////////
	// LOOP //
	//////////
	public void Loop() {
		if (activity.cacheUpdateTimer.Flag()) {
			activity.cachedFPS = activity.fpsTracker.getFormattedAverageFPS();
		}
	}
	
	//////////
	// DRAW //
	//////////
	public void Draw(Canvas c) {
		// Add text
	    Paint paint = new Paint(); 
	    paint.setAntiAlias(true);
	    paint.setFakeBoldText(true); // if you like bold
	    paint.setColor(Color.WHITE); 
	    paint.setTextSize(30);
	    c.drawText("FPS: " + activity.cachedFPS, 10, 35, paint);
	}
	
	/////////////
	// DESTROY //
	/////////////
	public void Destroy() {
		isRunning = false;
		// Continually attempt to end the thread (the join method doesn't always work)
		while(true) {
			try {
				vThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		// After it ends, delete it.
		vThread = null;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void lightsOut() {
		if (android.os.Build.VERSION.SDK_INT >= 11) {
			// Honeycomb or newer! :D
			setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		} else {
			// Not cool enough for soft buttons :(
		}
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		activity.onSizeChanged(w, h, oldw, oldh);
	}
}