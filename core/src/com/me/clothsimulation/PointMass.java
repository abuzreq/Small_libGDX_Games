package com.me.clothsimulation;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class PointMass implements InputProcessor {
	float lastX, lastY; // for calculating position change (velocity)
	float x, y;
	float accX, accY;

	float mass = 1;
	float damping = 20;

	ShapeRenderer renderer = new ShapeRenderer();
	// An ArrayList for links, so we can have as many links as we want to this
	// PointMass
	ArrayList<Link> links = new ArrayList<Link>();

	boolean pinned = false;
	float pinX, pinY;

	// PointMass constructor
	PointMass(float xPos, float yPos) {
		x = xPos;
		y = yPos;

		lastX = x;
		lastY = y;

		accX = 0;
		accY = 0;
	}

	public void render(float delta) {
		updatePhysics(delta);
		draw();
	}

	public void updatePhysics(float timeStep) { // timeStep should be in elapsed
												// seconds (deltaTime)
		this.applyForce(0, mass * Verlet.gravity);

		float velX = x - lastX;
		float velY = y - lastY;

		// dampen velocity
		velX *= 0.99f;
		velY *= 0.99f;

		float timeStepSq = timeStep * timeStep;
		// TODO try multiplying the velocity with timestep to get motion
		// equation
		// calculate the next position using Verlet Integration
		float nextX = x + velX + 0.5f * accX * timeStepSq;
		float nextY = y + velY + 0.5f * accY * timeStepSq;

		// reset variables
		lastX = x;
		lastY = y;

		x = nextX;
		y = nextY;

		accX = 0;
		accY = 0;
	}

	public void updateInteractions() {
		// this is where our interaction comes in.
		if (mousePressed) {
			float distanceSquared = Verlet.distPointToSegmentSquared(pmouseX,
					pmouseY, mouseX, mouseY, x, y);
			if (mouseButton == Buttons.LEFT) {
				if (distanceSquared < Verlet.mouseInfluenceSize) { // remember
																	// mouseInfluenceSize
																	// was
																	// squared
																	// in
																	// setup()
					// To change the velocity of our PointMass, we subtract that
					// change from the lastPosition.
					// When the physics gets integrated (see updatePhysics()),
					// the change is calculated
					// Here, the velocity is set equal to the cursor's velocity
					lastX = x - (mouseX - pmouseX)
							* Verlet.mouseInfluenceScalar;
					lastY = y - (mouseY - pmouseY)
							* Verlet.mouseInfluenceScalar;
				}
			} else { // if the right mouse button is clicking, we tear the cloth
						// by removing links
				if (distanceSquared < Verlet.mouseTearSize)
					links.clear();
			}
		}
	}

	public void solveConstraints() {
		/* Link Constraints */
		// Links make sure PointMasss connected to this one is at a set distance
		// away
		for (int i = 0; i < links.size(); i++) {
			Link currentLink = (Link) links.get(i);
			currentLink.solve();
		}

		/* Boundary Constraints */
		// These if statements keep the PointMasss within the screen
		if (y < 1)
			y = 2 * (1) - y;
		if (y > Verlet.height - 1)
			y = 2 * (Verlet.height - 1) - y;

		if (x > Verlet.width - 1)
			x = 2 * (Verlet.width - 1) - x;
		if (x < 1)
			x = 2 * (1) - x;

		/* Other Constraints */
		// make sure the PointMass stays in its place if it's pinned
		if (pinned) {
			x = pinX;
			y = pinY;
		}
	}

	// attachTo can be used to create links between this PointMass and other
	// PointMasss
	public void attachTo(PointMass P, float restingDist, float stiff) {
		attachTo(P, restingDist, stiff, 30, true);
	}

	public void attachTo(PointMass P, float restingDist, float stiff,
			boolean drawLink) {
		attachTo(P, restingDist, stiff, 30, drawLink);
	}

	public void attachTo(PointMass P, float restingDist, float stiff,
			float tearSensitivity) {
		attachTo(P, restingDist, stiff, tearSensitivity, true);
	}

	public void attachTo(PointMass P, float restingDist, float stiff,
			float tearSensitivity, boolean drawLink) {
		Link lnk = new Link(this, P, restingDist, stiff, tearSensitivity,
				drawLink);
		links.add(lnk);
	}

	public void pinTo(float pX, float pY) {
		pinned = true;
		pinX = pX;
		pinY = pY;
	}

	public void applyForce(float fX, float fY) {
		// acceleration = (1/mass) * force
		// or
		// acceleration = force / mass
		accX += fX / mass;
		accY += fY / mass;
	}

	public void removeLink(Link lnk) {
		links.remove(lnk);
	}

	public void draw() {
		// draw the links and points

		if (links.size() > 0) {
			for (int i = 0; i < links.size(); i++) {
				Link currentLink = (Link) links.get(i);
				currentLink.draw();
			}
		} else {
			renderer.begin(ShapeType.Point);
			renderer.point(x, y, 0);
			renderer.end();
		}
		// the previous mouse coordinates is taken in touchdown and the new in touchdragged
		//draw mouse dragging trace
		renderer.begin(ShapeType.Line);
		renderer.line(mouseX, mouseY, pmouseX, pmouseY);
		renderer.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	OrthographicCamera camera = new OrthographicCamera(Verlet.width,
			Verlet.height);
	boolean mousePressed = false;
	float mouseX;
	float mouseY;
	float pmouseX;
	float pmouseY;
	float dmouseX;
	float dmouseY;

	int mouseButton;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Vector3 vec = new Vector3(screenX - Verlet.width / 2, -screenY
				+ Verlet.height / 2, 0);
		camera.project(vec);
		mouseX = vec.x;
		mouseY = vec.y;

		mousePressed = true;
		mouseButton = button;

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		pmouseX = 0;
		pmouseY = 0;
		mouseX = 0;
		mouseY = 0;
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		Vector3 vec = new Vector3(screenX - Verlet.width / 2, -screenY
				+ Verlet.height / 2, 0);
		camera.project(vec);
		System.out.println("[" + vec.x + " : " + vec.y + "]");

		pmouseX = mouseX;
		pmouseY = mouseY;
		mouseX = vec.x;
		mouseY = vec.y;
		// dmouseX = vec.x - mouseX ;
		// dmouseY = vec.y - mouseY ;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
