package com.me;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Seek implements Screen , InputProcessor{
//created for RoboCup purposes,as a test fr the behaviour of seeking (see Programming Game AI By Example)
	MyGdxGame game ;
	Vector2 target = new Vector2(0,0);
	Vector2 playerPos = new Vector2(250,250);
	Vector2 playerHeading = new Vector2(250,250);
	ShapeRenderer renderer = new ShapeRenderer();
	Rectangle player = new Rectangle();
	
	boolean seek = false ;
	public Seek(MyGdxGame game) {
		this.game = game ;
	}

	final double R2D = 180.0/3.145 ;
	Vector2 norm = new Vector2(-playerHeading.y ,  playerHeading.x);
	//Vector2 vector = new Vector2(Math.tan(norm.cpy().scl(0.5f).add(playerHeading.cpy().scl(0.5f))));
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(seek){
			playerPos.lerp(target, 0.02f);	
		}
		renderer.begin();
		float angle = target.angle(target);
		renderer.rotate(playerPos.x, playerPos.y, 0,angle );	
		renderer.rect(playerPos.x, playerPos.y, 25, 25);
		Vector2 tmp = new Vector2(playerPos.x + playerHeading.x , playerPos.y + playerHeading.y);
	//	renderer.line(new Vector2(playerPos.x + 12.5f ,playerPos.y + 12.5f),tmp );
		
		renderer.setColor(Color.RED);
		renderer.line(new Vector2(100,100) ,playerHeading);
		renderer.setColor(Color.GREEN);
	
		renderer.line(new Vector2(100,100) ,norm);
		renderer.line(new Vector2(100,100) ,norm.scl(-1));
		renderer.setColor(Color.BLUE);
		
	//	renderer.line(new Vector2(100,100) ,vector);
	//	renderer.line(new Vector2(100,100) ,vector.scl(-1));
		renderer.end();
		
		/*
		Vector2 vec =  new Vector2 (playerHeading.x* (float)Math.cos(60*R2D),playerHeading.y );
		renderer.line(new Vector2(100,100) ,vec);
		renderer.setColor(Color.MAROON);
		renderer.line(new Vector2(100,100) ,new Vector2(-vec.y ,  vec.x));
		renderer.setColor(Color.BLUE);
		Vector2 vec2 =  new Vector2 (playerHeading.x,playerHeading.y * (float)Math.sin(60*R2D));
		renderer.line(new Vector2(100,100) ,vec2);
		renderer.setColor(Color.CYAN);
		renderer.line(new Vector2(100,100) ,new Vector2(-vec2.y ,  vec2.x));
		renderer.end();*/
	}

    private List<Vector2> feelers =  new LinkedList<Vector2>();
    private void CreateFeelers(Vector2 pos , Vector2 heading) {
    	double HalfPi = Math.PI / 2 ;
    	double m_dWallDetectionFeelerLength = 1 ;
    	feelers.clear();
        //feeler pointing straight in front
    	feelers.add(pos.add(heading.scl((float) (m_dWallDetectionFeelerLength ))));
        
        //feeler to left
        Vector2 temp = new Vector2(heading);
        Transformation.Vec2DRotateAroundOrigin(temp, HalfPi * 3.5f);
        feelers.add(pos.add(temp.scl((float) (m_dWallDetectionFeelerLength / 2.0f))));

        //feeler to right
        temp = new Vector2(heading);
        Transformation.Vec2DRotateAroundOrigin(temp, HalfPi * 0.5f);
        feelers.add(pos.add(temp.scl((float) (m_dWallDetectionFeelerLength / 2.0f))));
    
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
		renderer.setAutoShapeType(true);
		CreateFeelers(new Vector2(100,100),new Vector2(100,100));
		System.out.println(feelers);
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
		if(keycode == Keys.S)
			seek = !seek ;
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
		int y = Gdx.graphics.getHeight() - screenY ;
		target.set(screenX,y);
		System.out.println(target);
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
