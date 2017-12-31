package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import states.GameStateManager;
import states.MenuState;

/**
 * The main class generated by libgdx that handles rendering
 * 
 * @author Malyq McElroy
 *
 */
public class FirstGame extends ApplicationAdapter {
	// Create the batch that handles rendering
	SpriteBatch batch;
	// Set the size of the game window
	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	// Create the title of the game
	public static final String title = "Falling Up";
	// Create a GameStateManager to transfer between states
	private GameStateManager gsm;
	
	/**
	 * Auto-generated by libgdx
	 */
	@Override
	public void create () {
		// Initialize the batch
		batch = new SpriteBatch();
		// Initialize the GameStateManager
		gsm = new GameStateManager();
		// Set the background color
		Gdx.gl.glClearColor(1, 0, 0, 1);
		// Push a menu state onto the stack to begin
		gsm.push(new MenuState(gsm));
	}

	/**
	 * Auto-generated by libgdx
	 */
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	/**
	 * Clean up any disposable objects
	 */
	@Override
	public void dispose () {
		batch.dispose();
	}
}