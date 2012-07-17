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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

//HybridActivity implements a thread, surface view and activity in one hybrid class.
public class DwActivity extends Activity {
	
	private GameView view;
	
	boolean ToastDebugging = false;
	// Service List
	List<DwService> serviceList = new ArrayList<DwService>();;
	
	//Bitmap icon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initialise the view, when the Activity is created.
		view = new GameView(this);
		
		//icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
		// Set the view to display on the activity.
		setContentView(view);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// Destroy the view.
		view.Destroy();
		// Give each service a chance to end gracefully
		for (DwService service : serviceList) {
			service.onDestroy();
		}
		// Allow the game to end gracefully
		onGameDestroy();
		// Delete all services
		serviceList.clear();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Create the view.
		view.Create();
		// create each service
		for (DwService service : serviceList) {
			service.onCreate();
		}
		// Create the game
		onGameCreate();
	}
	
	public void onGameCreate() {}
	public void onGameLoop() {}
	public void onGameDraw(Canvas c) {}
	public void onGameDestroy() {}
	
	
	public class GameView extends SurfaceView implements Runnable {
		
		// Internal view thread
		Thread vThread = null;
		// Internal view SurfaceHolder
		SurfaceHolder vHolder;
		// Running variable
		boolean isRunning = false;

		public GameView(Context context) {
			// View constructor
			super(context);
			// Get surface's holder.
			vHolder = getHolder();
		}

		@Override
		public void run() {
			// Running loop
			while (isRunning) {
				// If the surface isn't ready, skip drawing (but keep looping)
				if (!vHolder.getSurface().isValid()) {
					continue;
				}
				Loop();
				for (DwService service : serviceList) {
					service.onLoop();
				}
				onGameLoop();
				// Unlock, draw, then relock the canvas.
				Canvas c = vHolder.lockCanvas();
				// Clear the screen.
				c.drawColor(Color.BLACK);
				// Draw engine related stuffs
				Draw(c);
				// Draw each service
				for (DwService service : serviceList) {
					service.onDraw(c);
				}
				// Draw the game
				onGameDraw(c);
				vHolder.unlockCanvasAndPost(c);
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
		}
		
		//////////
		// DRAW //
		//////////
		public void Draw(Canvas c) {
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
	}
	
	public void DebugMsg(CharSequence text) {
		if (ToastDebugging) {
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	// Register the service, so we know what to run at the right times.
	public void Register(DwService service) {
		// Add the service to the service list
		serviceList.add(service);
		// Let the service add us back
		service.parent(this, view);
	}
	
	// Return the view (mainly for the child activity)
	public View getView() {
		return view;
	}
}