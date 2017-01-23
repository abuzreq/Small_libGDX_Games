package com.me;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.JointDef.JointType;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * @Possible_Refactorings : class:Entity  , method:scaleParticleEmitter(ParticleEffect, float) ,the  XML parsing template in show()
 * @author Abuzreq
 *
 */
public class AttachIt implements Screen, InputProcessor {

	World world;
	OrthographicCamera camera;
	SpriteBatch batch = new SpriteBatch(); // for drawing the particle effects
	ShapeRenderer rend = new ShapeRenderer();
	Player player;
	ArrayList<Wall> walls = new ArrayList<Wall>();
	Entity winningEntity;

	// musics
	Sound explosionSound = Gdx.audio.newSound(Gdx.files
			.internal("data/explosion.wav"));
	Sound jointCreationSound = Gdx.audio.newSound(Gdx.files
			.internal("data/joint.wav"));
	Music backgroundMusic = Gdx.audio.newMusic(Gdx.files
			.internal("data/background.wav"));

	private final float TIMESTEP = 1f / 60f;
	private final int VELOCITYITERATIONS = 15;
	private final int POSITIONITERATION = 11;

	boolean jointOn = true;
	boolean jointOn2 = false;
	// So that joint sound not player at first time
	boolean isFirstJoint = true;

	BitmapFont font = new BitmapFont();
	ParticleEffect explosionEffect;
	ParticleEffect fireworkEffect;
	boolean explode = false;
	boolean playerWon = false;
	MyGdxGame game;

	public AttachIt(MyGdxGame game) {
		this.game = game;
	}

	

	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// might zoom in relation to player rotation :)
		// currently the camera follows the player
		//camera.position.set(player.getX(), cameraNewPos.y, 0);
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
		// box2d world step and render
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);

		// drawing joints
		rend.setProjectionMatrix(camera.combined);

		//draws the joints
		rend.begin(ShapeType.Line);
		if (player.joint1 != null) {
			rend.setColor(Color.BLUE);
			rend.line(player.getX(), player.getY(),
					player.joint1.getAnchorB().x, player.joint1.getAnchorB().y);
		}
		if (player.joint2 != null) {
			rend.setColor(Color.ORANGE);
			rend.line(player.getX(), player.getY(),
					player.joint2.getAnchorB().x, player.joint2.getAnchorB().y);
		}
		rend.end();

		// drawing walls
		rend.begin(ShapeType.Filled);
		rend.setColor(Color.DARK_GRAY);
		for (int i = 0; i < walls.size(); i++) {
			// finding the bottom left corner (it's the x ,y for the ShapeRenderer), getX() of the wall will get the x of the box2d:body which is the center
			float x1 = walls.get(i).getX() - walls.get(i).width / 2;
			float y1 = walls.get(i).getY() - walls.get(i).height / 2;

			rend.rect(x1, y1, walls.get(i).width, walls.get(i).height);
		}
		// drawing player
		rend.setColor(Color.GREEN);
		rend.rect(player.getX() - player.width / 2, player.getY()
				- player.height / 2, player.width, player.height);
		//drawing winning entity
		rend.setColor(Color.CYAN);
		rend.circle(winningEntity.getX(), winningEntity.getY(),
				winningEntity.width);
		rend.end();

		// Particle Effects Rendering
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		if (explode) {
			explode = false;

			explosionEffect.start();
			explosionSound.play();
			player.destroyJoint1();
			player.destroyJoint2();

			Timer.schedule(new Task() {

				@Override
				public void run() {
					player.getBody().setAngularVelocity(0);
					player.getBody().setLinearVelocity(0, 0);
					player.getBody().setTransform(0, -10, 0);
					player.createJoint(1, walls.get(0), new Vector2(0, 0),
							new Vector2(walls.get(0).getX(), walls.get(0)
									.getY()), true);
				}
			}, 0.6f);

		}
		if (playerWon)
			font.draw(batch, "You Won!", winningEntity.getX() - 45,
					winningEntity.getY() - 8);
		if (playerWon) {
			fireworkEffect.start();
			fireworkEffect.draw(batch, delta);
		}
		explosionEffect.draw(batch, delta);//even though the drawing is called , nothing will appear until the effect starts
		explosionEffect.setPosition(player.getX(), player.getY());
		
		batch.end();
	}

	
	
	
	@Override
	public void show() {

		// starting the music
		backgroundMusic.setLooping(true);
		backgroundMusic.play();

		world = new World(new Vector2(0, -5f), false);
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / 22f,
				Gdx.graphics.getHeight() / 22f);
		player = new Player(world, 0, -10, 1, 1, BodyType.DynamicBody);
		world.setContactListener(player);

		winningEntity = new Entity(world, 62, 152, 1.5f, 1.5f,
				BodyType.StaticBody);
		winningEntity.getFixture().setSensor(true);

		font.scale(0.1f);

		// parsing the positions and dimensions of the walls from an XML file
		XmlReader reader = new XmlReader();
		try {
			Element root = reader.parse(Gdx.files.internal("data/Walls.xml"));
			String EOD = root.getAttribute("EOD");// parsing End Of Document
													// attribute
			for (int i = 0;; i++) {

				Element wall = root.getChild(i);

				// checks if the parser had reached the EOD which was previously
				// parsed from the root attributes
				if (wall.getName().equalsIgnoreCase(EOD))
					break;
				Wall tmp = new Wall(world, wall.getFloat("x"),
						wall.getFloat("y"), wall.getFloat("height"),
						wall.getFloat("width"), BodyType.StaticBody);
				walls.add(tmp);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// creates an initial joint with the nearest wall (this assumes that the
		// nearest wall have the highest precedence in the XML file (child(0)))
		player.createJoint(1, walls.get(0), new Vector2(0, 0), new Vector2(
				walls.get(0).getX(), walls.get(0).getY()), true);
		isFirstJoint = false;
		Gdx.input.setInputProcessor(this);

		// Initialising the explosion effect (emitted when the player collide
		// with one of the walls)
		explosionEffect = new ParticleEffect();
		explosionEffect.load(Gdx.files.internal("data/explosion.p"),
				Gdx.files.internal("data"));
		float delta = 0.05f;
		scaleParticleEmitter(explosionEffect, delta);

		// Initialising the fireworks effect (emitted after winning)
		fireworkEffect = new ParticleEffect();
		fireworkEffect.load(Gdx.files.internal("data/fireworks.p"),
				Gdx.files.internal("data"));
		fireworkEffect.setPosition(winningEntity.getX(), winningEntity.getY());
		delta = 0.08f;
		scaleParticleEmitter(fireworkEffect, delta);
		cameraNewPos = new Vector2(player.getX(), player.getY());

	}

	/**
	 * will change the scale(size) and velocity of each of the @particleEffect
	 * emitters by multiplying the current highMax and lowMax of the scale and
	 * velocity by @delta
	 * 
	 * @param particleEffect
	 * @param delta
	 */
	public static void scaleParticleEmitter(ParticleEffect particleEffect,
			float delta) {
		delta = 0.08f;
		for (ParticleEmitter emitter : particleEffect.getEmitters()) {
			float scaling = emitter.getScale().getHighMax();

			emitter.getScale().setHigh(scaling * delta);
			scaling = emitter.getScale().getLowMax();
			emitter.getScale().setLow(scaling * delta);
			scaling = emitter.getVelocity().getHighMax();
			emitter.getVelocity().setHigh(scaling * delta);
			scaling = emitter.getVelocity().getLowMax();
			emitter.getVelocity().setLow(scaling * delta);
		}
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
		world.dispose();

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.A) {
			player.getBody().applyForceToCenter(new Vector2(-300, 0), true);

		} else if (keycode == Keys.D) {
			player.getBody().applyForceToCenter(new Vector2(300, 0), true);

		}

		else if (keycode == Keys.Q) {
			player.destroyJoint1();
			return true;

		} else if (keycode == Keys.E) {
			player.destroyJoint2();
			return true;

		} else if (keycode == Keys.S) {
			if (player.joint1 != null)
				player.joint1.setMaxLength(player.joint1.getMaxLength() + 1);
			if (player.joint2 != null)
				player.joint2.setMaxLength(player.joint2.getMaxLength() + 1);
		}

		else if (keycode == Keys.W) {
			if (player.joint1 != null)
				player.joint1.setMaxLength(player.joint1.getMaxLength() - 0.4f);
			if (player.joint2 != null)
				player.joint2.setMaxLength(player.joint2.getMaxLength() - 0.4f);
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

	Vector2 cameraNewPos = new Vector2();

	/**
	 * Creates a joint with a wall if there are unused ones and if the click pos
	 * is inside that wall
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		Vector3 vec = new Vector3(screenX, screenY, 0);
		camera.unproject(vec);
		for (int i = 0; i < walls.size(); i++) {
			if (walls.get(i).getBody().getFixtureList().get(0)
					.testPoint(vec.x, vec.y)) {
				cameraNewPos.lerp(new Vector2(vec.x, vec.y), +0.3f);
				if (jointOn && !jointOn2) {
					player.createJoint(2, walls.get(i), new Vector2(0, 0),
							new Vector2(vec.x, vec.y), true);

				} else if (!jointOn && jointOn2)
					player.createJoint(1, walls.get(i), new Vector2(0, 0),
							new Vector2(vec.x, vec.y), true);
				else if (!jointOn && !jointOn2)
					player.createJoint(1, walls.get(i), new Vector2(0, 0),
							new Vector2(vec.x, vec.y), true);
				player.updateAttached();
				return true;
			}
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

	/**
	 * Creates a Box2D:Body with fixture with box shape
	 */
	class Entity {
		Body body;
		Fixture fixture;
		float width;
		float height;

		/**
		 * 
		 * @param world
		 * @param x
		 * @param y
		 * @param height
		 * @param width
		 * @param bodyType
		 */
		public Entity(World world, float x, float y, float height, float width,
				BodyType bodyType) {
			BodyDef bodyDef = new BodyDef();
			// bodyDef.fixedRotation = true ;
			bodyDef.position.set(x, y);
			bodyDef.type = bodyType;
			this.width = width;
			this.height = height;
			FixtureDef fixDef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(width / 2, height / 2);
			fixDef.shape = shape;
			fixDef.density = 2;
			fixDef.restitution = 0.40f;
			fixDef.friction = 1;
			body = world.createBody(bodyDef);
			fixture = body.createFixture(fixDef);

		}

		/**
		 * @param world
		 * @param x
		 * @param y
		 * @param height
		 * @param width
		 * @param bodyType
		 * @param density
		 * @param restitution
		 * @param friction
		 */
		public Entity(World world, float x, float y, float height, float width,
				BodyType bodyType, float density, float restitution,
				float friction) {
			BodyDef bodyDef = new BodyDef();
			// bodyDef.fixedRotation = true ;
			bodyDef.position.set(x, y);
			bodyDef.type = bodyType;
			this.width = width;
			this.height = height;
			FixtureDef fixDef = new FixtureDef();
			PolygonShape shape = new PolygonShape();
			shape.setAsBox(width / 2, height / 2);
			fixDef.shape = shape;
			fixDef.density = density;
			fixDef.restitution = restitution;
			fixDef.friction = friction;
			body = world.createBody(bodyDef);
			fixture = body.createFixture(fixDef);

		}

		public Body getBody() {
			return body;
		}

		public void setBody(Body body) {
			this.body = body;
		}

		public Fixture getFixture() {
			return fixture;
		}

		public void setFixture(Fixture fixture) {
			this.fixture = fixture;
		}

		public float getX() {
			return body.getPosition().x;
		}

		public float getY() {
			return body.getPosition().y;
		}

	}

	class Wall extends Entity {

		public Wall(World world, float x, float y, float height, float width,
				BodyType type) {
			super(world, x, y, height, width, type);
			// through setting the user data to the object it self , we can
			// compare it
			// through instanceof when checking for collision for example
			this.getBody().setUserData(this);
		}

	}

	class Player extends Entity implements ContactListener {
		RopeJoint joint1;
		RopeJoint joint2;
		Entity attached1;
		Entity attached2;
		World world;

		public Player(World world, float x, float y, float height, float width,
				BodyType type) {
			super(world, x, y, height, width, type);
			this.world = world;
			this.getBody().setUserData(this);

		}

		public void destroyJoint1() {

			if (player.joint1 != null)
				world.destroyJoint(player.joint1);
			player.joint1 = null;
			jointOn = false;
			updateAttached();
		}

		public void destroyJoint2() {

			if (player.joint2 != null)
				world.destroyJoint(player.joint2);
			player.joint2 = null;
			jointOn2 = false;
			updateAttached();
		}

		public void updateAttached() {
			if (joint1 == null) {
				attached1 = null;
			}
			if (joint2 == null) {
				attached2 = null;
			}

		}

		// you can also use raytracing to set the anchor and create the joint at
		// the first point the ray encounter on the first fixture in his way
		public void createJoint(int jointNum, Entity attached, Vector2 anchorA,
				Vector2 anchorB, boolean collideConnected) {
			RopeJointDef jointDef = new RopeJointDef();
			jointDef.bodyA = this.getBody();
			jointDef.bodyB = attached.getBody();
			jointDef.collideConnected = collideConnected;
			jointDef.type = JointType.RopeJoint;

			// to set the anchor exactly in the center
			float dx = anchorB.x - attached.getX();
			float dy = anchorB.y - attached.getY();
			jointDef.localAnchorA.set(anchorA.x, anchorA.y);
			jointDef.localAnchorB.set(dx, dy);
			jointDef.maxLength = attached.getBody().getPosition()
					.dst(player.getBody().getPosition());
			if (jointNum == 1) {
				joint1 = (RopeJoint) this.world.createJoint(jointDef);
				jointOn = true;
				attached1 = attached;
			} else if (jointNum == 2) {
				joint2 = (RopeJoint) this.world.createJoint(jointDef);
				jointOn2 = true;
				attached2 = attached;

			}
			if (!isFirstJoint)
				jointCreationSound.play();

		}

		public RopeJoint getJoint1() {
			return joint1;
		}

		public void setJoint1(RopeJoint joint1) {
			this.joint1 = joint1;
		}

		public RopeJoint getJoint2() {
			return joint2;
		}

		public void setJoint2(RopeJoint joint2) {
			this.joint2 = joint2;
		}

		public Entity getAttached1() {
			return attached1;
		}

		public void setAttached1(Entity attached1) {
			this.attached1 = attached1;
		}

		public Entity getAttached2() {
			return attached2;
		}

		public void setAttached2(Entity attached2) {
			this.attached2 = attached2;
		}

		@Override
		public void beginContact(Contact contact) {
			Body a = contact.getFixtureA().getBody();
			Body b = contact.getFixtureB().getBody();
			if (a.getUserData() instanceof Player
					&& b.getUserData() instanceof Wall) {
				explode = true;
			} else if (a.getUserData() instanceof Player
					&& b.getFixtureList().get(0).isSensor()) {
				playerWon = true;
				player.getBody().setLinearVelocity(0, 0);
				player.getBody().setAngularVelocity(0);
			}

		}

		@Override
		public void endContact(Contact contact) {

		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}

	}

}
