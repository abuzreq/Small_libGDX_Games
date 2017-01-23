package com.me;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NoisyDrawing implements InputProcessor, Screen {

	MyGdxGame game ;
	public NoisyDrawing(MyGdxGame game )
	{
		this.game = game;
	}
	ShapeRenderer renderer = new  ShapeRenderer();
	OrthographicCamera camera = new OrthographicCamera();
	Random rand = new  Random();
	Polygon polygon = new Polygon();
	boolean draw = true;
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		renderer.begin(ShapeType.Line);
	//	renderer.setProjectionMatrix(camera.combined);
		renderer.setColor(Color.WHITE);
		if(!list.isEmpty())
		{
			
			for(int i =0 ; i< list.size()-1;i++)
			{
			float rotation = (float) Math.toDegrees(rand.nextFloat()*6.28f);
			// 	System.out.println( rotation);
				Vector3 vec =  new Vector3(list.get(i).x,list.get(i).y,0);
			
				//camera.unproject(vec);
				renderer.rotate(list.get(i).x,list.get(i).y,0, rotation);
				renderer.line(list.get(i).x,list.get(i).y,list.get(i+1).x,list.get(i+1).y);
				
			}
			
		}
		
		/*
		for(int i =2 ; i< polygon.getVertices().length;i+=2)
		{
			renderer.rotate(verts[i-2], verts[i-1], 0, (float) Math.toDegrees(rand.nextFloat()*6.28f));
			renderer.line(verts[i-2], verts[i-1], verts[i], verts[i+1]);
		}*/
		renderer.end();
		
	}
	float[] verts;

	@Override
	public void show() {
		polygon.setVertices(new float[]{0,0,50,0,100,25,125,50,100,75,50,50,25,25,0,0});
		 verts = polygon.getVertices();
		
		//renderer.translate(100, 100, 0);
		
		Gdx.input.setInputProcessor(this);
	}
	@Override
	public void resize(int width, int height) {

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
		if(keycode == Keys.SPACE)
			draw = true;
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

	ArrayList<Vector2> list = new ArrayList< Vector2>();
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		list.add(new Vector2(screenX,480-screenY));
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
