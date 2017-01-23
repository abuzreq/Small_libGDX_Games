package com.me;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class LetItRain implements InputProcessor, Screen {

	/**			NOTES
	 * 
	 * 	To achieve a good angles rain drops , velocity has to be introduced in a manner similar to Desert Journey
	 * 	things to add are controlling the drops width , having particles effect at hitting the ground
	 *  try to abstract the whole things to be a modules , I imagine how this same things might
	 *   work as meteor shower in a starry night or with more intensity can give
	 *  the illusion of moving at high speed (in this case it looks much like a gradient )
	 * 
	 * Also I think that I have a previous project somewhere that also handles rain but can't remember
	 */
	MyGdxGame game ;
	final ArrayList<Vector2> raindrops = new ArrayList<Vector2>();
	ShapeRenderer renderer = new ShapeRenderer();
	float rainSpeed = 10 ;
	int rainDir = -1 ;
	float rainAngle = 30 ;

	public LetItRain(MyGdxGame game )
	{
		this.game  = game ;
	}
	@Override
	public void render(float delta) {
		//Gdx.gl20.glClearColor(1, 1, 1, 1);
		//Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		
		
			
		renderer.begin(ShapeType.Filled);

	
		renderer.end();
		
		renderer.begin(ShapeType.Filled);
		renderer.setColor(0,0, 0, 0.1f);
		renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		renderer.setColor(Color.GREEN);
		for(int i = 0 ; i < raindrops.size() ; i++)	
		{
		//	float dx = rainDir * (float)(Math.tan(Math.toRadians(30))) ;
			raindrops.get(i).add(0, -rainSpeed);
			if(raindrops.get(i)!= null)
				drawRainDrop(renderer,raindrops.get(i),0 , 15 ,0.5f);
			
			if(raindrops.get(i).y <= 0)
				{
					raindrops.remove(i);
					i--;
				}
			
		}
		renderer.end();
	

	}

	private void drawRainDrop(ShapeRenderer renderer ,Vector2 dropPosition ,float dropAngle ,float dropHeight , float dropWidth )
	{
		
		Vector2 dropHead = new Vector2(dropPosition.x ,dropPosition.y - dropHeight);	
		//renderer.line(dropPosition, dropHead);
		renderer.rectLine(dropPosition,dropHead,4);
		
	}
	@Override
	public void resize(int width, int height) {
		

	}

	@Override
	public void show() {
		
		
		renderer.setColor(Color.BLUE);
		//renderer.rotate(0f, 0f, 1, -rainAngle);
		final Random rand = new Random();
		Timer.schedule(new Task(){

			@Override
			public void run() {
				float rx = rand.nextInt(Gdx.graphics.getWidth()-200)+100;				
				raindrops.add(new Vector2(rx,Gdx.graphics.getHeight()));
				
			}}, 0.2f,0.05f,-2);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
