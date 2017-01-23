package com.me;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

public class Square extends Sprite implements InputProcessor {
	private Texture anim;
	private Texture normal;
	private int num;
	private Sound sound ;
	long id ;
	int priority ;
	OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight());

	/**
	 * @animTex is the animation texture
	 * @num is the index that will be used to keep track of the square that is
	 *      clicked on or shown
	 * 
	 * **/
	public Square(Texture tex, Texture animTex, float x, float y, int num,String soundPath) {
		super(tex);
		this.normal = tex;
		this.anim = animTex;
		setPosition(x, y);
		setOrigin(x, y);

		this.num = num;
		
		 sound = Gdx.audio.newSound(Gdx.files.internal(soundPath));
	}

	@Override
	public void draw(Batch batch) {

		super.draw(batch);
	}

	// eventually I didn't used the animation textures , but kept it for history :P
	public void supply(int current) {
		if (current == num) {
			this.setColor(new Color(this.getColor().r,this.getColor().g,this.getColor().b,0));
			//setTexture(anim);
			sound.setPriority(id, priority);
		//	 id = sound.play(1);
		
	//		sound.play(1);
		}

		else
			this.setColor(new Color(this.getColor().r,this.getColor().g,this.getColor().b,1));
	//		setTexture(normal);

	}
	
	public void playSound(int priority)
	{
		sound.setPriority(id, priority);
		sound.play();
	}
	public void setSoundPriority(int priority)
	{
		sound.setPriority(id, priority);
		this.priority = priority;
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
	

		if (Simon.takeInput) {

			Vector3 vec = new Vector3(screenX, screenY, 0);
			camera.unproject(vec);
			if (getBoundingRectangle().contains(vec.x, vec.y)) {
				Simon.input = num;
				sound.play(0.5f);
			}

		}
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
