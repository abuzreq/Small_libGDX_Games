package com.me;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/***
 * I created this class to make Box more abstract and less coupled 
 * a let all the methods and variables that are closely attached and relative to the game here
 * @super Box 
 * @author Abuzreq
 * 
 */
public class FloodBox extends Box {
	static OrthographicCamera camera = null;
	private boolean isInFlood = false;

	public FloodBox(int x, int y) {
		super(x, y);
	}

	public void draw() {
		if (camera != null)
			renderer.setProjectionMatrix(camera.combined);
		super.draw();
	}

	public boolean isInFlood() {
		return isInFlood;
	}

	public void setInFlood(boolean isInFlood) {
		this.isInFlood = isInFlood;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public static void setCamera(OrthographicCamera camera) {
		FloodBox.camera = camera;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER)
			FloodIt.startIt = true;
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
		FloodBox.gridScaleVector = gridScaleVector;
	}

	public static float getGridSeperation() {
		return gridSeperation;
	}

	public static void setGridSeperation(float gridSeperation) {
		FloodBox.gridSeperation = gridSeperation;
	}

	// TODO Fix this coupling
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 vec = new Vector3(screenX, screenY, 0);
		if (camera != null)
			camera.unproject(vec);
		if (contains(vec.x, vec.y)) {
			FloodIt.anticipated = this.getColor();
			return true;
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

	public String toString() {
		return "[Color:" + getColorName(getColor()) + " ,X:" + getX() + " ,Y:"
				+ getY() + "]";
	}

}
