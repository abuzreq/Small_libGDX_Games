package com.me;

//TODO LOSING *
//TODO speed balancing*
//TODO	scoring system*
//TODO planes
//TODO fragmenting
//TODO  Sounds absolute()/relative()
//TODO 	Alarm atmosphere
//TODO  alarm lights
//TODO  light effect
//TODO  rotating cannon
//TODO  game levelling , increase speed*
///TODO starting messages , levels transition
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * @PossibleRefactorings timers , printArray([x,y,z,y,q]),interpolating vectors>>lines
 * drawing a crosshair , growing the size of shapes(circle , rectangle ..etc)
 * explosion-like rapid change of color (see the change in the colors of the explosions)
 * ensuring a fair chance for all events (ensuring all targets will get at least missile)
 ***working around not being able to remove an element when an iterator is iterator on a collection
 * 
 * @author Abuzreq
 *
 */
public class MissileCommand implements Screen, InputProcessor {

	MyGdxGame game;
	//Sounds
	Music backgroundAlarm = Gdx.audio.newMusic(Gdx.files
			.internal("data/danger_alarm.mp3"));
	Music typing = Gdx.audio
			.newMusic(Gdx.files.internal("data/morse_code.mp3"));
	Sound defenderMissileSound = Gdx.audio.newSound(Gdx.files
			.internal("data/shoot.wav"));
	static Sound explosionSound = Gdx.audio.newSound(Gdx.files
			.internal("data/exp.wav"));

	
	BitmapFont font = new BitmapFont();
	ShapeRenderer renderer = new ShapeRenderer();
	SpriteBatch batch = new SpriteBatch();
	OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight());

	// landscape background
	Sprite landscape;

	// entites
	static Target[] targets = new Target[8];//contains the defender and cities
	static int[] targetsExceptions = new int[8];//used to monitor which target were destroyed 0 means alive , -1 destroyed
	
	float[] defenderRight = { Gdx.graphics.getWidth() - 56, 20f,
			Gdx.graphics.getWidth() - 33, 20, Gdx.graphics.getWidth() - 44, 45 };//the polygon vertices that shapes the right defender
	float[] defenderLeft = { 56, 20f, 33, 20, 44, 45 };//the polygon vertices that shapes the left defender
	int leftDefenderMissile = 15;//remaining missiles for Left
	int rightDefenderMissile = 15;//remaining missiles for Right 

	// shooting and interpolation
	// left
	Vector2 pointer = new Vector2(44, 42);//will be initially filled with mouse click pos
	Vector2 tmpPointer = new Vector2(44, 42);//pos from pointer will be stored here
	Vector2 leftLerp = new Vector2(44, 42);// used to interpolate from the defender to tmpPointer
	boolean isLerpingLeft = false;
	// right
	Vector2 pointer2 = new Vector2(Gdx.graphics.getWidth() - 44, 42);
	Vector2 rightLerp = new Vector2(Gdx.graphics.getWidth() - 44, 42);
	Vector2 tmpPointer2 = new Vector2(Gdx.graphics.getWidth() - 44, 42);
	boolean isLerpingRight = false;

	 /*for de-bouncing (that is on a mouse click multiple click are made ,
	so we allow taking input for once and setting those to false just 
	after that first click and resetting it again to true after the input has been handled)*/
	boolean takeInputLeft = false;
	boolean takeInputRight = false;

	/*stores all missiles and explosions currently alive,the size is
	initialised as the maximum number of enemy missiles at show() which is 
	called at the start of each level
	the size of explosions can't exceed the number of player explosions + the number of 
	enemy missiles ,since cities don't produce explosions their number will not be added
	*/
	static ArrayList<Missile> missiles ;
	static ArrayList<Explosion> explosions = new ArrayList<Explosion>();

	// scoring
	//general 
	static int score = 0;
	private int previousScore;
	int currentLevel = 1;
	//score modifiers
	static int missileScore = 35;
	static int planeScore = 75;
	static int cityScore = 100;
	static int remainingMissilesScore = 10;


	// isStarting and isFirstLevel are used to start and stop the typing message at the first level only (even if restarted)
	boolean isStarting = true;
	boolean isFirstLevel = true;
	
	// win\loss state
	boolean endGame = false;
	boolean levelUp = false;
	boolean isFinalaizing = false;//used to indicate that the game is ending upon (win/loss) conditions

	static int totalNumOfMissiles = 12;//indicates the num of missiles the enemy will fire ,will increment by 2 each level.
	static int remainingNumOfMissiles = 12;//moniter how many of totalNumOfMissiles were already fired
	int remainingCities; //used mainly in finding final score
	int remainingDefenedersMissiles;//used mainly in finding final score

	// initial enemy missiles speed
	float missilesSpeed = 0.7f;
	float lerpingDelta = 0.37f;
	final int MAX_MISSILE = 15;
	//an explosion that takes place upon player defeat.
	Explosion finalExplosion = new Explosion(Gdx.graphics.getWidth() / 2,
			Gdx.graphics.getHeight() / 2, 0, 250);

	Timer timer = Timer.instance();//used to time missiles firing, we need an instance to stop or clear the timer from outside it local domain

	public MissileCommand(MyGdxGame game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// to enable transparency
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		renderer.begin(ShapeType.Filled);
		renderer.setColor(Color.GRAY);
		//finding if all targets were destroyed
		boolean areAllDestoryed = true;
		for (int t = 1; t < targets.length - 1; t++) {
			if (targetsExceptions[t] != 1) {
				renderer.rect(targets[t].x, targets[t].y, targets[t].width,
						targets[t].height);
				areAllDestoryed = false;
			}
		}
		// Precedence of the following two lines is important
		if (!endGame)
			if (areAllDestoryed) {
				endGame = true;
				isFinalaizing = true;
			}
		if (!levelUp && !endGame)
			//if the player haveno more missiles and all the incoming missiles exploded
			if (remainingNumOfMissiles == 0 && missiles.isEmpty()) {
				isFinalaizing = true;
				levelUp = true;
			}

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			explosions.get(i).render(renderer, explosions.get(i).x,
					explosions.get(i).y, explosions.get(i).radius, 12);
			// explosion boundary -for debugging
			// renderer.setColor(Color.PURPLE);
			// renderer.rect(explosions.get(i).getBoundaryBox().x,
			// explosions.get(i).getBoundaryBox().y,
			// explosions.get(i).getBoundaryBox().width,
			// explosions.get(i).getBoundaryBox().height);
		}

		//draw missiles and rotate them depending on their angle
		for (Missile missile : missiles) {
			Arrays.toString(missiles.toArray());
			renderer.rect(missile.x, missile.y, missile.width / 2,
					missile.height / 2, missile.width, missile.height, 1, 1,
					90 + missile.getVelocity().angle(), Color.RED,
					Color.ORANGE, Color.DARK_GRAY, Color.DARK_GRAY);
		}
		renderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);//should be disabled here after the render ends
		
		if ((leftLerp.epsilonEquals(tmpPointer, 0.5f) && !leftLerp.isZero())
				&& isLerpingLeft) {
			isLerpingLeft = false;
			final Explosion explosion = new Explosion(leftLerp.x, leftLerp.y,
					10, 20);
			explosions.add(explosion);
			Timer.schedule(new Task() {

				@Override
				public void run() {
					explosion.grow(1f);
					if (explosion.radius <= 0) {
						this.cancel();
						explosions.remove(explosion);
					}
				}
			}, 0.2f, 0.2f, -2);

			leftLerp.set(44, 42);
			tmpPointer.set(44, 42);
			takeInputLeft = true;
		}
		if ((rightLerp.epsilonEquals(tmpPointer2, 0.5f) && !rightLerp.isZero())
				&& isLerpingRight) {
			isLerpingRight = false;
			final Explosion explosion = new Explosion(rightLerp.x, rightLerp.y,
					10, 20);
			explosions.add(explosion);
			Timer.schedule(new Task() {

				@Override
				public void run() {

					explosion.grow(1f);
					// System.out.println(explosion.radius);

					if (explosion.radius <= 0) {
						explosions.remove(explosion);
						this.cancel();

					}
				}
			}, 0.2f, 0.2f, -2);

			rightLerp.set(Gdx.graphics.getWidth() - 44, 42);
			tmpPointer2.set(Gdx.graphics.getWidth() - 44, 42);
			takeInputRight = true;
		}

		leftLerp.lerp(tmpPointer, lerpingDelta);
		rightLerp.lerp(tmpPointer2, lerpingDelta);

		renderer.setColor(Color.OLIVE);
		renderer.begin(ShapeType.Line);
		//drawing defenders polygons
		if (targetsExceptions[0] != 1)
			renderer.polygon(defenderLeft);
		if (targetsExceptions[targetsExceptions.length - 1] != 1)
			renderer.polygon(defenderRight);

		renderer.setColor(Color.MAROON);
		//creates an array of the size of all missiles to indicate which had exploded
		int[] missilesTmpExceptions = new int[missiles.size()];
		for (int m = 0; m < missiles.size(); m++) {

			boolean flag = missiles.get(m).update();
			if (!flag) {
				missilesTmpExceptions[m] = -1;
			}

			// drawing missiles trace
			renderer.line(new Vector2(missiles.get(m).getInitialX(), missiles
					.get(m).getInitialY()), new Vector2(missiles.get(m).getX()
					+ missiles.get(m).width / 2, missiles.get(m).getY()
					+ missiles.get(m).height / 2));
		}

		//delete the previously marked missiles 
		for (int i = 0; i < missiles.size(); i++) {
			if (missilesTmpExceptions[i] == -1) {
				missiles.remove(i);
			}
		}
		renderer.setColor(Color.BLUE);
		if (isLerpingLeft)
			renderer.line(44, 42, leftLerp.x, leftLerp.y);
		else
			renderer.line(44, 42, 44, 42);
		if (isLerpingRight)
			renderer.line(Gdx.graphics.getWidth() - 44, 42, rightLerp.x,
					rightLerp.y);
		else
			renderer.line(Gdx.graphics.getWidth() - 44, 42,
					Gdx.graphics.getWidth() - 44, 42);
		renderer.end();

		batch.begin();
		landscape.draw(batch);
		font.setColor(Color.WHITE);
		font.draw(batch, "" + score + "  Level " + currentLevel, 10, 480);
		font.setColor(Color.GREEN);
		//Typing starting message
		if (isStarting && isFirstLevel) {
			font.draw(batch, "Even though you had no hand in politics", 210,
					260);
			font.draw(batch,
					" it's YOUR duty soldier to protect our beloved country !",
					180, 220);
			font.draw(batch, "best wishes... ", 430, 190);
			font.draw(batch, "His Highness!", 430, 160);
		}
		font.setColor(Color.RED);
		font.draw(batch, leftDefenderMissile + "", 32, 15);
		font.draw(batch, rightDefenderMissile + "", 667, 15);
		if (endGame)
			batch.draw(new Texture(Gdx.files.internal("data/blank_red.png")),
					0, 0);
		batch.end();

		if (takeInputLeft && targetsExceptions[0] != 1
				&& leftDefenderMissile != 0)
			if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
				takeInputLeft = false;
				// move outside
				Vector3 temporaryVector = new Vector3(Gdx.input.getX(),
						Gdx.input.getY(), 0);

				camera.unproject(temporaryVector);
				pointer.set(temporaryVector.x, temporaryVector.y);
				tmpPointer = pointer.cpy();
				leftDefenderMissile--;
				isLerpingLeft = true;
				defenderMissileSound.play();
			}
		if (takeInputRight
				&& targetsExceptions[targetsExceptions.length - 1] != 1
				&& rightDefenderMissile != 0)
			if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
				takeInputRight = false;
				// move outside
				Vector3 temporaryVector = new Vector3(Gdx.input.getX(),
						Gdx.input.getY(), 0);

				camera.unproject(temporaryVector);
				pointer.set(temporaryVector.x, temporaryVector.y);
				tmpPointer2 = pointer.cpy();
				rightDefenderMissile--;
				isLerpingRight = true;
				defenderMissileSound.play();

			}

		if (endGame) {

			if (isFinalaizing) {
				isFinalaizing = false;

				timer.clear();
				missiles.clear();
				explosions.clear();
				takeInputLeft = false;
				takeInputRight = false;
				Timer.schedule(new Task() {

					@Override
					public void run() {
						finalExplosion.grow(3f);
						if (finalExplosion.radius <= 0) {
							this.cancel();
							restart();
						}
					}
				}, 0.05f, 0.05f, -2);
			}
			renderer.setColor(Color.WHITE);
			renderer.begin(ShapeType.Filled);
			finalExplosion.render(renderer, finalExplosion.x, finalExplosion.y,
					finalExplosion.radius, 10);
			renderer.end();
			batch.begin();

			batch.draw(new Texture(Gdx.files.internal("data/the_end.png")),
					175, 250);
			// this works since the render method of the explosion changes the
			// color of the renderer after each call
			font.setColor(renderer.getColor());
			font.draw(batch, "" + score, 10, 480);
			batch.end();

		}
		if (levelUp) {
			if (isFinalaizing) {
				System.out.println("finalizing");
				isFinalaizing = false;
				remainingCities = 0;
				takeInputLeft = false;
				takeInputRight = false;
				for (int i = 1; i < targetsExceptions.length - 1; i++) {
					if (targetsExceptions[i] == 0)
						remainingCities++;
				}
				// add the missiles score only if the defender isn't destroyed
				int leftFinal = leftDefenderMissile
						* (targetsExceptions[0] == 0 ? 1 : 0);
				int rightFinal = rightDefenderMissile
						* (targetsExceptions[targetsExceptions.length - 1] == 0 ? 1
								: 0);
				remainingDefenedersMissiles = leftFinal + rightFinal;
				System.out.println(remainingDefenedersMissiles + "/"
						+ leftFinal + "/" + rightFinal);
				timer.clear();
				missiles.clear();
				explosions.clear();
				Timer.schedule(new Task() {

					@Override
					public void run() {
						levelUp();

					}
				}, 10f, 0f, 0);

			}
			batch.begin();
			font.setColor(Color.BLUE);
			font.setScale(1.2f);
			font.draw(batch, "LEVEL UP ! ", 280, 450);
			font.setColor(Color.RED);
			font.setScale(1f);
			font.draw(batch, "Scores ", 300, 410);
			font.draw(batch, "Previous ", 175, 340);
			font.draw(batch, "basic ", 197, 293);
			font.draw(batch, "Cities ", 197, 245);
			font.draw(batch, "Missiles ", 179, 197);
			font.draw(batch, "Total ", 197, 136);
			font.setColor(Color.GREEN);
			font.draw(batch, previousScore + "", 425, 340);
			font.draw(batch, score + "", 425, 293);
			font.draw(batch, remainingCities + "*" + cityScore + "="
					+ (remainingCities * cityScore), 425, 245);
			font.draw(batch, remainingDefenedersMissiles + "*"
					+ remainingMissilesScore + "="
					+ (remainingDefenedersMissiles * remainingMissilesScore),
					425, 197);
			font.draw(
					batch,
					(previousScore + score + (remainingCities * cityScore) + (remainingDefenedersMissiles * remainingMissilesScore))
							+ "", 425, 136);
			batch.end();

		}
	}

	// will be called at the beginning of every level
	@Override
	public void show() {

		Gdx.input.setInputProcessor(this);
		//Just interesting to know it :P
		//Gdx.input.setCursorCatched(true);
		takeInputLeft = true;
		takeInputRight = true;
		missiles = new ArrayList<Missile>(totalNumOfMissiles);
		explosions = new ArrayList<Explosion>(totalNumOfMissiles+MAX_MISSILE*2);
		// Preparing the crosshair
		//One approach is to attach a png to the cursor pos , but to make benefit of
		//setCursorImage you need to use a Pixmap
		//	Pixmap pm = new Pixmap(Gdx.files.internal("data/crosshair.png"));
		
		Format format = Pixmap.Format.RGBA8888;//the format required by setCursorImage()
		Pixmap pm = new Pixmap(16, 16, format);
		pm.setColor(Color.WHITE);
		/*
		 * 			|
		 * 		  -----
		 * 			|
		 */
		pm.drawLine(0, 7, 15, 7);//origin(0,0) bottom-left
		pm.drawLine(0, 8, 15, 8);
		pm.drawLine(7, 0, 7, 15);
		pm.drawLine(8, 0, 8, 15);
		int xHotSpot = pm.getWidth() / 2;
		int yHotSpot = pm.getHeight() / 2;
		Gdx.input.setCursorImage(pm, xHotSpot, yHotSpot);

		camera.setToOrtho(false);
		landscape = new Sprite(new Texture(
				Gdx.files.internal("data/landscape.png")));
		landscape.setPosition(0, -5);

		for (int i = 0; i < targets.length; i++) {
			targets[i] = new Target(18 + 92 * i, 0, 42, 48);
		}
		final Random random = new Random();
		isStarting = true;
		backgroundAlarm.setLooping(true);
		backgroundAlarm.setVolume(0.3f);
		backgroundAlarm.play();
		if (isFirstLevel) {
			typing.play();
			typing.setVolume(0.5f);
		}
		//generate a number indicating which target should the missile attack
		// if that target index in excludeList was 0 allow it , otherwise generate another number 
		//until you have a target that wasn't marked for attack
		final int[] probalities = { 0, 0, 0, 0, 0, 0, 0, 0 };// the random
																// generation
																// results
																// storage
		final int[] excludeList = { 0, 0, 0, 0, 0, 0, 0, 0 };// 0 included , 1
																// excluded
		Timer.schedule(new Task() {

			@Override
			public void run() {
				isStarting = false;
				if (isFirstLevel)
					typing.stop();
				backgroundAlarm.setVolume(0.5f);
				takeInputLeft = true;
				takeInputRight = true;
				timer.scheduleTask(new Task() {
					@Override
					public void run() {
						int event = 0;//which target the missile will attack , event will to be balanced for a fair distribution 
						while (true) {//explained above
							event = random.nextInt(targets.length);
							if (excludeList[event] != 1) {
								excludeList[event] = 1;
								probalities[event]++;
								break;
							} else if (excludeList[event] == 1) {
								continue;
							}
						}
					
						boolean areAllOnes = true;
						for (int i = 0; i < excludeList.length; i++) {
							if (excludeList[i] == 0)
								areAllOnes = false;
						}
						//if all targets got marked for attack then reset the excludeList 
						//(this is natural to happen as the number of enemy missiles is normally larger than the number of targets)
						if (areAllOnes)// re-initialise
							for (int i = 0; i < excludeList.length; i++) {
								excludeList[i] = 0;
							}

						Missile missile = new Missile(new Vector2(random
								.nextInt(720), 480), missilesSpeed, event);
						missiles.add(missile);
						remainingNumOfMissiles--;
						if (remainingNumOfMissiles == 0) {
							System.out.print("probalities : ");
							printArray(probalities);

							this.cancel();
						} else if (remainingNumOfMissiles > 2) {//since max number of frags is 2
							Random random = new Random();
							int n = random.nextInt(missiles.size());
							//This is what I found somehow good way of deciding when to fragment
							//level ---- num of missiles  --- probability of fragmentation
							// 1				12					1/12	
							// 2 				12+2=14				2/14  = 1/7
							// 3 				14+2 = 16			2/16   = 1/8
							//n					12+(n-1)*2			num of numbers that are divisible over 7 divided by num of missiles
							if (n % 7 == 0)
								missiles.get(n).fragment(random.nextInt(2) + 1);//1 or 2

						}
						if (endGame)
							this.cancel();

					}
				}, 1, 0.75f, -2);// 0.75f
			}
		}, 3.5f, 0, 0);

	}

	public void printArray(int[] arr) {
		System.out.print("[ ");
		for (int i = 0; i < arr.length - 1; i++) {
			System.out.print(arr[i] + ",");
		}
		System.out.println(arr[arr.length - 1] + " ]");
	}

	public void restart() {
		
		timer.clear();
		endGame = false;
		missiles.clear();
		explosions.clear();
		leftDefenderMissile = MAX_MISSILE;
		rightDefenderMissile = MAX_MISSILE;
		remainingNumOfMissiles = totalNumOfMissiles;
		score = 0;
		isFirstLevel = true;
		for (int i = 0; i < targetsExceptions.length; i++) {
			targetsExceptions[i] = 0;
		}
		currentLevel = 1;
		show();

	}

	public void levelUp() {
		levelUp = false;
		previousScore += score + (remainingCities * cityScore)
				+ (remainingDefenedersMissiles * remainingMissilesScore);
		score = 0;
		leftDefenderMissile = MAX_MISSILE;
		rightDefenderMissile = MAX_MISSILE;
		missilesSpeed += 0.1f;
		totalNumOfMissiles += 2;
		remainingNumOfMissiles = totalNumOfMissiles;
		/*
		 * for (int i = 0; i < targetsExceptions.length; i++) {
		 * targetsExceptions[i] = 0; }
		 */
		targetsExceptions[0] = 0;
		targetsExceptions[targetsExceptions.length - 1] = 0;
		isFirstLevel = false;
		currentLevel++;
		show();
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

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.G) {
			levelUp = true;
			isFinalaizing = true;
			previousScore = score;
		} else if (keycode == Keys.P) {//for debugging
			missilesSpeed += 0.1f;
		} else if (keycode == Keys.Q) {//for debugging
			Random random = new Random();
			missiles.get(random.nextInt(missiles.size())).fragment(3);

		}
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
