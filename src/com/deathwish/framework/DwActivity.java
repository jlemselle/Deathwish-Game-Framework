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
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//HybridActivity implements a thread, surface view and activity in one hybrid class.
public class DwActivity extends Activity {
	
	private GameView view;
	FrameRateTracker fpsTracker; // Class to keep track of frame rate
	String cachedFPS; // Cached FPS, so we don't have to calculate new FPS every frame
	Timer cacheUpdateTimer; // Timer to tell us when we need to update our cached FPS
	private boolean isLandscape = true;
	private boolean showTitle = false;
	private boolean isFullscreen = true;
	private boolean lowProfile = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Initialise the view, when the Activity is created.
		view = new GameView(this);
		// Initialise our frame rate tracker
		fpsTracker = new FrameRateTracker();
		// Initialise FPS cache
		cachedFPS = "";
		// Initialise our cache timer to 20 frames
		cacheUpdateTimer = new Timer(20);
		
		onWindowConfig();
		// request landscape orientation
		if (isLandscape) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}
		// requesting to turn the title OFF
		if (showTitle == false) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		// making it full screen
		if (isFullscreen) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		
		// Set the view to display on the activity.
		setContentView(view);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Destroy the view.
		view.Destroy();
		// Give each service a chance to end gracefully
		for (Service service : serviceList) {
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
		for (Service service : serviceList) {
			service.onCreate();
		}
		
		// Create the game
		onGameCreate();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus) {
			if (lowProfile) {
				view.lightsOut();
			}
		}
		super.onWindowFocusChanged(hasFocus);
	}

	// Service List
	List<Service> serviceList = new ArrayList<Service>();;
	
	////////////////////////////////////////////////
	// GAME OVERRIDEN METHODS
	////////////////////////////////////////////////
	
	public void onWindowConfig() {}
	public void onSizeChanged(int w, int h, int oldw, int oldh) {}
	public void onGameCreate() {}
	public void onGameLoop() {}
	public void onGameDraw(Canvas c) {}
	public void onGameDestroy() {}
	
	////////////////////////////////////////////////
	// GAME CALLED METHODS
	////////////////////////////////////////////////
	
	// Register the service, so we know what to run at the right times.
	public void Register(Service service) {
		// Add the service to the service list
		serviceList.add(service);
		// Let the service add us back
		service.parent(this, view);
	}
	
	// Return the view (mainly for the child activity)
	public View getView() {
		return view;
	}
	
	// Method to deliver frame time to the game (for interpolation and animation)
	public float getFrameTime() {
		return (fpsTracker.getFrameTime()/1000);
	}
	
	// A method to allow the game to specify a specific frame rate (for performance, and power conservation)
	public void setTargetFrameRate(int targetFPS) {
		fpsTracker.setTargetFrameTime(targetFPS);
	}
	
	public void setLandscape(boolean isLandscape) {
		this.isLandscape = isLandscape;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public void setFullscreen(boolean isFullscreen) {
		this.isFullscreen = isFullscreen;
	}
	
	public void setLowProfile(boolean lowProfile) {
		this.lowProfile = lowProfile;
	}
	
	////////////////////////////////////////////////
	// DEBUGGING
	////////////////////////////////////////////////
	
	// Variable to enable/disable debug toast messages
	boolean ToastDebugging = true;
	
	public void DebugMsg(CharSequence text) {
		if (ToastDebugging) {
			Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}