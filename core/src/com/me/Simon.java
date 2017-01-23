package com.me;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.me.DataStructures.DLCL;
import com.me.DataStructures.DLN;
import com.me.DataStructures.Queue;


public class Simon implements Screen, InputProcessor {
	MyGdxGame game;
/*
	private Square red;
	private Square yellow;
	private Square green;
	private Square blue;
	*/
	
	private Sprite begin;
	
	private Square[] squares = new Square[4];
	
	Sound razz;
	private SpriteBatch batch;
	private OrthographicCamera camera;

	BitmapFont font = new BitmapFont();

	private Random random;
	private boolean gameOn = false;
	DLCL<Integer> list;

	// to keep updating the animation
	private int current = -1;
	// private int paternLength = 0;
	// private int pointer = 0;
	DLN<Integer> currentNode;
	DLN<Integer> displayNode;
	public static boolean takeInput = false;

	public static int input = -1;


	int score = 0;

	public Simon(MyGdxGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		squares[0].draw(batch);
		squares[1].draw(batch);
		squares[2].draw(batch);
		squares[3].draw(batch);
		
		begin.draw(batch);
		font.draw(batch, "score \\ " + score, -220, 230);
		batch.end();

		if (takeInput && input != -1)
			takeInput(input);


		//if( = true ){
		squares[0].supply(current);
		squares[1].supply(current);
		squares[2].supply(current);
		squares[3].supply(current);
	//	}
		// System.out.println(list);
		razz = Gdx.audio.newSound(Gdx.files.internal("data/razz.wav"));

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

		// Squares Initialising
		squares[0] = new Square(new Texture("data/red1.png"), new Texture(
				"data/red2.png"), -155, 0, 0, "data/red.ogg");
		squares[1] = new Square(new Texture("data/yellow1.png"), new Texture(
				"data/yellow2.png"), 27, 0, 1, "data/yellow.ogg");
		squares[2] = new Square(new Texture("data/green1.png"), new Texture(
				"data/green2.png"), -155, -165, 2, "data/green.ogg");
		squares[3] = new Square(new Texture("data/blue1.png"), new Texture(
				"data/blue2.png"), 27, -165, 3, "data/blue.ogg");

		begin = new Sprite(new Texture("data/begin.png"));
		begin.setPosition(-90, 150);

		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		random = new Random();

		Gdx.input.setInputProcessor(new InputMultiplexer(this, squares[0], squares[1],
				squares[2], squares[3]

		));

	}

	public void changeColor(int t) {
		current = displayNode.getData();
		Timer.schedule(new Task() {

			@Override
			public void run() {
				current = -1;

			}
		}, t, t, 1);
	}

	public void startGame() {
		list = new DLCL<Integer>(new DLN<Integer>(random.nextInt(4)));
		System.out.println(list);
		current = list.getHead().getData();
		resetColorOnTime(0.5f, 0.5f, 1);
		currentNode = list.getHead();
		allowInput();
		score = 0;

	}

	public void takeInput(int in) {
		Gdx.graphics.setContinuousRendering(false );
		System.out.println("in takeInput list " + list);
		takeInput = false;
		input = -1;
		System.out.println("you clicked : " + in);
		current = in;
		
		PLayPrioritrizedSound(in);
		
		Timer.schedule(new Task() {
			@Override
			public void run() {

				current = -1;
			}
		}, 0.5f, 0.5f, 1);
		/*
		 * if(currentNode.data == list.head.data) currentNode = list.head.next;
		 * else { System.out.println("But that's wrong head"); list.clear();
		 * startGame(); }
		 */
		System.out.println("in takeInput list 2" + list);
		if (currentNode.getData() == in) {

			if (currentNode.getNext() == list.getHead()) {
				System.out.println("that's all");
				Timer.schedule(new Task() {
					@Override
					public void run() {
						newSequence();
					}

				}, 1.2f, 1.2f, 0);
				score++;
			} else if (currentNode.getNext() != list.getHead()) {
				System.out.println(currentNode.toString());
				currentNode = currentNode.getNext();
				takeInput = true;

			}
		} else if (currentNode.getData() != in) {
			System.out.println("But that's wrong");
			razz.play();
			list.clear();
			startGame();
		}
		Gdx.graphics.setContinuousRendering(true );
	}

	final Queue<Integer> queue = new Queue<Integer>();

	public void newSequence() {
		list.add2Tail(random.nextInt(4));
		System.out.println(list);

		
		
		displayNode = list.getHead();
		queue.enqueue(displayNode.getData());
		displayNode = displayNode.getNext();
		// Too fast?!
		int i = 0;
		while (displayNode.getNext() != list.getHead().getNext()) {
			queue.enqueue(displayNode.getData());
			displayNode = displayNode.getNext();
		}
		// int tmp = ;

		System.out.println("queue  " + queue);
		System.out.println("queue length " + queue.length());

		// System.out.println("I am on my way : " + displayNode);
		/*
		 * if(gameOn){ current = list.head.data ; gameOn = false ; }
		 */
	
		resetColorOnTime2(1f, 1f, queue.length());

		currentNode = list.getHead();
		allowInput();
	}

	boolean toNext = true;

	
	public void PLayPrioritrizedSound(int i)
	{
		squares[0].setSoundPriority(0);
		squares[1].setSoundPriority(0);
		squares[2].setSoundPriority(0);
		squares[3].setSoundPriority(0);
		
		squares[i].playSound(1);
	}

	//change this name 
	public void resetColorOnTime2(final float delay, final float interval,
			int repetition) {
		Timer.schedule(new Task() {

			@Override
			public void run() {
			
				if (!queue.isEmpty() && toNext) {
					toNext = false;
					int tmp = queue.dequeue();
					current = tmp;
					PLayPrioritrizedSound(tmp);
				
				
				}
				
				resetColorOnTime(1f , 1f , 0);
				if (queue.isEmpty())
					resetColorOnTime(delay , interval , 1);
				// resetColorOnTime2(0.5f, 0.5f, 1);
			}

		}, delay, interval, repetition);
	}

	public void allowInput() {
		takeInput = true;
		input = -1;

	}

	public void resetColorOnTime(float delay, float interval, int repetition) {
		Timer.schedule(new Task() {

			@Override
			public void run() {

				current = -1;
				toNext = true;

			}

		}, delay, interval, repetition);
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
		batch.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.P)
			System.out.println(list);
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

		Vector3 vec = new Vector3(screenX, screenY, 0);
		camera.unproject(vec);
		if (begin.getBoundingRectangle().contains(vec.x, vec.y)) {
			// to be changed into a limited way
			// gameOn = true;
			startGame();

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
