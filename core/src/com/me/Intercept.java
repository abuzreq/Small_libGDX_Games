package com.me;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
/**
 * Source :http://www.gamedev.net/page/resources/_/technical/math-and-physics/intercepting-a-moving-target-in-2d-r3884
 * 
 * TODO would be more useful if you can refactor this into a method that works in the environment of BOX2D
 * @implementor Abuzreq
 *
 */
public class Intercept implements Screen, InputProcessor {

	World world ; 
	Box2DDebugRenderer  renderer ;
	OrthographicCamera camera;
	
	Body ship;
	Fixture shipFixture;
	
	Body bullet;
	Fixture bulletFixture;

	ShapeRenderer rend = new ShapeRenderer();
	
	
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 15;
	private final int POSITIONITERATION = 11;
			
	MyGdxGame game;
	public Intercept(MyGdxGame game)
	{
		this.game = game;
	}
	
	Vector2 Ps = new Vector2();
	Vector2 Pbf = new Vector2();
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		rend.setColor(Color.RED);
		rend.setProjectionMatrix(camera.combined);
		rend.begin(ShapeType.Line);
		rend.line(Ps, Pbf);
		rend.end();
	
	
	
	//	System.out.println(bullet.getLinearVelocity());
		
		renderer.render(world, camera.combined);
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);
		
		
	}

	@Override
	public void resize(int width, int height) {
	

	}
	float cos ;

	@Override
	public void show() {
		
		camera = new OrthographicCamera(Gdx.graphics.getWidth() /15,
				Gdx.graphics.getHeight() / 15) ;
		world = new World(new Vector2(0,0),false);
		renderer = new Box2DDebugRenderer();

		
		//Body Creation
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(-25,-5);
		bodyDef.fixedRotation = true;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 250;
		fixtureDef.friction = 0.8f;
		fixtureDef.restitution = 0.2f;

		
		CircleShape circle = new CircleShape();
		circle.setRadius(0.6f);

		fixtureDef.shape = circle;
		ship = world.createBody(bodyDef);
		shipFixture = ship.createFixture(fixtureDef);
		
		ship.getPosition().set(0, 0);
		ship.setLinearVelocity(7,0);
		
		// bullet creation	
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(10,10);
		bodyDef.fixedRotation = true;
		
	
		fixtureDef.density = 1000f;
		fixtureDef.friction = 0.8f;
		fixtureDef.restitution = 0.2f;

		
		circle.setRadius(0.2f);

		fixtureDef.shape = circle;
		bullet = world.createBody(bodyDef);
		bulletFixture = bullet.createFixture(fixtureDef);
		Gdx.input.setInputProcessor(this);
		
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

		if(keycode == Keys.SPACE)
		{
			fireBullet();
		}
		return false;
	}

	private void fireBullet() {
		Vector2 Pbi =  ship.getPosition();
		 Ps = bullet.getPosition();
		Vector2 Vb = ship.getLinearVelocity();
		float bulletSpeed = 10 ;
		Vector2 R = new Vector2(Pbi.x - Ps.x ,Pbi.y-Ps.y);
		System.out.println(Pbi);
		System.out.println(Ps);
		System.out.println(R);
	
		float a =  (float) (Math.pow(Vb.x,2) + Math.pow(Vb.y,2) -Math.pow(bulletSpeed,2)) ;
		float b = 2*(R.x * Vb.x +R.y*Vb.y) ;
		float c = (float) (Math.pow(R.x,2) +Math.pow(R.y,2));
		System.out.println(a +" : "+b+" : "+c +"");
		
		
		float t1   = (float)((-b  +Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a)); 
	//	System.out.println(t1);
		float t2   = (float)((-b  -Math.sqrt(Math.pow(b, 2)-4*a*c))/(2*a)); 
	//	System.out.println(t2);
		
		float t =Math.min(t1, t2);
		
		 Pbf  = Pbi.add(Vb.scl(t2));
		Vector2 Vbs = new Vector2(Pbf.x - Ps.x,Pbf.y-Ps.y);
		System.out.println(Pbf);
		Vector2 dir = Vbs.nor();
		bullet.setLinearVelocity(bulletSpeed * dir.x , bulletSpeed *dir.y);
		
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
		 
	
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// 
		return false;
	}


}
