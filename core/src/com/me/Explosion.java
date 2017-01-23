package com.me;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Explosion extends Circle {

	private float upperlimit;
	private int dir = 1;
	private Rectangle boundaryBox = new Rectangle();

	private Color[] explosionColors = { Color.YELLOW, Color.WHITE, Color.BLUE,
			Color.ORANGE, Color.WHITE, Color.PURPLE };
	int currentColor = 0;
	static final int numRepeats = 4;
	int i = numRepeats;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param upperlimit of growth
	 */
	public Explosion(float x, float y, float radius, float upperlimit) {
		setPosition(x, y);
		this.setRadius(radius);
		boundaryBox.setCenter(x, y);
		boundaryBox.setPosition(x - radius, y - radius);
		boundaryBox.height = boundaryBox.width = radius * 2;

		this.upperlimit = upperlimit;
	}

	public void grow(float delta) {
		if (radius + delta >= upperlimit) {
			dir = -1;
		}

		setRadius(this.radius + dir * delta);
		/*
		 * the multiplication of delta by (1.57)is because I want the rate of
		 * change of the box to be equal to the rate of the circle if B is the
		 * box area . C is the circle area , f is the combined multiplications
		 * and divisions needed to let dB/dt == dC/dt dB/dt = 2 *width * dw/dt
		 * => dB/dt = 2*2*radius*dr/dt ,(dr/dt == dw/dt== delta ) dC/dt =
		 * 2*PI*radius*dr/dt if we equate the equation dB/dt * f = dC/dt, we
		 * will find that f = PI/2 = 1.57
		 */
		boundaryBox.setSize(boundaryBox.width + dir * delta * 1.57f,
				boundaryBox.height + dir * delta * 1.57f);

		// System.out.println(boundaryBox);
		Vector2 center = new Vector2(x, y);
		boundaryBox.setCenter(center);
		boundaryBox.setPosition(center.x - boundaryBox.width / 2, center.y
				- boundaryBox.height / 2);

	}

	public void update() {
		int[] toBeRemoved = new int[MissileCommand.missiles.size()];
		int i = 0;
		for (Missile missile : MissileCommand.missiles) {

			if (this.getBoundaryBox().overlaps(missile)) {
				final Explosion explosion = new Explosion(missile.x, missile.y,
						10, 15);
				MissileCommand.explosions.add(explosion);
				Timer.schedule(new Task() {

					@Override
					public void run() {

						explosion.grow(1f);

						if (explosion.radius <= 0) {
							MissileCommand.explosions.remove(explosion);
							this.cancel();
						}
					}
				}, 0.2f, 0.2f, -2);
				// MissileCommand.missiles.remove(i);

				// i++;
				toBeRemoved[i] = 1;
			} else
				i++;

		}

		for (int j = 0; j < toBeRemoved.length; j++) {
			if (toBeRemoved[j] == 1) {
				MissileCommand.missiles.remove(j);
				MissileCommand.explosionSound.play();
				MissileCommand.score += MissileCommand.missileScore;
			}
		}
	}

	public void render(ShapeRenderer renderer, float x, float y, float radius,
			int segs) {
		i--;
		Color cl = explosionColors[currentColor];
		renderer.setColor(cl.r, cl.g, cl.b, 0.8f);
		if (i == 0) {
			i = numRepeats;
			currentColor += 1;
			currentColor %= explosionColors.length;
		}

		renderer.circle(x, y, radius, segs);

	}

	public Rectangle getBoundaryBox() {
		return boundaryBox;
	}

	public void setBoundaryBox(Rectangle boundryBox) {
		this.boundaryBox = boundryBox;
	}

}
