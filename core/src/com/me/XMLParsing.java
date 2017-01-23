package com.me;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class XMLParsing implements Screen {

	
	SpriteBatch batch;
	Sprite sprite ;
	MyGdxGame game ;
	public XMLParsing(MyGdxGame game)
	{
		this.game = game ;
	}
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		sprite.draw(batch);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		XmlReader reader = new XmlReader();
		try {
			Element root = reader.parse(Gdx.files.internal("data/Sprite.xml"));
			sprite = new Sprite(new Texture(Gdx.files.internal(root.get("path"))));
			sprite.setPosition(root.getFloat("x"), root.getFloat("y"));
			sprite.setSize(root.getFloat("width"), root.getFloat("height"));
			sprite.setOrigin(root.getChildByName("origin").getFloat("x"), root.getChildByName("origin").getFloat("x"));
			sprite.setRotation(root.getFloat("rotation"));
			// the encoding I set is windows-1256 which doesn't support arabic , res: ???? 
			System.out.println(root.getChildByName("arabic").get("hello"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	batch.dispose();
	sprite.getTexture().dispose();

	}

}
