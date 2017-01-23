package com.me;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Filters implements InputProcessor, Screen {

	MyGdxGame game ;
	
	public Filters(MyGdxGame game)
	{
		this.game = game; 
	}

	
	Texture tex = new Texture(Gdx.files.internal("data/sunflower.jpg"));
	Texture tex2 ;
	SpriteBatch batch = new SpriteBatch();
	
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(tex, 0, 0);
		batch.draw(tex2, 300, 150);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	// The blurr filter idea is to average the color(in RGB88 format) in surrounding cells and add put it in cell
	@Override
	public void show() {
		tex.getTextureData().prepare();
		Pixmap pixmap = tex.getTextureData().consumePixmap();
		
		int width = pixmap.getWidth();
		int height = pixmap.getHeight();
		Pixmap newPixmap = new Pixmap(width,height,Format.RGBA8888);
		for(int i = 1 ;  i< width-1 ; i++)
			for(int j = 1 ; j < height -1 ; j++){
				int sum =pixmap.getPixel(i, j) + pixmap.getPixel(i-1, j) + pixmap.getPixel(i-1, j-1)+pixmap.getPixel(i, j+1) +pixmap.getPixel(i, j-1)+pixmap.getPixel(i, j+1)+pixmap.getPixel(i+1, j)+pixmap.getPixel(i, j-1)+pixmap.getPixel(i, j+1);
				int average =  (int) (sum / 9.0) ;	
				Color color = new Color();
				Color.rgba8888ToColor(color, average);
				newPixmap.setColor(color);
				newPixmap.fillRectangle(i, j, 1, 1);
			}
		tex2 = new Texture(newPixmap);
		
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
	public void dispose() {
		

	}

	@Override
	public boolean keyDown(int keycode) {
		
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
