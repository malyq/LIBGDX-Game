package sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * The Circle class defines what the red ball from the PlayState is. It has
 * methods to manipulate the circle such as horizontal movement, updating and
 * setting the position, and collision detection.
 * 
 * @author Malyq McElroy
 *
 */
public class Circle {
	// Rate at which the circle falls (units/delta time)
	private static final int GRAVITY = 42;
	// Offset used for the rectangles around the circle, later used for
	// collision detection
	private static final int RECTANGLE_OFFSET = 8;
	// Integer values corresponding to which of the circle's sides hit something
	// else
	private static final int BOTTOM_HIT = 1;
	private static final int LEFT_HIT = 2;
	private static final int RIGHT_HIT = 3;
	private static final int TOP_HIT = 4;
	// Used to forbid the circle from double jumping
	public boolean jumping = false;
	// Create bounds around all sides of the circle for collision detection
	public Rectangle circleRect, bottom, left, right, top;
	// Texture that represents a circle
	private Texture circle;
	// Integer values to determine the position and speed of the circle
	public Vector3 position, velocity;
	// Sprite that defines a circle
	public Sprite sprite;
	// Audio for when the ball jumps
	private Sound jumpSound;
	// Speed in which the ball moves when the arrow keys are pressed (will later
	// be scaled by delta)
	private int horizontalSpeed = 95;
	// The distance in which the circle jumps in the y direction
	private int verticalJump = 7;

	/**
	 * Circle constructor
	 * 
	 * @param x
	 *            the x coordinate of the circle's starting position
	 * @param y
	 *            the y coordinate of the circle's starting position
	 */
	public Circle(int x, int y) {
		// Initialize the texture
		circle = new Texture("redCircle.png");
		// Set the starting position of the circle to the parameters of the
		// constructor
		position = new Vector3(x, y, 0);
		// The circle begins with no initial speed
		velocity = new Vector3(0, 0, 0);
		// The rectangle around the entire circle object
		circleRect = new Rectangle(position.x, position.y, circle.getWidth(), circle.getHeight());
		// Rectangle at the bottom of the circle
		bottom = new Rectangle(position.x, position.y, circle.getWidth(), RECTANGLE_OFFSET);
		// Rectangle at the top of the circle
		top = new Rectangle(position.x, position.y + circleRect.getHeight() - RECTANGLE_OFFSET, circle.getWidth(),
				RECTANGLE_OFFSET);
		// Rectangle at the left of the circle
		left = new Rectangle(position.x, position.y + RECTANGLE_OFFSET, RECTANGLE_OFFSET,
				circleRect.getHeight() - (RECTANGLE_OFFSET * 2));
		// Rectangle at the right of the circle
		right = new Rectangle(position.x + circleRect.getWidth() - RECTANGLE_OFFSET, position.y + RECTANGLE_OFFSET,
				RECTANGLE_OFFSET, circleRect.getHeight() - (RECTANGLE_OFFSET * 2));
		// Initialize the sprite
		sprite = new Sprite(circle);
		// Set the initial position of the circle
		this.setPosition(position.x, position.y);
		// Initalize the audio
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("jumpSound.ogg"));
	}

	/**
	 * Because the update method is repeatedly called it allows the circle to always
	 * be falling unless it hits a block, though that is handled elsewhere.
	 * 
	 * @param dt
	 *            Short for delta time, it allows the speed to scale off of the
	 *            user's frames
	 */
	public void update(float dt) {
		// The circle is constantly falling in proportion to gravity
		velocity.y -= GRAVITY * dt;
		// Because the circle is falling, we need to adjust the position of all
		// the rectangles so the collision detection is accurate
		bottom.y += velocity.y;
		circleRect.y += velocity.y;
		top.y += velocity.y;
		left.y += velocity.y;
		right.y += velocity.y;
		// Update the position of the sprite
		sprite.setPosition(circleRect.x, circleRect.y);
	}

	/**
	 * Called when the left arrow key is pressed. Adjusts the X position of all the
	 * rectangles and the sprite
	 * 
	 * @param dt
	 *            Short for delta time, it allows the speed to scale off of the
	 *            user's frames
	 */
	public void moveLeft(float dt) {
		// Move all the rectangle components to the left based on the speed
		circleRect.x -= (horizontalSpeed * dt);
		bottom.x -= (horizontalSpeed * dt);
		top.x -= (horizontalSpeed * dt);
		left.x -= (horizontalSpeed * dt);
		right.x -= (horizontalSpeed * dt);
		// Update the position of the sprite
		sprite.setPosition(circleRect.x, circleRect.y);
	}

	/**
	 * Called when the right arrow key is pressed. Adjusts the X position of all the
	 * rectangles and the sprite
	 * 
	 * @param dt
	 *            Short for delta time, it allows the speed to scale off of the
	 *            user's frames
	 */
	public void moveRight(float dt) {
		// Move all the rectangle components to the right based on the speed
		circleRect.x += (horizontalSpeed * dt);
		bottom.x += (horizontalSpeed * dt);
		top.x += (horizontalSpeed * dt);
		left.x += (horizontalSpeed * dt);
		right.x += (horizontalSpeed * dt);
		// Update the position of the sprite
		sprite.setPosition(circleRect.x, circleRect.y);
	}

	/**
	 * Sets the position of the circle by setting the positions of all the rectangle
	 * components
	 * 
	 * @param x
	 *            the new x position
	 * @param y
	 *            the new y position
	 */
	public void setPosition(float x, float y) {
		// Update the large rectangle and the bottom rectangle
		circleRect.x = x;
		circleRect.y = y;
		bottom.x = x;
		bottom.y = y;
		// The left, right, and top rectangles all must be drawn with the same
		// offsets they were initialized with
		left.x = x;
		left.y = y + RECTANGLE_OFFSET;
		right.x = x + circleRect.getWidth() - RECTANGLE_OFFSET;
		right.y = y + RECTANGLE_OFFSET;
		top.x = x;
		top.y = y + circle.getHeight() - RECTANGLE_OFFSET;
		// Update the position of the sprite
		sprite.setPosition(x, y);
	}

	/**
	 * Method for collision detection. Returns an int based on which rectangle
	 * collided
	 * 
	 * @param r
	 *            the rectangle that collided with one of the circle's rectangles
	 * 
	 * @return an integer value depending on which rectangle collided. Returns -1 if
	 *         nothing collided
	 */
	public int hits(Rectangle r) {
		// Note: If we check for the bottom first then we will never execute the
		// other checks.

		// Check if the left rectangle collided
		if (r.overlaps(left)) {
			return LEFT_HIT;
		}
		// Check if the right rectangle collided
		else if (r.overlaps(right)) {
			return RIGHT_HIT;
		}
		// Check if the bottom rectangle collided
		else if (r.overlaps(bottom)) {
			return BOTTOM_HIT;
		}
		// Check if the top rectangle collided
		else if (r.overlaps(top)) {
			return TOP_HIT;
		}
		// No rectangles collided, return -1
		return -1;
	}

	/**
	 * Method that allows the circle to jump
	 */
	public void jump() {
		// In order to prevent double jumping, we first must make sure the
		// circle isn't already jumping
		if (jumping == false) {
			// Update the y velocity
			velocity.y = verticalJump;
			// Play the jumping sound at max volume
			jumpSound.play(1f);
			// The circle is now jumping
			jumping = true;
		}
	}

	/**
	 * Method the either adjusts the x or y position depending on what part of the
	 * circle collided
	 * 
	 * @param type
	 *            an integer value that corresponds to which rectangle collided
	 * @param x
	 *            the new x position of the circle
	 * @param y
	 *            the new y position of the circle
	 */
	public void action(int type, float x, float y) {
		// If the top or bottom collide
		if (type == BOTTOM_HIT || type == TOP_HIT) {
			// Reset the y velocity, essentially making it stop falling
			velocity.y = 0;
			// Update the position with the new y value and the same x value
			setPosition(circleRect.x, y);
			// Once the circle's bottom touches the block's surface, it is
			// allowed to jump again
			if (type == BOTTOM_HIT) {
				jumping = false;
			}
		}
		// If the left or right collide
		if (type == LEFT_HIT || type == RIGHT_HIT) {

			// Reset the y velocity, essentially making it stop falling
			/////// velocity.y = 0;

			// Update the position with the new x value and the same y value
			setPosition(x, circleRect.y);
		}
	}

	/**
	 * Basic method for drawing the sprite
	 * 
	 * @param sb
	 *            the SpriteBatch needed to draw the circle sprite
	 */
	public void draw(SpriteBatch sb) {
		sprite.draw(sb);
	}

	/**
	 * Clean up any disposable objects
	 */
	public void dispose() {
		jumpSound.dispose();
		circle.dispose();
	}

	/////// GETTERS ////////

	/**
	 * 
	 * @return the texture
	 */
	public Texture getCircle() {
		return circle;
	}

	/**
	 * 
	 * @return the position vector
	 */
	public Vector3 getPosition() {
		return position;
	}

	/**
	 * 
	 * @return the velocity vector
	 */
	public Vector3 getVelocity() {
		return velocity;
	}

	/**
	 * 
	 * @return the rectangle around the entire circle
	 */
	public Rectangle getCircleRect() {
		return circleRect;
	}
}
