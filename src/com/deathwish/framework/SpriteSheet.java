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

public class SpriteSheet extends Sprite {
	
	int SPRITE_ROWS, SPRITE_COLUMNS;
	int[] ANIMATION_MAP;
	int SPRITE_WIDTH;
	int SPRITE_HEIGHT;

	public SpriteSheet(Bitmap image, int rows, int columns) {
		super(image);
		Initialise(rows, columns);
	}

	public SpriteSheet(Resources resources, int drawable, int rows, int columns) {
		super(resources, drawable);
		Initialise(rows, columns);
	}
	
	private void Initialise(int rows, int columns) {
		SPRITE_ROWS = rows;
		SPRITE_COLUMNS = columns;
		SPRITE_HEIGHT = (image.getHeight() / SPRITE_ROWS);
		SPRITE_WIDTH = (image.getWidth() / SPRITE_COLUMNS);
	}

}
