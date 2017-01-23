package com.me;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Missile extends Rectangle {

	private Vector2 velocity;
	private int target;
	private float initialX;
	private float initialY;
	private Vector2 targetOrigin = new Vector2();
	float speed;// to give the same speed for fragments

	public Missile(Vector2 position, float speed, int target) {
		this.width = 2.5f;
		this.height = 8;
		initialX = position.x;
		initialY = position.y;
		this.speed = speed;
		this.setPosition(position);

		this.target = target;
		targetOrigin
				.set((MissileCommand.targets[target].getX() + MissileCommand.targets[target].width / 2),
						(MissileCommand.targets[target].getY() + MissileCommand.targets[target].height / 2));
		// a vector that is the difference between the missile initial position
		// and the target
		Vector2 vec = new Vector2(targetOrigin.x - position.x, targetOrigin.y
				- position.y);
		// the direction of the above vector
		this.velocity = new Vector2(speed * vec.nor().x, speed * vec.nor().y);

	}

	public boolean update() {
		this.setX(velocity.x + getX());
		this.setY(velocity.y + getY());
		Vector2 vec = new Vector2();
		getPosition(vec);
		// if the target wasn't eliminated , eliminate it , otherwise check if
		// the missile went outside the borders
		if (vec.epsilonEquals(targetOrigin, 4f)) {
			if (MissileCommand.targetsExceptions[target] != 1) {
				MissileCommand.targetsExceptions[target] = 1;
				MissileCommand.explosionSound.play();
				return false;
			}
		}
		if (vec.y <= 0)
			return false;
		return true;
	}

	public void fragment(int n) {
		// max 5 fragments , n <= missiles.length
		Missile[] missiles = new Missile[5];
		Random random = new Random();
		int rand = random.nextInt(MissileCommand.targets.length);
		for (int i = 0; i < n; i++) {			
			while (rand == target) {// the fragments shouldn't have the same target as the main missile
				rand = random.nextInt(MissileCommand.targets.length);
			}
			Vector2 vec = new Vector2();
			this.getPosition(vec);
			missiles[i] = new Missile(vec, speed, rand);
		}
		for (int i = 0; i < missiles.length; i++) {
			if (missiles[i] != null) {
				MissileCommand.missiles.add(missiles[i]);
				MissileCommand.remainingNumOfMissiles--;
			}
		}
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public float getInitialX() {
		return initialX;
	}

	public void setInitialX(float initialX) {
		this.initialX = initialX;
	}

	public float getInitialY() {
		return initialY;
	}

	public void setInitialY(float initialY) {
		this.initialY = initialY;
	}

}
