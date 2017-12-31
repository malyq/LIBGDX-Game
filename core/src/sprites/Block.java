package sprites;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * The Block class defines what the green rectangles from the PlayState class
 * are. A single block consists of both a left and right green rectangle. The
 * block class has basic getters and setters as well as a method for
 * repositioning the blocks as they go off the screen.
 * 
 * @author Malyq McElroy
 *
 */

public class Block {
	// Offset the right rectangle from the left
	private static final int BLOCK_OFFSET = 3;
	// The distance between the left and right rectangles
	private static final int BLOCK_GAP = 30;
	// The height of each rectangle
	public static final int BLOCK_HEIGHT = 20;
	// Bounds on the random number generator
	private static final int RAND_BOUND = 135;
	// Random number generator used for the x-values of each block
	private Random rand;
	// The texture associated with a block
	private Texture block;
	// Stores the x and y values of each block
	private Vector2 posLeftBlock, posRightBlock;
	// Constructs the hidden rectangles around each part of the block; used for
	// collision detection
	private Rectangle boundsLeft, boundsRight;
	// Stores the randomly generated value
	private int randNumber;

	/**
	 * Block constructor
	 * 
	 * @param y
	 *            The vertical position of the given block
	 */
	public Block(float y) {
		// Initialize the texture
		block = new Texture("greenBar.png");
		// Initialize the random number generator
		rand = new Random();
		// Set the value of the generated number to a variable
		// We want the x position of the left block to be from -135 and 0. This allows
		// the circle to move off the screen without falling and prevents us from
		// resizing the block texture.
		randNumber = rand.nextInt(0 + 1 + RAND_BOUND) - RAND_BOUND;
		// Randomly generate the x position of the left block then set the position of
		// the right block accordingly
		posLeftBlock = new Vector2(randNumber, y);
		posRightBlock = new Vector2(posLeftBlock.x + BLOCK_GAP + block.getWidth(), y);
		// Create rectangles around each block; these will be used for collision
		// detection later
		boundsLeft = new Rectangle(posLeftBlock.x, posLeftBlock.y, block.getWidth(), block.getHeight());
		boundsRight = new Rectangle(posRightBlock.x - BLOCK_OFFSET, posRightBlock.y, block.getWidth() + BLOCK_OFFSET,
				block.getHeight());
	}

	/**
	 * Method to change the position of a block so that when they go off screen we
	 * can reposition them to the bottom without having to constantly delete and
	 * create new blocks
	 * 
	 * @param y the new y position of the block
	 */
	public void reposition(float y) {
		// Give the left block a new random x-position and the passed in y-position
		posLeftBlock.set(randNumber, y);
	    // Give the right block a new x-position based on that of y's; update its y-position
		posRightBlock.set(posLeftBlock.x + BLOCK_GAP + block.getWidth(), y);
		// Update the positions of the rectangles around each block
		boundsLeft.setPosition(posLeftBlock.x, posLeftBlock.y);
		boundsRight.setPosition(posRightBlock.x, posRightBlock.y);
	}

	/**
	 * Checks collision between the circle and the current block
	 * 
	 * @param player the rectangle around the circle
	 * @return true if the circle and block touch, false otherwise
	 */
	public boolean collides(Rectangle player) {
		return player.overlaps(boundsLeft) || player.overlaps(boundsRight);
	}


	/**
	 * Clean up any disposable objects
	 */
	public void dispose() {
		block.dispose();
	}

	/////// GETTERS ////////

	/**
	 * 
	 * @return x-position of the left half of the block
	 */
	public Vector2 getPosLeftBlock() {
		return posLeftBlock;
	}

	/**
	 * 
	 * @return x-position of the right half of the block
	 */
	public Vector2 getPosRightBlock() {
		return posRightBlock;
	}

	/**
	 * 
	 * @return x-position of the left rectangle
	 */
	public Rectangle getBoundsLeft() {
		return boundsLeft;
	}

	/**
	 * 
	 * @return x-position of the right rectangle
	 */
	public Rectangle getBoundsRight() {
		return boundsRight;
	}

	/**
	 * 
	 * @return the texture associated with the block
	 */
	public Texture getBlock() {
		return block;
	}
}
