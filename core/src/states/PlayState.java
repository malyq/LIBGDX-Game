package states;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.FirstGame;

import sprites.Block;
import sprites.Circle;

/**
 * Game state that shows a screen for when the user is actually playing the
 * game. Most of the action happens in this class as well as some collision
 * detection and the inevitable transfer to a game over state. The movement of
 * the screen is simulated with an orthographic camera and the constant
 * rendering of the background at the camera's location.
 * 
 * @author Malyq McElroy
 *
 */
public class PlayState extends State {
	// Offset for redrawing the sun
	private static final int SUN_OFFSET = 225;
	// Offset for redrawing the dashes
	private static final int DASHES_OFFSETY = 135;
	// Position of the score text
	private static final int SCORE_POS = 150;
	// Bounds for the right side of the screen
	private static final int RIGHT_BOUNDS = 240;
	// Left and Right offset for collision detection
	private static final int LR_OFFSETY = 5;
	// Offset in the y direction for various elements
	private static final int OFFSETY = 80;
	// Offset in the x direction for the rectangle at the top of the screen
	private static final int DASH_RECT_OFFSETX = 135;
	// Max amount of blocks on the screen at one time
	private static final int MAX_BLOCKS = 7;
	// Spacing between blocks
	private static final int BLOCK_SPACING = 55;
	// Starting position for the circle
	private static final int CIRCLE_POS = 205;
	// Speed at which the camera gradually increases
	private double scaleSpeed = .5;
	// Textures for the background, sun, and dashes
	private Texture bg, sun, dashes;
	// The circle the user controls
	private Circle circle;
	// Holds all of the blocks for reference
	private Array<Block> blocks;
	// Text that displays the user's current score
	BitmapFont scoreText = new BitmapFont();
	// Rectangle for collision detection with the top of the screen
	private Rectangle dashRect;
	// Displays the numerical value of the score
	public float score = 0;
	// Basically a boolean value to stop the speed of the camera from scaling out of
	// control
	private int speedStopper = 0;
	// Sound that plays when the user gets a game over
	private Sound gameOver;
	// Ambient background music
	private Music bgMusic;
	// Gets the hundredths place of the score
	private float seconds = 0;

	/**
	 * PlayState Constructor
	 * 
	 * @param gsm
	 *            Allows the transfer of control to a GameOverState
	 */
	public PlayState(GameStateManager gsm) {
		// Calls the super's constructor
		super(gsm);
		// Initialize the background
		bg = new Texture("spaceBigger.png");
		// Initialize the sun
		sun = new Texture("newSun.png");
		// Initialize the dashes at the top of the screen
		dashes = new Texture("dashes.png");
		// Initialize the camera position
		cam.setToOrtho(false, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Create an array of blocks
		blocks = new Array<Block>();
		// Add the blocks to the array with positions that are equally spaced as
		// determined by i
		for (int i = 0; i <= MAX_BLOCKS; i++) {
			blocks.add(new Block(i * (Block.BLOCK_HEIGHT + BLOCK_SPACING)));
		}
		// Create the circle
		circle = new Circle(CIRCLE_POS, CIRCLE_POS);
		// Create the rectangle at the top of the screen for testing if a game over has
		// occurred
		dashRect = new Rectangle(0, cam.position.y - (cam.viewportHeight / 2) + DASH_RECT_OFFSETX + dashes.getHeight(),
				FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Initialize the background music
		bgMusic = Gdx.audio.newMusic(Gdx.files.internal("bgMusic.ogg"));
		// Initialize the game over sound
		gameOver = Gdx.audio.newSound(Gdx.files.internal("gameOver.ogg"));
		// Have the background music loop when it ends
		bgMusic.setLooping(true);
		// Set the volume
		bgMusic.setVolume(.05f);
		// Begin playing the background music
		bgMusic.play();
	}

	@Override
	public void update(float dt) {
		// Adjust the camera's position based on it's speed
		cam.position.y -= scaleSpeed;
		// Adjust the rectangle of the dashes based on the camera's position. If we
		// don't update its' position, the collision detection will fail.
		dashRect.y -= scaleSpeed;
		// Whenever there is a 9 in the hundredths place increase the speed of the
		// camera
		if ((int) seconds == 9 && speedStopper == 0) {
			// Because update is constantly called if we don't prevent the entrance of this
			// if-conditional after the first speed increase, then it will continue to
			// increase out of control.
			speedStopper = 1;
			scaleSpeed += .1;
		}
		// Now that we've stopped the scaling, we can reset the stopper so we can scale
		// again next time we get a 9 in the hundredths
		if ((int) seconds == 1) {
			speedStopper = 0;
		}
		// Loop through all the blocks
		for (Block block : blocks) {
			// Check to see if a block has gone past the dashes and by our definition
			// "off-screen"
			if (cam.position.y + (cam.viewportHeight / 2) < block.getPosLeftBlock().y
					+ block.getBoundsLeft().getHeight()) {
				// If a block has gone off-screen, then instead of creating a new block at the
				// bottom of the screen, we can just reposition the old one.
				block.reposition(
						block.getPosLeftBlock().y - ((block.getBlock().getHeight() + BLOCK_SPACING) * MAX_BLOCKS - 2));
			}
			// Check to see if a block has gone past the dashes
			if (circle.getCircleRect().y + circle.getCircleRect().height > dashRect.y + OFFSETY) {
				// Stop playing the background music
				bgMusic.stop();
				// Play the game over sound
				gameOver.play();
				// Enter a new GameOverState
				gsm.set(new GameOverState(gsm), score);
			}
		}
		// Update the camera
		cam.update();
	}

	@Override
	public void render(SpriteBatch sb) {
		// Establish the frame of reference for the camera
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		// Draw the background to the screen
		sb.draw(bg, 0, cam.position.y - (cam.viewportWidth / 2) - OFFSETY, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Draw the sun to the screen
		sb.draw(sun, 0, cam.position.y - (cam.viewportWidth / 2) + SUN_OFFSET, FirstGame.WIDTH / 2,
				FirstGame.HEIGHT / 2);
		// Draw the dashes to the screen
		sb.draw(dashes, 0, cam.position.y - (cam.viewportHeight / 2) + DASHES_OFFSETY, FirstGame.WIDTH / 2,
				FirstGame.HEIGHT / 2);
		// Draw the score to the screen
		scoreText.draw(sb, "Score =  " + score, SCORE_POS, cam.position.y + (cam.viewportWidth / 2) + OFFSETY);
		// Update the value of the score
		score += Gdx.graphics.getDeltaTime();
		scoreText.setUseIntegerPositions(false);
		// Store the hundredths place into a variable
		seconds = score % 10;
		// Draw the blocks to the screen
		for (Block block : blocks) {
			sb.draw(block.getBlock(), block.getPosLeftBlock().x, block.getPosLeftBlock().y);
			sb.draw(block.getBlock(), block.getPosRightBlock().x, block.getPosRightBlock().y);
		}
		// Check if the circle goes off the right side of the screen
		if (circle.getCircleRect().x > RIGHT_BOUNDS) {
			circle.setPosition(-circle.getCircleRect().width, circle.getCircleRect().y);
			// Check if the circle goes off the left side of the screen
		} else if (circle.getCircleRect().x + circle.getCircleRect().width < 0) {
			circle.setPosition(RIGHT_BOUNDS, circle.getCircleRect().y);
			// Check if the circle goes off the bottom of the screen
		} else if (circle.getCircleRect().y < cam.position.y - 200) {
			circle.setPosition(circle.getCircleRect().x, cam.position.y - 200);
		}
		// Draw the circle
		circle.draw(sb);
		sb.end();

		/////////// Collision Detection ///////////

		// Update the position of the circle
		circle.update(Gdx.graphics.getDeltaTime());
		for (Block block : blocks) {
			// Check if each left part of the block is overlapping with the circle
			switch (circle.hits(block.getBoundsLeft())) {
			// If the bottom side of the circle overlaps enter this case
			case 1:
				// Prevent the circle from falling
				circle.action(1, 0, block.getBoundsLeft().y + block.getBoundsLeft().height - LR_OFFSETY);
				break;
			// If the left side of the circle overlaps enter this case
			case 2:
				// Prevent the circle from moving left
				circle.action(2, block.getBoundsLeft().x + block.getBoundsLeft().width, 0);
				break;
			// If the top side of the circle overlaps enter this case
			case 4:
				// Prevent the circle from going up through other blocks
				circle.action(4, 0, block.getBoundsLeft().y - circle.getCircleRect().height);
				break;
			}
			// Check if each right part of the block is overlapping with the circle
			switch (circle.hits(block.getBoundsRight())) {
			// If the bottom side of the circle overlaps enter this case
			case 1:
				// Prevent the circle from falling 
				circle.action(1, 0, block.getBoundsRight().y + block.getBoundsRight().height - LR_OFFSETY);
				break;
		    // If the right side of the circle overlaps enter this case
			case 3:
				// Prevent the block from moving right
				circle.action(3, block.getBoundsRight().x - 10, 0);
				break;
		   // If the top side of the circle overlaps enter this case
			case 4:
				// Prevent the circle from going up through other blocks
				circle.action(4, 0, block.getBoundsRight().y - circle.getCircleRect().height);
				break;
			}
		}
		// Check if the left arrow was pushed
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			// Move the circle left
			circle.moveLeft(Gdx.graphics.getDeltaTime());
		}
       // Check if the right arrow was pushed
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			// Move the circle right
			circle.moveRight(Gdx.graphics.getDeltaTime());
		}
		// Check if the space bar was pushed
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			// Make the circle jump
			circle.jump();
		}
	}

	@Override
	public void dispose() {
		// Dispose of excess objects to avoid memory leaks
		circle.dispose();
		bgMusic.dispose();
		gameOver.dispose();
		bg.dispose();
		sun.dispose();
		dashes.dispose();
		scoreText.dispose();
		for (Block block : blocks) {
			block.dispose();
		}
	}

}
