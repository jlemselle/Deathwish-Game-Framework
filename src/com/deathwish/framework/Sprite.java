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

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Sprite {
	protected Bitmap image;
	protected float x, y = 0;
	protected Paint paint = null;
	
	public Sprite(Resources resources, int drawable) {
		image = BitmapFactory.decodeResource(resources, drawable);
	}
	
	public Sprite(Bitmap image) {
		this.image = image;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void setPosition(int x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setPosition(float x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void move(float x, float y) {
		setPosition(this.x + x, this.y + y);
	}
	public void move(int x, int y) {
		setPosition(this.x + x, this.y + y);
	}
	public void move(int x, float y) {
		setPosition(this.x + x, this.y + y);
	}
	public void move(float x, int y) {
		setPosition(this.x + x, this.y + y);
	}
	
	
	public int getX() {
		return (int) x;
	}
	public int getY() {
		return (int) y;
	}
	
	public int getHeight() {
		return (int) image.getHeight();
	}
	public int getWidth() {
		return (int) image.getWidth();
	}
	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	
	public void draw(Canvas c) {
		c.drawBitmap(image, x, y, paint);
	}

}
