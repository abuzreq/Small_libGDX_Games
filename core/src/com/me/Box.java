package com.me;

import java.util.Random;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/***
 * 
 * This class @extends Rectangle and @implements InputProcessor
 * can be used to generate and draw a single or a grid or rectangles with random color or one general color (if specified)
 * you can specify that you are drawing a grid and then scale the boxes and have a separation between them 
 * @author Abuzreq
 *
 */
public class Box extends Rectangle implements InputProcessor {
	
	private Color color ;
	protected static final int BOX_DIM = 60 ;
	static Color defaultColor ;
	static boolean useDefaultColor =false ;
	//you can add as many colours as you want
	static Color[] colors = {Color.BLUE, Color.RED , Color.LIGHT_GRAY ,Color.ORANGE ,Color.MAGENTA , Color.GREEN,Color.CYAN};
	static String[] colorNames = {"BLUE","RED" , "LIGHTGRAY" ,"ORANGE" ,"MAGNETA" , "GREEN","YELLOW"};
	static Random random = new Random();
	
	protected static ShapeRenderer renderer  = new ShapeRenderer(); 
	
	//Grid Drawing
	private final static Vector2 FullyScaled = new Vector2(1,1);
	static Vector2 gridScaleVector  = new Vector2(1,1);//default : there is no scaling.
	static float gridSeperation = 0;//default : all boxes are adjacent.
	static boolean isDrawingGrid = false ;
	
	public Box(int x ,int  y  )
	{
		setPosition(x, y);
		setCenter(x, y);
		setWidth(BOX_DIM);
		setHeight(BOX_DIM);	
		if(useDefaultColor)
			color = defaultColor ;
		else
		color = (colors[random.nextInt(colors.length)]);		
	}
	/**
	 * if isDrawingGrid , it will lerp the scaling vector until it reaches a final vector (1,1)
	 * setting isDrawingGrid is your responsibility
	 * @param scaleDelta the scaling amount
	 */
	
	public static void scaleGrid(float scaleDelta)
	{
		if(isDrawingGrid)
		{
			if(gridScaleVector.x < 0.95f)
				gridScaleVector = gridScaleVector.lerp(FullyScaled,scaleDelta );
				else {
					gridScaleVector.x = 1;
					gridScaleVector.y = 1;
					}
		}
	}
	/** will draw a rectangle ,if isDrawingGrid : the width and height is scaled by static vector2:gridScaleVector and separated by a static float gridSeperation from the neighbouring boxes **/
	public void draw() {
		renderer.setColor(color);
		renderer.begin(ShapeType.Filled);
		if(isDrawingGrid)
		renderer.rect(getX(), getY(), BOX_DIM*(gridScaleVector.x-gridSeperation), BOX_DIM*(gridScaleVector.y-gridSeperation));
		else
			renderer.rect(getX(), getY(), BOX_DIM, BOX_DIM);
		renderer.end();
	
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

	
	public static Vector2 getGridScaleVector() {
		return gridScaleVector;
	}



	public static void setGridScaleVector(Vector2 gridScaleVector) {
		Box.gridScaleVector = gridScaleVector;
	}



	public static float getGridSeperation() {
		return gridSeperation;
	}



	public static void setGridSeperation(float gridSeperation) {
		Box.gridSeperation = gridSeperation;
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {		
		return false;
	}

	static String getColorName(Color color)
	{
		for(int i = 0 ; i < colors.length ;i++)
		{
			if(colorEquals(color,colors[i]))
				return colorNames[i];	
		}
		return "not known :( ";//make it professional
	}
	 public String getColorName()
	{
		for(int i = 0 ; i < colors.length ;i++)
		{
			if(colorEquals(this.color,colors[i]))
				return colorNames[i];	
		}
		return "not known :( ";
	}
	
	/**
	 * @param c1 the first color
	 * @param c2 the second color
	 * @return whether the RGBA of the two colors are equal (after rounding to have all the closely nearby degrees of a colour considered equal to it)
	 * **/
	public static boolean colorEquals(Color c1 , Color c2)
	{
		if(c1==null || c2 ==null)
			return false ;
		if(Math.round(c1.r) == Math.round(c2.r) && Math.round(c1.g) == Math.round(c2.g) && Math.round(c1.b) == Math.round(c2.b) && Math.round(c1.a) == Math.round(c2.a) )
				return true ;
		else return false ;
				 
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
	
	public String toString()
	{
		return "[Color:"+getColorName(color)+" ,X:"+getX()+" ,Y:"+getY()+"]";
	}
	public Color getColor() {
		
		return  color;
	}
	public void setColor(Color color) {
		this.color =color ;
		
	}

}
