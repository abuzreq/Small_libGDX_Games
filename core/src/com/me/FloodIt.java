package com.me;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * 
 * @author Abuzreq
 *
 */
public class FloodIt implements Screen {

	private int gridDim = 14  ;
	static int steps = 0 ;
	private int MaxSteps = 25 ; // should later on depend on the gridDim
	private SpriteBatch batch;
	private OrthographicCamera camera;
	ShapeRenderer renderer;
	MyGdxGame game;
	FloodBox[][] boxes;
	// pints are stored i this sequence : x1,y1,x2,y2,x3,y3,...,xn,yn
	ArrayList<Point> flood;

	static Color floodColor;
	static Color anticipated;
	
	BitmapFont font = new BitmapFont();
	
	// used to scale the boxes at the beginning 
	static Vector2 scaleVec ;
	// used to keep a fixed separation between boxes 
	static float separation = 0.07f ;
	
	public FloodIt(MyGdxGame game) {
		this.game = game;
	}
	
	
	static boolean startIt =  false ;
	
	
	@Override
	public void render(float delta) {

		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl20.glClearColor(0, 0, 0, 1);

		batch.setProjectionMatrix(camera.combined);
		
		
		
		if(startIt){
			Box.scaleGrid(0.055f);
		}				
		for (int j = 0; j < boxes.length; j++) {
			for (int i = 0; i < boxes[j].length; i++) {	
					boxes[i][j].draw();
			}
		}
		batch.begin();
		
		if(!startIt)
			font.draw(batch, "Press Enter to Start ...", -30, 435);
		else
			font.draw(batch, steps+" / "+MaxSteps, -60, 435);

		batch.end();

		if (anticipated != null) {
			
			floodFill(anticipated);
		}

	}


	public Color getBoxColor(int i, int j) {
		return boxes[i][j].getColor();
	}

	private void colorWithIncreasingTime(final ArrayList<Point> list,
			final Color color, float initialTime) {
		for (int k = 0; k < list.size(); k++) {
			final int tmpInt = k;
			Timer.schedule(new Task() {
				@Override
				public void run() {
					boxes[list.get(tmpInt).i][list.get(tmpInt).j]
							.setColor(color);
				}
			}, initialTime * k, 1, 0);

		}
	}

	private void floodFill(Color color) {
		if(!Box.colorEquals(color, floodColor))
			steps++;
		anticipated = null;
		int i = 0;
		// the x, y of the boxes inside the around list are in indices form 
		floodColor = color;

		colorWithIncreasingTime(flood, floodColor, 0.02f);

		// we copy the elements in flood to a temporary list to be able to
		// remove the elements we had iterated on without affecting flood
		// if only next() were used I noticed it will be slower 
		ArrayList<Point> list = new ArrayList<Point>();
		list.addAll(flood);
		Iterator<Point> it = list.iterator();
		
		while (it.hasNext()) {

			Point p = it.next();
			it.remove();
			System.out.println(list.size());

			// if not at the right most of the grid
			if (p.getI() != gridDim-1) {
				i += add2Flood(p, 1, 0);
			}
			// if not at the bottom most of the grid
			if (p.getJ() != gridDim-1) {
				i += add2Flood(p, 0, 1);			
			}
			// if not at the left most of the grid
			if (p.getI() != 0) {
				i += add2Flood(p, -1, 0);				
			}
			// if not at the top most of the grid
			if (p.getJ() != 0) {
				i += add2Flood(p, 0, -1);		
			}			
		}// end of while loop
		
		// each iteration on all flood elements will add the adjacent boxes only , if i != 0 then there is
		//a chance that more boxes of the same colour of the adjacent can be added.
		if (i != 0) {
			floodFill(color);
		}

	}

	/**
	 * if the the box at the (point + the shift) is not in flood and its colour
	 * is equal to the flood colour then it's added if not a duplicate
	 * @param p    the reference point    
	 * @param i    the shift on I-axis from the reference point
	 * @param j     the shift on J-axis from the reference point
	 */
	private int add2Flood(Point p, int i, int j) {
		Point tmp = new Point(p.add2I(i), p.add2J(j));
		if (!boxes[tmp.getI()][tmp.getJ()].isInFlood()
				&& Box.colorEquals(floodColor,
						getBoxColor(tmp.getI(), tmp.getJ())))
			if (!contains(flood, tmp)) {
				flood.add(tmp);
				return 1 ;
			}
			 return 0 ;
	}

	//TODO might want to try hashsets later on
	public boolean contains(ArrayList<Point> list, Point element) {

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(element))
				return true;
		}
		return false;
	}



	@Override
	public void show() {
	
	
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getWidth());
		renderer = new ShapeRenderer();
		
		//Try changing those 4 to see the effect
		FloodBox.setCamera(camera);
		FloodBox.isDrawingGrid = true ;
		FloodBox.setGridSeperation(0.07f);
		FloodBox.setGridScaleVector(new Vector2(0.1f,0.1f));
		
		// Initialling the boxes
		// all the boxes depend for their location on the first top left box.
		int initialX = -Gdx.graphics.getWidth() / 2 + Box.BOX_DIM/2 ;
		int initialY = Gdx.graphics.getHeight() / 2 - (2*Box.BOX_DIM)+10;
		boxes = new FloodBox[gridDim][gridDim];
		boxes[0][0] = new FloodBox(initialX, initialY);
		boxes[0][0].setInFlood(true);

		
		
		/**makes sure that no adjacent boxes to the initial will have the same color at the beginning of the game**/
		for (int j = 0; j < boxes.length; j++) {
			for (int i = 0; i < boxes[j].length; i++) {			
				boxes[i][j] = new FloodBox((int) initialX + FloodBox.BOX_DIM * (i),
						initialY - FloodBox.BOX_DIM * j);
				if(i == 0 && j==0 )floodColor = boxes[i][j].getColor();
				
				// Although it takes extra time to check for these conditions Boxes.length ^2 I found it worth it , especially since it's done once in the beginning
				if(i == 0 && j ==1){	System.out.println("here it comes !");
					if(Box.colorEquals(boxes[i][j].getColor(), floodColor)){
						System.out.println("hey that fella below is kidding !");
						boxes[i][j] = new FloodBox((int) initialX + Box.BOX_DIM * (i),initialY - Box.BOX_DIM * j);
						}}
				if(i == 1 && j ==0){	System.out.println("here it comes2 !");
				if(Box.colorEquals(boxes[i][j].getColor(), floodColor)){
					System.out.println("hey that fella below is kidding 2!");
					boxes[i][j] = new FloodBox((int) initialX + Box.BOX_DIM * (i),initialY - Box.BOX_DIM * j);
					}}
															
			}
		}

		flood = new ArrayList<Point>(gridDim*gridDim);
		flood.add(new Point(0, 0));
		// top left corner-box
		floodColor = boxes[0][0].getColor();
		
		font.setScale(2f);
		// to make all boxes responsive
		
		
		InputMultiplexer multi = new InputMultiplexer();
		for (int j = 0; j < boxes.length; j++) {
			for (int i = 0; i < boxes[j].length; i++) {

				multi.addProcessor(boxes[i][j]);
			}
		}
		
		Gdx.input.setInputProcessor(new InputMultiplexer(multi));

		
		//I hereby claim that this is a sort of cellular automata :P
		// enable to auto solve (it's hard to determine how many steps are needed from the beginning)
		//	autoSolver(0.005f);		
		// enable to see an entertaining colour pattern (becomes be boring after a while though)
		//	makeSalad();
	}

	// :)
	 void makeSalad() {
		for (int i = 0; i < gridDim*gridDim; i++) {
			final int k = i % Box.colors.length;

			Timer.schedule(new Task() {

				@Override
				public void run() {
					anticipated = Box.colors[k];

				}
			}, 0.5f * i, 1, 0);
		}
	}

	 //generate a random color each time which will eventually solve it :P
	 void autoSolver(float speed) {
		final Random rand = new Random();
		Timer.schedule(new Task() {

			@Override
			public void run() {
				if (flood.size() == gridDim*gridDim) {
					this.cancel();
				}
				anticipated = Box.colors[rand.nextInt(Box.colors.length)];

			}
		}, 0.5f, speed, -2);// -2 Forever
	}

	@Override
	public void dispose() {
		batch.dispose();
		renderer.dispose();
		
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
	public void resize(int width, int height) {

	}

	
	
	class Point {
		int i;
		int j;

		public Point(int i, int j) {
			this.i = i;
			this.j = j;
		}

		// ;
		public int getI() {
			return i;
		}

		public int getJ() {
			return j;
		}

		public int add2I(int n) {
			return this.i + n;
		}

		public int add2J(int n) {
			return this.j + n;
		}

		public String toString() {
			return "[" + getI() + " : " + getJ() + "]";
		}

		@Override
		public boolean equals(Object other) {
			if (other != null && other instanceof Point) {
				Point point = (Point) other;
				if (this.i == point.i && this.j == point.j)
					return true;
				else {
					return false;
				}
			} else {
				return false;
			}
		}

	}
}
