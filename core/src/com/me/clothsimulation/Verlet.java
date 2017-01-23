package com.me.clothsimulation;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.me.MyGdxGame;










public class Verlet implements Screen, InputProcessor {

	MyGdxGame game ;
	static Physics physics;
	static float gravity = -100;  
	Random random = new Random();
	// Where we'll store all of the points
	static ArrayList<PointMass> pointmasses;
	
	// every PointMass within this many pixels will be influenced by the cursor
	static float mouseInfluenceSize = 20; 
	// minimum distance for tearing when user is right clicking
	static float mouseTearSize = 8;
	static float mouseInfluenceScalar = 5;
	
	final int curtainHeight = 10;
	final int curtainWidth = 15;
	final int yStart = 450; // where will the curtain start on the y axis?
	final float restingDistances = 20;
	final float stiffnesses = 1;
	final float curtainTearSensitivity = 100; // distance the PointMasss have to go before ripping
	
	final static float width = Gdx.graphics.getWidth();
	final static float height = Gdx.graphics.getHeight();
	
	
	
	
	public Verlet(MyGdxGame game) {
		game = this.game ;
	}

	public void updateGraphics() {
		
	}
	@Override
	public void render(float delta) {
		
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		 physics.update();
		 for (PointMass p : pointmasses) {
			    p.render(delta);
			  }
		  for (Circle c : physics.circles) {
			    c.draw(); 
			  }
	
	}
	

public static void addPointMass(PointMass p) {
  pointmasses.add(p); 
}
public static void removePointMass(PointMass p) {
  pointmasses.remove(p);  
}

	@Override
	public void show() {
		 pointmasses = new ArrayList<PointMass>();
		 physics = new Physics();
		  createCurtain();
		//  createBodies();
		  InputMultiplexer multi = new InputMultiplexer();
		  for(int i = 0 ;i < pointmasses.size() ;i++)
			  multi.addProcessor(pointmasses.get(i));
		  multi.addProcessor(this);
		  Gdx.input.setInputProcessor(multi);
		  
	}
	
	public void toggleGravity() {
		  if (gravity != 0)
		    gravity = 0;
		  else
		    gravity = -100;
		}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	

	}

	@Override
	public void resize(int width, int height) {

	}


	@Override
	public void dispose() {
		

	}
	
	// Using http://www.codeguru.com/forum/showpost.php?p=1913101&postcount=16
	// We use this to have consistent interaction
	// so if the cursor is moving fast, it won't interact only in spots where the applet registers it at
	public static float distPointToSegmentSquared(float lineX1, float lineY1, float lineX2, float lineY2, float pointX, float pointY) {
	  float vx = lineX1 - pointX;
	  float vy = lineY1 - pointY;
	  float ux = lineX2 - lineX1;
	  float uy = lineY2 - lineY1;
	  
	  float len = ux*ux + uy*uy;
	  float det = (-vx * ux) + (-vy * uy);
	  if ((det < 0) || (det > len)) {
	    ux = lineX2 - pointX;
	    uy = lineY2 - pointY;
	    return Math.min(vx*vx+vy*vy, ux*ux+uy*uy);
	  }
	  
	  det = ux*vy - uy*vx;
	  return (det*det) / len;
	}
	
	public void createCurtain() {
		  // midWidth: amount to translate the curtain along x-axis for it to be centered
		  // (curtainWidth * restingDistances) = curtain's pixel width
		  int midWidth = (int) (  (curtainWidth * restingDistances)/2);
		  System.out.println(midWidth);
		  // Since this our fabric is basically a grid of points, we have two loops
		  for (int y = 0; y <= curtainHeight; y++) { // due to the way PointMasss are attached, we need the y loop on the outside
		    for (int x = 0; x <= curtainWidth; x++) { 
		      PointMass pointmass = new PointMass(midWidth + x * restingDistances, y * restingDistances + yStart);
		      
		      // attach to 
		      // x - 1  and
		      // y - 1  
		      //  *<---*<---*<-..
		      //  ^    ^    ^
		      //  |    |    |
		      //  *<---*<---*<-..
		      //
		      // PointMass attachTo parameters: PointMass PointMass, float restingDistance, float stiffness
		      // try disabling the next 2 lines (the if statement and attachTo part) to create a hairy effect
		      if (x != 0) 
		        pointmass.attachTo((PointMass)(pointmasses.get(pointmasses.size()-1)), restingDistances, stiffnesses);
		      // the index for the PointMasss are one dimensions, 
		      // so we convert x,y coordinates to 1 dimension using the formula y*width+x  
		      if (y != 0)
		        pointmass.attachTo((PointMass)(pointmasses.get((y - 1) * (curtainWidth+1) + x)), restingDistances, stiffnesses);
		      
		      // we pin the very top PointMasss to where they are
		      if (y == 0)
		        pointmass.pinTo(pointmass.x, pointmass.y);
		        
		      // add to PointMass array  
		      pointmasses.add(pointmass);
		    }
		  }
		}

	private void createBodies() {
		  for (int i = 0; i < 25; i++) {
			    new Body(random.nextInt((int)width),random.nextInt((int)height) , 40);
			  }
		
	}
	
	

	@Override
	public boolean keyDown(int keycode) {
		  if (keycode == Keys.R ) {
			    pointmasses = new ArrayList<PointMass>();
			    physics.circles = new ArrayList<Circle>();
			//    createCurtain();
			    createBodies();
			  } 
			  if (keycode == Keys.G)
			    toggleGravity();
		return false;
	}

	

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
