package com.me.tetris;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.me.MyGdxGame;

/**
 * @Note subarray : a 1D array that holds indices(row,column) to finite cells of a larger mainarray
 * @Possible_Refactorings : Array rotation , rotation of a sub array inside a larger one (considering the contents of the mainarray)
 *  ,Checking if a subarray lies completely inside a larger one , finding the cell in a subarray that have the least y index
 *  clearings : clear all array , clear a subarray , clear a cell or  a line
 *  ,checks for consecutive rows are all filled with a value
 *  shifting and overriding a line with the lines above
 *  Synchronisation between two subarrays
 *  applies a subarray in a mainarray
 *  shifting a subarray up , down , left or right inside a mainarray while taking
 *  into considerations the other contents of the mainarray , so : and whether a subarray can be shifted 
 *  printing 1d and 2d arrays
 * @author Abuzreq
 *
 */
public class Tetris implements InputProcessor, Screen {
	// the pivot of the subarrays that represents the block should always be the
	// first two elements
	MyGdxGame game;


	//Sounds
	Music tetrisSound = Gdx.audio.newMusic(Gdx.files.internal("data/tetris.ogg"));
	Sound endSound = Gdx.audio.newSound(Gdx.files
			.internal("data/tetris_end.mp3"));
	Sound blipSound = Gdx.audio.newSound(Gdx.files.internal("data/blip.wav"));
	
	
	Timer timer = Timer.instance();
	BitmapFont font = new BitmapFont();
	Sprite playGround = new Sprite(new Texture(
			Gdx.files.internal("data/playground.png")));
	private Sprite nextGround = new Sprite(new Texture(
			Gdx.files.internal("data/next.png")));

	Sprite t = new Sprite(new Texture(Gdx.files.internal("data/T.png")));
	SpriteBatch batch = new SpriteBatch();
	ShapeRenderer renderer = new ShapeRenderer();

	OrthographicCamera camera = new OrthographicCamera();
	int score = 0;
	boolean gameover = false;
	char[] shapesTypes = { 'i', 'o', 't', 's', 'z', 'j', 'l' };

	Random random = new Random();
	
	boolean createNew = false;
	int[] tetromino = new int[8];
	int[] shadowTetromino = new int[8];
	int[] nextTetromino = new int[8];

	char currentTetromino;
	char nextTetrominoShape;

	int[][] cells = new int[10][20];
	int[][] cellsCopy = new int[10][20];

	int[][] shadowCells = new int[10][20];
	int[][] nextCells = new int[5][2];

	int[] zcells = { 2, 10, 1, 10, 2, 11, 3, 11 };
	int[] tcells = { 4, 9, 5, 9, 3, 9, 4, 8 };

	int[] test = { 0, 1, 1, 1, 2, 1, 3, 1, 4, 1, 5, 1, 6, 1, 7, 1, 8, 1, 9, 1 };
	int[] test2 = { 0, 2, 1, 2, 2, 2, 3, 2, 4, 2, 5, 2, 6, 2, 7, 2, 8, 2, 9, 2 };

	
	
//	Temporary for rotation
	boolean rotationFlag = true;
	int[] tmpRotation = new int[tetromino.length];
	public Tetris(MyGdxGame game) {
		this.game = game;

	}
//Temporary 

	Color tmpColor = new Color();

	/**
	 * changes the color of the passed renderer depending on cell (defined by
	 * those indices) value
	 * */
	public void setColorDraw(ShapeRenderer renderer, float relX, float relY,
			int[][] cells, int i, int j) {
		if (cells[i][j] == 't') {
			renderer.setColor(Color.MAGENTA);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);

		} else if (cells[i][j] == 'z') {
			renderer.setColor(Color.RED);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'j') {
			renderer.setColor(Color.BLUE);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'l') {
			renderer.setColor(Color.ORANGE);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'o') {
			renderer.setColor(Color.YELLOW);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'i') {
			renderer.setColor(Color.CYAN);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 's') {
			renderer.setColor(Color.GREEN);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		}

	}

	public void setColorDrawAlpha(ShapeRenderer renderer, float relX,
			float relY, int[][] cells, int i, int j, float alphaMultiplier) {
		if (cells[i][j] == 't') {
			renderer.setColor(Color.MAGENTA.r, Color.MAGENTA.g,
					Color.MAGENTA.b, Color.MAGENTA.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);

		} else if (cells[i][j] == 'z') {
			renderer.setColor(Color.RED.r, Color.RED.g, Color.RED.b,
					Color.RED.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'j') {
			renderer.setColor(Color.BLUE.r, Color.BLUE.g, Color.BLUE.b,
					Color.BLUE.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'l') {
			renderer.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b,
					Color.ORANGE.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'o') {
			renderer.setColor(Color.YELLOW.r, Color.YELLOW.g, Color.YELLOW.b,
					Color.YELLOW.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 'i') {
			renderer.setColor(Color.CYAN.r, Color.CYAN.g, Color.CYAN.b,
					Color.CYAN.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		} else if (cells[i][j] == 's') {
			renderer.setColor(Color.GREEN.r, Color.GREEN.g, Color.GREEN.b,
					Color.GREEN.a * alphaMultiplier);
			renderer.box(relX + i * 32 + 1, relY + j * 32 + 1, 0, 30, 30, 0);
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// batch.setProjectionMatrix(camera.combined);
		batch.begin();
		playGround.draw(batch);
		nextGround.draw(batch);
		font.draw(batch, "score : " + score, 415, 120);
		batch.end();

		// to be able to have alpha rendering using ShapeRenderer
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		renderer.begin(ShapeType.Filled);
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				setColorDraw(renderer, playGround.getX(), playGround.getY(),
						cells, i, j);
				setColorDrawAlpha(renderer, playGround.getX(),
						playGround.getY(), shadowCells, i, j, 0.3f);
			}
		}

		for (int n = 0; n < nextCells.length; n++) {
			for (int m = 0; m < nextCells[n].length; m++) {
				setColorDraw(renderer, nextGround.getX(), nextGround.getY(),
						nextCells, n, m);
			}
		}
		renderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		
		if(gameover)resetGame();

	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
	
		tetrisSound.play();
		tetrisSound.setLooping(true);

		currentTetromino = shapesTypes[random.nextInt(shapesTypes.length)];
		tetromino = formShape(5, 22, currentTetromino);
		apply(cells, tetromino, currentTetromino);

		shadowTetromino = formShape(5, 19, currentTetromino);
		synchronizeShadow(shadowTetromino, tetromino);

		nextTetrominoShape = shapesTypes[random.nextInt(shapesTypes.length)];
		nextTetromino = formShape(2, 1, nextTetrominoShape);
		apply(nextCells, nextTetromino, nextTetrominoShape);

		playGround.setPosition(30, 30);
		nextGround.setPosition(356, 606);
		Gdx.input.setInputProcessor(this);

		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				if (!gameover) {
					if (createNew) {
						createNew = false;
						if (!rotationFlag) {
							for (int i = 0; i < tmpRotation.length; i++) {
								tetromino[i] = tmpRotation[i];
							}
							apply(cells, tetromino, currentTetromino);
						}
						System.out.println("Create New");

						currentTetromino = nextTetrominoShape;
						tetromino = formShape(5, 22, currentTetromino);
						apply(cells, tetromino, currentTetromino);

						shadowTetromino = formShape(5, 19, currentTetromino);
						synchronizeShadow(shadowTetromino, tetromino);

						nextTetrominoShape = shapesTypes[random
								.nextInt(shapesTypes.length)];
						nextTetromino = formShape(2, 1, nextTetrominoShape);
						clearAll(nextCells);
						apply(nextCells, nextTetromino, nextTetrominoShape);
					}
					boolean flag = down(cells, tetromino, currentTetromino);
					if (!flag) {
						blipSound.play();
						apply(cells, tetromino, currentTetromino);
						isFilled(0, cells[0].length);
						if (!isBlockInBorder(cells, tetromino)) {
							System.out.println("GameOver");
							gameover = true;
							tetrisSound.setVolume(0.6f);
							tetrisSound.setVolume(0.2f);
							endSound.play(1);
						} else {
							createNew = true;
						}
					}

				}
			}
		}, 1, 1, -2);

	}

/**
 *  copies the content of @original to @copy
 * @param original
 * @param copy
 */
	public void copyTo(int[][] original, int[][] copy) {
		for (int i = 0; i < original.length; i++) {
			for (int j = 0; j < original[i].length; j++) {
				copy[i][j] = original[i][j];
			}
		}
	}

	/**
	 * Synchronises @shadowTetromino with the  transformations of @tetromino 
	 * @param shadowTetromino
	 * @param tetromino
	 */
	public void synchronizeShadow(int[] shadowTetromino, int[] tetromino) {
		int[] tmp = new int[tetromino.length];
		for (int i = 0; i < tetromino.length; i++) {
			tmp[i] = tetromino[i];
		}
		// I am making this copy to stop the flickering in the main tetromino
		copyTo(cells, cellsCopy);
		boolean flag = true;
		while (flag) {
			flag = down(cellsCopy, tmp, currentTetromino);
		}
		for (int i = 0; i < tmp.length; i++) {
			shadowTetromino[i] = tmp[i];
		}
		clearAll(shadowCells);
		apply(shadowCells, shadowTetromino, currentTetromino);
	}

	@Override
	public void hide() {

	}

	ArrayList<Integer> array;
/**
 *  Not Complete!
 */	
	protected void simulate() {
		array = new ArrayList<Integer>();
		array.add(2);
		array.add(1);

		array.add(1);
		array.add(1);
		array.add(2);
		array.add(2);
		array.add(3);
		array.add(2);
		int[] arr = new int[array.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = array.get(i);
		}
		apply(cells, arr, 1);

		// creates point as origin
		// allow you to add to the array through clicks
		// also to appoint the origin through rearranging the array
	}

	/**
	 * applies this subarray with @value to the main 2d cells array passed
	 * **/
	public void apply(int[][] mainarray, int[] subarray, int value) {

		for (int i = 0, j = 1; i < subarray.length && j < subarray.length; i += 2, j += 2) {
			if (isInBorders(mainarray, subarray, i, j))
				mainarray[subarray[i]][subarray[j]] = value;
		}
	}

	/** @return whether the cells in subarray with index i ,j is inside the borders of mainarray**/
	public boolean isInBorders(int[][] mainarray, int[] subarray, int i, int j) {
		boolean inX = (subarray[i] < mainarray.length && subarray[i] >= 0);
		boolean inY = (subarray[j] < mainarray[0].length && subarray[j] >= 0);
		if (inX && inY)
			return true;
		else {
			return false;
		}
	}

	//Checks to see if all cells of the subarray is inside border of the mainarray (0,0) is at the bottomleft
	public boolean isBlockInBorder(int[][] mainarray, int[] subarray) {
		boolean flag = true;

		for (int i = 0, j = 1; i < subarray.length && j < subarray.length; i += 2, j += 2) {
			flag &= isInBorders(mainarray, subarray, i, j);
		}
		return flag;
	}

	/**
	 * 
	 * @param xPivot x pivot of rotation
	 * @param yPivot y pivot of rotation
	 * @param type type of the shape : z,s,t,o,l,j,i
	 * @return
	 */
	public int[] formShape(int xPivot, int yPivot, char type) {

		int[] shape = new int[8];
		if (type == 'z') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot - 1;
			shape[3] = yPivot;

			shape[4] = xPivot;
			shape[5] = yPivot - 1;

			shape[6] = xPivot + 1;
			shape[7] = yPivot - 1;
			return shape;
		} else if (type == 's') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot + 1;
			shape[3] = yPivot;

			shape[4] = xPivot;
			shape[5] = yPivot - 1;

			shape[6] = xPivot - 1;
			shape[7] = yPivot - 1;
			return shape;
		} else if (type == 't') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot + 1;
			shape[3] = yPivot;

			shape[4] = xPivot;
			shape[5] = yPivot - 1;

			shape[6] = xPivot - 1;
			shape[7] = yPivot;
			return shape;
		} else if (type == 'o') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot + 1;
			shape[3] = yPivot;

			shape[4] = xPivot;
			shape[5] = yPivot - 1;

			shape[6] = xPivot + 1;
			shape[7] = yPivot - 1;
			return shape;
		} else if (type == 'l') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot;
			shape[3] = yPivot - 1;

			shape[4] = xPivot + 1;
			shape[5] = yPivot;

			shape[6] = xPivot + 2;
			shape[7] = yPivot;
			return shape;
		} else if (type == 'j') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot;
			shape[3] = yPivot - 1;

			shape[4] = xPivot - 1;
			shape[5] = yPivot;

			shape[6] = xPivot - 2;
			shape[7] = yPivot;
			return shape;
		} else if (type == 'i') {

			shape[0] = xPivot;
			shape[1] = yPivot;

			shape[2] = xPivot - 1;
			shape[3] = yPivot;

			shape[4] = xPivot + 1;
			shape[5] = yPivot;

			shape[6] = xPivot + 2;
			shape[7] = yPivot;
			return shape;
		} else
			return null;

	}

	/**
	 * checks to see if the consecutive pair (a,b) are in this array
	 * 
	 */
	public boolean isThere(int[] array, int a, int b) {
		boolean flag = false;
		for (int i = 0, j = 1; i < array.length && j < array.length; i += 2, j += 2) {
			if (a == array[i] && b == array[j])
				flag = true;
		}
		return flag;
	}

	/**
	 * @returns true if this array content can be shifted by the amount xShift
	 *          and yShift
	 * **/
	public boolean canMove(int[][] mainarray, int[] subarray, int xShift,
			int yShift) {
		boolean flag = true;
		for (int i = 0, j = 1; i < subarray.length && j < subarray.length; i += 2, j += 2) {
			if (subarray[j] < mainarray[0].length)
				if (mainarray[subarray[i] + xShift][subarray[j] + yShift] != 0
						&& !isThere(subarray, subarray[i] + xShift, subarray[j]
								+ yShift)) {
					flag = false;
				}
		}
		return flag;
	}

	/**
	 * 
	 * @param subarray
	 * @return the part of the array with the smallest y 
	 */
	public int findBottom(int[] subarray) {
		int min = subarray[1]; // first y value
		for (int k = 1; k < subarray.length; k += 2) {
			if (subarray[k] < min)
				min = subarray[k];
		}
		return min;
	}

	public int findBottomX(int[] subarray) {
		int minIndex = 1;
		int min = subarray[minIndex]; // first y value

		for (int k = 1; k < subarray.length; k += 2) {
			if (subarray[k] < min) {
				min = subarray[k];
				minIndex = k;
			}
		}
		return subarray[minIndex - 1];
	}

	/**
	 * ** transform the values in @subarray down by one under conditions
	 * 
	 * @param subarray
	 * @param value
	 */
	public boolean down(int[][] mainarray, int[] subarray, int value) {

		int min = findBottom(subarray);

		if (min != 0) {

			if (canMove(mainarray, subarray, 0, -1)) {
				for (int i = 0, j = 1; i < subarray.length
						&& j < subarray.length; i += 2, j += 2) {
					if (subarray[j] < mainarray[0].length)
						mainarray[subarray[i]][subarray[j]] = 0;
					subarray[j] -= 1;

				}
				apply(mainarray, subarray, value);
				return true;
			} else {
				clear(mainarray, subarray);
				apply(mainarray, subarray, value);
				return false;
			}

		} else
			return false;
	}

	public boolean up(int[][] mainarray, int[] subarray, int value) {

		int min = findBottom(subarray);

		// if (min != 0) {

		if (canMove(mainarray, subarray, 0, 1)) {
			for (int i = 0, j = 1; i < subarray.length && j < subarray.length; i += 2, j += 2) {
				if (subarray[j] < mainarray[0].length)
					mainarray[subarray[i]][subarray[j]] = 0;
				subarray[j] += 1;

			}
			apply(mainarray, subarray, value);
			return true;
		} else {
			// apply(mainarray, subarray, value);
			return false;
		}

		// } else
		// return false;
	}

	public void right(int[][] mainarray, int[] subarray, int value) {

		int rightmost = subarray[0]; // first x value
		for (int k = 0; k < subarray.length; k += 2) {
			if (subarray[k] > rightmost)
				rightmost = subarray[k];

		}
		if (rightmost != 9) {
			if (canMove(mainarray, subarray, 1, 0))
				for (int i = 0, j = 1; i < subarray.length
						&& j < subarray.length; i += 2, j += 2) {
					if (subarray[j] < mainarray[0].length)
						mainarray[subarray[i]][subarray[j]] = 0;
					subarray[i] += 1;
				}
			apply(mainarray, subarray, value);
		}
	}

	public void left(int[][] mainarray, int[] subarray, int value) {

		int leftmost = subarray[0]; // first x value
		for (int k = 0; k < subarray.length; k += 2) {
			if (subarray[k] < leftmost)
				leftmost = subarray[k];

		}
		if (leftmost != 0) {
			if (canMove(mainarray, subarray, -1, 0))
				for (int i = 0, j = 1; i < subarray.length
						&& j < subarray.length; i += 2, j += 2) {
					if (subarray[j] < mainarray[0].length)
						mainarray[subarray[i]][subarray[j]] = 0;
					subarray[i] -= 1;
				}
			apply(mainarray, subarray, value);
		}
	}

	/**
	 * sets the part in the main array that is occupied by 
	 * the cell with index i,j in the subarray to zero 
	 * @param mainarray
	 * @param subarray
	 * @param i
	 * @param j
	 */
	public void clear(int[][] mainarray, int[] subarray, int i, int j) {
		if (isInBorders(mainarray, subarray, i, j))
			mainarray[subarray[i]][subarray[j]] = 0;
	}

	/**
	 * sets all cells of arr to zero
	 * @param arr
	 */
	public void clearAll(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = 0;
			}
		}
	}

	/**  sets the part in the main array that the subarray currently occupies to zero
	 * @param mainarray
	 * @param subarray
	 */
	public void clear(int[][] mainarray, int[] subarray) {
		for (int i = 0, j = 1; i < subarray.length && j < subarray.length; i += 2, j += 2) {
			if (isInBorders(mainarray, subarray, i, j))
				cells[subarray[i]][subarray[j]] = 0;
		}
	}

	public void printCells(int[][] array) {
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				System.out.print(array[i][j] + "\t");
			}
			System.out.println();
		}
	}

	public void printCells(int[] array) {
		for (int i = 0; i < array.length; i = i + 2) {

			System.out.print(array[i] + " , " + array[i + 1] + " : ");

		}
		System.out.println();
	}

	// needs special cases care
	// I will keep this method to show the amount of work I was going to do if I
	// didn't abstracted the
	// Rotation to work for all cases (the code below is only to take care of
	// the T tetromino)
	public void rotateT(int[][] mainarray, int[] subarray, char ch, int value) {
		if (ch == 't') {
			for (int i = 2, j = 3; i < subarray.length && j < subarray.length; i += 2, j += 2) {

				if (subarray[i] == (subarray[0] + 1)
						&& subarray[j] == subarray[1]) {
					clear(mainarray, subarray, i, j);
					subarray[i] -= 1;
					subarray[j] -= 1;
				} else if (subarray[i] == (subarray[0] - 1)
						&& subarray[j] == subarray[1]) {
					clear(mainarray, subarray, i, j);
					subarray[i] += 1;
					subarray[j] += 1;
				} else if (subarray[i] == (subarray[0])
						&& subarray[j] == (subarray[1] + 1)) {
					clear(mainarray, subarray, i, j);
					subarray[i] += 1;
					subarray[j] -= 1;
				} else if (subarray[i] == (subarray[0])
						&& subarray[j] == (subarray[1] - 1)) {
					clear(mainarray, subarray, i, j);
					subarray[i] -= 1;
					subarray[j] += 1;
				}

			}

		}
		apply(mainarray, subarray, value);
	}

	// need configuirng to rotate both sides
	public int[] rotateVector(int a, int b, double angle) {
		int[] rot = { 0, -1, 1, 0 };
		int[] rotationMatrix = { (int) Math.cos(angle), (int) -Math.sin(angle),
				(int) Math.sin(angle), (int) Math.cos(angle) };
		// System.out.println(rotationMatrix[0] +" ; "+ rotationMatrix[1]+" ; "+
		// rotationMatrix[2]+" ; "+rotationMatrix[3]);
		int[] newVec = { rotationMatrix[0] * a + rotationMatrix[1] * b,
				rotationMatrix[2] * a + rotationMatrix[3] * b };
		return newVec;
	}

	// TODO Generalize
	// later maybe check how many fills ,so that you can add bonus to scores
	public void isFilled(int j, int j2) {
		boolean filled = true;
		int tmp = j;
		int[] toBeEliminated = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
				-1, -1, -1, -1, -1, -1, -1, -1, -1 };
		for (; j < j2; j++) {

			filled = true;
			for (int i = 0; i < cells.length; i++) {

				if (cells[i][j] == 0) {
					filled = false;
					break;
				}
			}

			if (filled) {
				toBeEliminated[j] = j;
			}

		}
		for (int i = toBeEliminated.length - 1; i >= 0; i--) {
			if (toBeEliminated[i] != -1) {
				eliminateLine(toBeEliminated[i]);
				score += 10;
				blipSound.play();

			}
		}

	}

	public void eliminateLine(int line) {
		for (int i = 0; i < cells.length; i++) {
			cells[i][line] = 0;
		}
		consume(line);
	}
//TODO 
	public void consume(int line) {

		for (int j = line; j < cells[0].length; j++) {
			for (int i = 0; i < cells.length; i++) {
				if (j != cells[0].length - 1) {
					cells[i][j] = cells[i][j + 1];
				} else
					cells[i][j] = 0;
			}
		}

	}
/**
 * @param mainarray 
 * @param subarray
 * @param value
 * @param angle
 * @return
 */
	public boolean rotate(int[][] mainarray, int[] subarray, int value,
			double angle) {
		if (value == 'o')
			return false;

		boolean flag = true;
		clear(mainarray, subarray);
		int[] tmp = new int[subarray.length];
		for (int i = 0; i < tetromino.length; i++) {
			tmp[i] = subarray[i];
		}
		// copyTo(mainarray,cellsCopy);

		if (tmp.length > 2) {
			// int[] transformStorage = new int[subarray.length-2];

			for (int i = 2, j = 3; i < tmp.length && j < tmp.length; i += 2, j += 2) {
				// vector of the point relative to origin
				int a = tmp[i] - tmp[0];
				int b = tmp[j] - tmp[1];
				int[] transformed = rotateVector(a, b, angle);
				boolean checkX = ((tmp[0] + transformed[0]) < mainarray.length)
						&& ((tmp[0] + transformed[0]) >= 0);
				boolean checkY = ((tmp[1] + transformed[1]) < mainarray[0].length)
						&& ((tmp[1] + transformed[1]) >= 0);

				int ftransformX = subarray[0] + transformed[0];
				int ftransformY = subarray[1] + transformed[1];
				if (checkX && checkY) {
					boolean isThere = !isThere(subarray, ftransformX,
							ftransformY);
					// System.out.println("isThere : " + isThere);
					boolean canTransform = (mainarray[ftransformX][ftransformY] != 0);
					// System.out.println("canTransform : " + canTransform);
					if (isThere && canTransform) {
						flag = false;

					} else {
						tmp[i] = ftransformX;
						tmp[j] = ftransformY;
					}
				} else
					flag = false;

			}

		}
		// System.out.println("Flag : " + flag);
		if (flag) {
			clear(mainarray, subarray);
			for (int i = 0; i < tmp.length; i++) {
				subarray[i] = tmp[i];
			}

			apply(mainarray, subarray, value);
		}
		return flag;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		endSound.dispose();
		tetrisSound.dispose();
		blipSound.dispose();
		renderer.dispose();
		batch.dispose();
		playGround.getTexture().dispose();
	}


	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.R) // Rotation to right
		{
			for (int i = 0; i < tetromino.length; i++) {
				tmpRotation[i] = tetromino[i];
			}//copies the current tetromino subarray to temporary one
			rotationFlag = rotate(cells, tetromino, currentTetromino,
					-Math.PI / 2);
			synchronizeShadow(shadowTetromino, tetromino);
			if (rotationFlag)
				blipSound.play();
		} 
		else if (keycode == Keys.Q) {// Rotation to left
			for (int i = 0; i < tetromino.length; i++) {
				tmpRotation[i] = tetromino[i];
			}
			rotationFlag = rotate(cells, tetromino, currentTetromino,
					Math.PI / 2);
			if (rotationFlag)
				blipSound.play();
			synchronizeShadow(shadowTetromino, tetromino);
		}
		// rotate(tcells , 't',1);
		// t.rotate90(true);

		if (keycode == Keys.S)
			down(cells, tetromino, currentTetromino);

		else if (keycode == Keys.D) {

			right(cells, tetromino, currentTetromino);
			right(shadowCells, shadowTetromino, currentTetromino);
			synchronizeShadow(shadowTetromino, tetromino);

		} else if (keycode == Keys.A) {

			left(cells, tetromino, currentTetromino);
			left(shadowCells, shadowTetromino, currentTetromino);
			synchronizeShadow(shadowTetromino, tetromino);

		} else if (keycode == Keys.W)
			fastDrop(tetromino, currentTetromino);

		else if (keycode == Keys.P)
		//	printCells(cells); 
			resetGame();

		return false;
	}

	// in cells only
	//will keep calling down until the tetromino is blocked
	public void fastDrop(int[] subarray, char value) {
		boolean flag = true;
		while (flag)
			flag = down(cells, subarray, value);

	}


	public void resetGame()
	{
		timer.clear();
		tetrisSound.stop();
		gameover = false ;
		timer.scheduleTask(new Task(){
			@Override
			public void run() {
				clearAll(cells);
				clearAll(cellsCopy);
				clearAll(shadowCells);
				clearAll(nextCells);				
				score = 0 ;
				tetrisSound.setVolume(1);
				timer.clear();
				show();
			}}, 3,0,0);
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
