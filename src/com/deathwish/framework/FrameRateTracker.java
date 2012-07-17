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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FrameRateTracker {
	
	private final int MAX_HISTORY = 100;
	
	private float frameTime;
	private float FPS;
	private boolean limitFPS = false;
	private float targetFrameTime;
	private DecimalFormat formatter = new DecimalFormat("#.0");
	private List<Float> History = new ArrayList<Float>();
	
	// Variables for calculating time
	long startTime;
	float sleepTime;
	
	public void setLimitFPS(boolean limitFPS) {
		this.limitFPS = limitFPS;
	}

	public float getFrameTime() {
		return frameTime;
	}
	
	public float getAverageFPS() {
		float total = 0;
		for (Float fps : History) {
			total += fps;
		}
		return (total / History.size());
	}

	public float getFPS() {
		return FPS;
	}
	
	public String getFormattedAverageFPS() {
		return formatter.format(getAverageFPS());
	}
	
	public String getFormattedFPS() {
		return formatter.format(FPS);
	}

	public void setTargetFrameTime(float targetFrameTime) {
		limitFPS = true;
		this.targetFrameTime = 1000 / targetFrameTime;
	}

	public FrameRateTracker() {
		super();
	}
	
	public void startTimer() {
		// Time the frame starts
		startTime = System.currentTimeMillis();
	}
	public void stopTimer() {
		if (limitFPS) {
			sleepTime = (float) (targetFrameTime - (System.currentTimeMillis() - startTime));
			try {
				if (sleepTime > 0)
					Thread.sleep((long) sleepTime);
				else
					Thread.sleep(10);
			} catch (Exception e) {}
		}
		frameTime = (float)((System.currentTimeMillis() - startTime));
		FPS = 1000 / frameTime;
		History.add(FPS);
		if (History.size() > MAX_HISTORY) {
			History.remove(0);
		}
	}
}
