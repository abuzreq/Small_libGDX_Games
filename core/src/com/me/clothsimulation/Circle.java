package com.me.clothsimulation;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;



public class Circle {
	  float radius;
	 ShapeRenderer renderer = new ShapeRenderer();
	  PointMass attachedPointMass;
	  
	  Circle (float r) {
	    radius = r;
	  }
	  
	  // Constraints
	  public void solveConstraints () {
	    float x = attachedPointMass.x;
	    float y = attachedPointMass.y;
	    
	    // only do a boundary constraint
	    if (y < radius)
	      y = 2*(radius) - y;
	    if (y > Verlet.height-radius)
	      y = 2 * (Verlet.height - radius) - y;
	    if (x > Verlet.width-radius)
	      x = 2 * (Verlet.width - radius) - x;
	    if (x < radius)
	      x = 2*radius - x;
	      
	    attachedPointMass.x = x;
	    attachedPointMass.y = y;
	  }
	  
	  public void draw () {
		  renderer.begin(ShapeType.Line);
	    renderer.ellipse(attachedPointMass.x, attachedPointMass.y, radius*2, radius*2);
		  renderer.end();
	  }
	  
	  public void attachToPointMass (PointMass p) {
	    attachedPointMass = p;
	  }
}
