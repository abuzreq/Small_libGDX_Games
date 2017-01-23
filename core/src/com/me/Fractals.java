package com.me;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Fractals implements Screen {
	MyGdxGame game;
	ShapeRenderer renderer;
	OrthographicCamera camera;
	SpriteBatch batch = new SpriteBatch();

	public Fractals(MyGdxGame game) {
		this.game = game;
	}

	ArrayList<Integer> ys = new ArrayList<Integer>();
	ArrayList<Integer> xs = new ArrayList<Integer>();

	ArrayList<Line> lines;

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		/*
		 * float[] arr = new float[1000]; for(int i = 0 ; i< arr.length && i <
		 * lines.size() ; i++){
		 * 
		 * arr[i] = lines.get(i).getP1().x ; arr[i+1] = lines.get(i).getP1().y ;
		 * arr[i+2] = lines.get(i).getP2().x ; arr[i+3] = lines.get(i).getP2().y
		 * ;
		 * 
		 * }
		 */
		renderer.begin(ShapeType.Line);
		// renderer.polygon(arr);
		renderer.setProjectionMatrix(camera.combined);
		// for(int i = 0 ; i< lines.size() ; i++)
		// renderer.line(lines.get(i).getP1(), lines.get(i).getP2());

		int i = 0;

		renderer.end();
		renderer.begin(ShapeType.Line);
		while (i != numPoints - 1) {
			renderer.line(xs.get(i), ys.get(i), xs.get(i + 1), ys.get(i + 1));
			i++;
		}
		renderer.end();

	}

	@Override
	public void resize(int width, int height) {

	}

	int i = 0;
	Vector2 last1;
	Vector2 last2;

	int randx1;
	int randx2;

	int randy1;
	int randy2;

	int numPoints = 20;

	@Override
	public void show() {
		renderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		Random random = new Random();

		float bend = 2.7f;
		xs.add(0);

		ys.add(0);
		for (int j = 1; j < numPoints; j++) {

			ys.add(ys.get(j - 1) + (random.nextInt(8) + 2));

		}

		for (int i = 1; i < numPoints; i++) {
			int m = ys.get(i - 1) < 80 ? 1 : -1;

			int dir = random.nextInt(2) == 0 ? -1 : 1;

			xs.add((int) (xs.get(i - 1) + random.nextInt(5) * dir + bend * m));
		}

		// randx1 = xs.get(random.nextInt(xs.size()/2));
		// randx2 = xs.get(random.nextInt(xs.size()+xs.size()/2));

		randy1 = randx1;
		randy2 = randx2;

		lines = new ArrayList<Line>();
		lines.add(new Line(new Vector2(0, 0), new Vector2(10, 0), 0));
		// lines.add(new Line(lines.get(0).getP1(),new Vector2(10,10),120));
		// last1 = lines.get(0).getP1() ;
		last2 = lines.get(0).getP2();

		// -2 is FOREVER

		Timer.schedule(new Task() {

			@Override
			public void run() {
				i++;
				if (i % 2 == 0)
					lines.add(new Line(new Vector2(last2), new Vector2(last2
							.add(10, 0)), 0));
				if (i % 2 != 0)
					lines.add(new Line(new Vector2(last2), new Vector2(last2
							.add(10, 0)), 60));

				// (float) (Math.PI/3f)
			}
		}, 0.4f, 0.4f, -2);

	}

	class Line {
		Vector2 p1;
		Vector2 p2;
		float rotation;

		public Line(Vector2 p1, Vector2 p2, float rotation) {
			this.p1 = p1;
			this.p2 = p2;
			this.rotation = rotation;
			p2.rotate((float) (rotation));
			// p1.rotate((float)(rotation));
			// p2.setAngle(p2.angle()+rotation);

		}

		public Vector2 getP1() {
			return p1;
		}

		public void setP1(Vector2 p1) {
			this.p1 = p1;
		}

		public Vector2 getP2() {
			return p2;
		}

		public void setP2(Vector2 p2) {
			this.p2 = p2;
		}

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

}
