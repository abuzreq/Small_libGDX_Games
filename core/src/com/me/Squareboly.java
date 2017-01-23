package com.me;
// week 4 game , it failed , but migh prove being a good game to complete in future
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Squareboly implements Screen, InputProcessor {
	MyGdxGame game;
	ShapeRenderer renderer;
	OrthographicCamera camera;
	Shape player;
	ArrayList<Shape> list;
	SpriteBatch batch = new SpriteBatch();
	Stage stage;
	Sprite spr = new Sprite(new Texture(Gdx.files.internal("data/red1.png")));
	
	public Squareboly(MyGdxGame game) {
		this.game = game;
	}

	boolean justOverlapped = false;
	float[] worldDim = {0,0,0,Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),Gdx.graphics.getWidth(),0,0,0};
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

	/* manipulate those to change camera view
		camera.setToOrtho(false);
		camera.rotate(180);
		
		*/
		renderer.begin(ShapeType.Line);
		renderer.setProjectionMatrix(camera.combined);
		Gdx.gl20.glLineWidth(5);
		renderer.polygon(worldDim);
		renderer.end();
		stage.act();
	//	camera.position.set(player.getPosition(), 0);
		camera.update();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		spr.draw(batch);
		batch.end();
		for (Shape shape : list) {
/*
			if (player.rec.overlaps(shape.rec)) {
				justOverlapped = true;
				if (player.compareTo(shape) > 0)
					player.rec.merge(shape.rec);
				// player.rec.setSize(player.rec.width+shape.rec.width/2,
				// player.rec.height+shape.rec.height/2);
				else if (player.compareTo(shape) < 0)
					player.merge2(shape.rec);
				// player.rec.setSize(player.rec.width-shape.rec.width/2,
				// player.rec.height-shape.rec.height/2);
			}*/

		}
		stage.draw();
		System.out.println(player.getPosition());
	}

	// renderer.begin(ShapeType.Filled);
	// renderer.end();

	@Override
	public void show() {
	
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		stage = new Stage();
		player = new Shape(0, 0, 25, 25, 0, 0, "White");
		stage.addActor(player);
		list = new ArrayList<Shape>();
		list.add(player);
		Random random = new Random();
		/*
		for(int i = 0 ; i < 20;i++)
		{
			Shape tmp = new Shape(random.nextInt()+Gdx.graphics.getWidth(), random.nextInt()+Gdx.graphics.getHeight(),
					random.nextInt(130)+10, random.nextInt(130)+10,
					random.nextInt(8)-4, random.nextInt(8)-4,
					colorNames[random.nextInt(colorNames.length-1)]);
			list.add(tmp);
			stage.addActor(tmp);
		}*/
	
		XmlReader reader = new XmlReader();
		try {
			Element root = reader.parse(Gdx.files.internal("data/Shapes.xml"));
			for (int i = 0;; i++) {
				Element shape = root.getChild(i);
				if (shape.getName().equalsIgnoreCase("end"))
					break;
				Shape tmp = new Shape(shape.getFloat("x"), shape.getFloat("y"),
						shape.getFloat("width"), shape.getFloat("height"),
						shape.getFloat("velX"), shape.getFloat("velY"),
						shape.get("color"));
				list.add(tmp);
				stage.addActor(tmp);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Gdx.input.setInputProcessor(this);

	}

	/*
	 * for(int i = 0 ;i < tree.count(tree.root.left);i++) {
	 * 
	 * if(i % 2 == 0){ renderer.circle(vec.x+50, vec.y, 20); } else { vec = new
	 * Vector2(vec.x + 25 , vec.y +50); renderer.circle(vec.x, vec.y, 20); } }
	 */

	public static int getArea(Rectangle rec) {
		return (int) (rec.width * rec.height);

	}

	public static int getArea(Circle cir) {
		return (int) (cir.radius * cir.radius * Math.PI);

	}

	@Override
	public void resize(int width, int height) {

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

	static Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE,
			Color.MAGENTA, Color.WHITE };
	static String[] colorNames = { "Red", "Blue", "Green", "Orange", "Magenta",
			"White" };

	class Shape extends Actor implements Comparable<Shape> {
		float area;
		Vector2 vel;
		Rectangle rec;
		Color color;
		private ShapeRenderer renderer = new ShapeRenderer();

		/**
		 * 
		 * @param x
		 * @param y
		 * @param width
		 * @param height
		 * @param velX
		 * @param velY
		 * @param colorName
		 */
		public Shape(float x, float y, float width, float height, float velX,
				float velY, String colorName) {
			rec = new Rectangle(x, y, width, height);
			super.setBounds(rec.x, rec.y, rec.width, rec.height);
			vel = new Vector2(velX, velY);
			
			int i = 0;
			while (!colorName.equalsIgnoreCase(colorNames[i]))
				i++;
			color = new Color(colors[i]);
			area = getArea();

		}

		public Vector2 getPosition() {
			return new Vector2(getX(), getY());
		}

		public Rectangle merge2(Rectangle rect) {
			float minX = Math.min(rec.x, rect.x);
			float maxX = Math.min(rec.x + rec.width, rect.x + rect.width);
			rec.x = minX;
			rec.width = maxX - minX;

			float minY = Math.min(rec.y, rect.y);
			float maxY = Math.min(rec.y + rec.height, rect.y + rect.height);
			rec.y = minY;
			rec.height = maxY - minY;

			return rec;
		}

		public float getArea() {
			return rec.width * rec.height;
		}

		public float getX() {
			return rec.x ;
		}

		public float getY() {
			return rec.y ;
		}

		public float getWidth() {
			return rec.width;
		}

		public float getHeight() {
			return rec.height;
		}

		// "x " +getX() + " , y " +getY() ;
		public String toString() {
			return area + "";
		}

		// TODO may need to check if player later
		private void checkBoundries() {
			if (!this.color.equals(Color.WHITE)) {
				if (getX() > Gdx.graphics.getWidth())
					rec.x = 0;
				else if (getX() + rec.width < 0)
					rec.x = Gdx.graphics.getWidth() - rec.width;
				if (getY() > Gdx.graphics.getHeight())
					rec.y = 0;
				else if (getY() + rec.getHeight() < 0)
					rec.y = Gdx.graphics.getHeight() - rec.getHeight();
			}
			else{
				if (getX() +rec.width> Gdx.graphics.getWidth())
					rec.x = Gdx.graphics.getWidth() - rec.width;
				else if (getX()  < 0)
					rec.x =0 ;
				if (getY() +rec.height > Gdx.graphics.getHeight())
					rec.y =  Gdx.graphics.getHeight() -rec.height;
				else if (getY() < 0)
					rec.y = 0;
			}
		}		
		
		
		
		private void checkBoundries2() {
			if (!this.color.equals(Color.WHITE)) {
				if (getX() + rec.width > Gdx.graphics.getWidth())
					vel.x *= -1 ;
				else if (getX() +rec.width/2 < 0)
					vel.x *= -1 ;
				if (getY() +rec.height > Gdx.graphics.getHeight())
					vel.y *= -1 ;
				else if (getY() +rec.height/2 < 0)
					vel.y *= -1 ;
			}
			else{
				if (getX() +rec.width> Gdx.graphics.getWidth())
					rec.x = Gdx.graphics.getWidth() - rec.width;
				else if (getX()  < 0)
					rec.x =0 ;
				if (getY() +rec.height > Gdx.graphics.getHeight())
					rec.y =  Gdx.graphics.getHeight() -rec.height;
				else if (getY() < 0)
					rec.y = 0;
			}
		}
		
		

		@Override
		public void draw(Batch batch, float alpha) {
			batch.end();
			renderer.begin(ShapeType.Filled);
			renderer.setProjectionMatrix(camera.combined);
			renderer.setColor(color);
			renderer.rect(getX(), getY(), getWidth(), getHeight());
			renderer.end();
			batch.begin();
		}

		@Override
		public void act(float delta) {
			rec.setPosition(getX() + vel.x, getY() + vel.y);
			checkBoundries();
		}/*
		 * public void render(ShapeRenderer renderer, Camera camera ,float
		 * delta) {
		 * 
		 * renderer.begin(ShapeType.Filled);
		 * renderer.setProjectionMatrix(camera.combined);
		 * renderer.setColor(color); renderer.rect(getX(), getY(), getWidth(),
		 * getHeight()); renderer.end(); }
		 */

		@Override
		public int compareTo(Shape o) {

			return (int) (this.area - o.area);
		}

	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Keys.W:
			player.vel.y = 5;
			break;
		case Keys.A:
			player.vel.x = -5;
			break;
		case Keys.S:
			player.vel.y = -5;
			break;
		case Keys.D:
			player.vel.x = 5;
			break;
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.W:
			player.vel.y = 0;
			break;
		case Keys.A:
			player.vel.x = 0;
			break;
		case Keys.S:
			player.vel.y = 0;
			break;
		case Keys.D:
			player.vel.x = 0;
			break;
		}
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
