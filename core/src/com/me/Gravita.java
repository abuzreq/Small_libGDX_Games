package com.me;
// you control a ship and able to move stars around you to control the direction of the ship by changing the forces acting on it 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;

public class Gravita implements Screen, InputProcessor {

	World world ; 
	Box2DDebugRenderer  renderer ;
	OrthographicCamera camera;
	Body ship;
	Fixture shipFixture;
	
	Body star;
	Fixture starFixture;

	float starGravitation;
	
	
	private final float TIMESTEP = 1 / 60f;
	private final int VELOCITYITERATIONS = 15;
	private final int POSITIONITERATION = 11;
			
	MyGdxGame game;
	public Gravita(MyGdxGame game)
	{
		this.game = game;
	}
	
boolean firstTime = true ;
	@Override
	public void render(float delta) {
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
	
		
	
	//	 cos = (ship.getTransform().getPosition().dot(star.getTransform().getPosition()))/(ship.getPosition().len()*star.getPosition().len());
		starGravitation = (float) (  0.0677f*(star.getMass()*ship.getMass())/Math.pow(star.getPosition().dst(ship.getPosition()),2));
		System.out.println(star.getLinearVelocity());
	
		ship.applyForce(starGravitation*getSideX(ship.getPosition(),star.getPosition()), starGravitation*getSideY(ship.getPosition(),star.getPosition()), ship.getWorldCenter().x, ship.getWorldCenter().y, true);
	
		renderer.render(world, camera.combined);
		world.step(TIMESTEP, VELOCITYITERATIONS, POSITIONITERATION);
	}
	/**@return -1 if b is at the left of a , 1 if at right**/
	public int getSideX(Vector2 a , Vector2 b)
	{
		if(a.x  > b.x )return -1;
		else return 1 ;
	}
	/**@return -1 if b is below a , 1 if above**/
	public int getSideY(Vector2 a , Vector2 b)
	{
		if(a.y > b.y )return -1;
		else return 1 ;
	}
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}
	float cos ;

	@Override
	public void show() {
		camera = new OrthographicCamera(Gdx.graphics.getWidth() /25,
				Gdx.graphics.getHeight() / 25) ;
		world = new World(new Vector2(0,0),false);
		renderer = new Box2DDebugRenderer();
		
		BodyDef bodyDef = new BodyDef();

		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		bodyDef.fixedRotation = true;

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 250;
		fixtureDef.friction = 0.8f;
		fixtureDef.restitution = 0.2f;

		
		CircleShape circle = new CircleShape();
		circle.setRadius(1f);

		fixtureDef.shape = circle;
		ship = world.createBody(bodyDef);
		shipFixture = ship.createFixture(fixtureDef);
		
		ship.getPosition().set(0, 0);

		
		
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(5,5);
		bodyDef.fixedRotation = true;
		
	
		fixtureDef.density = 1000f;
		fixtureDef.friction = 0.8f;
		fixtureDef.restitution = 0.2f;

		
		circle.setRadius(1.5f);

		fixtureDef.shape = circle;
		star = world.createBody(bodyDef);
		starFixture = star.createFixture(fixtureDef);
		
		star.getPosition().set(150, 150);
		starGravitation = (float) ( 0.0677f * (star.getMass()*ship.getMass())/Math.pow(star.getPosition().dst(ship.getPosition()),2));
	//	cos = (ship.getPosition().dot(star.getPosition()))/(ship.getPosition().len()*star.getPosition().len());
		
		Gdx.input.setInputProcessor(this);
		
	}
	
	 QueryCallback queryCallback=  new QueryCallback()
	 {

		@Override
		public boolean reportFixture(Fixture fixture) {
		
			
			return false;
		}
		 
		 
	 };

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

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

	
	Vector3 vec = new Vector3(); ;
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		 vec = new Vector3(screenX,screenY,0);
		camera.unproject(vec);
		if(star.getFixtureList().get(0).testPoint(vec.x, vec.y))
			star.getLinearVelocity().set(0, 0);
	//	world.QueryAABB(queryCallback, vec.x, vec.y, vec.x, vec.y);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		 vec = new Vector3(screenX,screenY,0);
		 camera.unproject(vec);
		 
			if(star.getFixtureList().get(0).testPoint(vec.x, vec.y)){
//				star.
		 star.setTransform(vec.x, vec.y, star.getAngle());
		star.getPosition().set(vec.x,vec.y);
		star.setLinearVelocity(0, 0);
			ship.setLinearVelocity(0, 0);
	
		}
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}
