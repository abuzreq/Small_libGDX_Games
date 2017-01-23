package com.me;

import com.badlogic.gdx.Game;
import com.me.tetris.Tetris;

public class MyGdxGame extends Game {

	AttachIt attachIt ;
	Intercept intercept;
	FloodIt floodIt;
	MissileCommand missileCommand;//is found in separate project for exporting purposes
	Simon simon;
	Squareboly squareBoly ;
	NoisyDrawing noisyDrawing ;//this is implemented in Processsing not here
	Tetris tetris;
	
	Seek seek ;
	LetItRain rain ;
	Filters filter ;

	@Override
	public void create() {
		attachIt = new 	AttachIt(this);
		intercept = new Intercept(this);
		floodIt =  new FloodIt(this);
		
		missileCommand = new MissileCommand(this);
		tetris = new Tetris(this);// 520*760
		rain = new LetItRain(this);
		
		simon = new Simon(this);
		squareBoly = new Squareboly(this);//Not working
		seek = new Seek(this);		
		filter = new Filters(this);
		noisyDrawing =  new NoisyDrawing(this);
		setScreen(attachIt);
		
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
