package com.me;

import com.badlogic.gdx.math.Rectangle;

public class Target extends Rectangle{
	
	private boolean isDestroyed = false ;

	public Target (float x , float y , float width ,float height)
	{
		this.x = x ;
		this.y = y ;
		this.width = width ;
		this.height = height ;
	}

	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void setDestroyed(boolean isDestroyed) {
		this.isDestroyed = isDestroyed;
	}
	
	
	
	

	
}
