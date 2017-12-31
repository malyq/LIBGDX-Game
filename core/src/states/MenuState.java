package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.FirstGame;

/**
 * Game state that shows a screen for the main menu. From here the player can
 * read the instructions and start a game.
 * 
 * @author Malyq McElroy
 *
 */
public class MenuState extends State {
	// Offsets for the elements to be displayed on the menu screen
	private static final int INSTRUCTIONS_OFFSET = 55;
	private static final int BG_OFFSET = 80;
	// Create a texture for the background
	private Texture bg;
	// Create a stage for the button
	private Stage stage;
	// Create a play button
	private TextButton button;
	// Create a skin for the stage
	private Skin skin;
	// Initialize a global variable for the gsm
	final GameStateManager gsmGlobal;
	// Create a BitmapFont for displaying text
	BitmapFont instructions = new BitmapFont();
	BitmapFont instructions2 = new BitmapFont();
	BitmapFont instructions3 = new BitmapFont();
	BitmapFont instructions4 = new BitmapFont();

	/**
	 * MenuState constructor
	 * 
	 * @param gsm
	 *            allows the transfer of control to a PlayState
	 */
	public MenuState(GameStateManager gsm) {
		// Call super's constructor
		super(gsm);
		// Expand the scope of the gsm
		gsmGlobal = gsm;
		bg = new Texture("spaceBigger.png");
		// Set the position of the camera
		cam.setToOrtho(false, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Initialize the stage and corresponding skin
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		// Create a play button
		button = new TextButton("Play", skin);
		// Set the width and height of the play button
		button.setWidth(200f);
		button.setHeight(50f);
		// Set the position of the play button
		button.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);
		// Add a listener for the play button
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transfer control to a new play state
				gsmGlobal.set(new PlayState(gsmGlobal), 0);
			}
		});
		// Add the button to the stage
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(SpriteBatch sb) {
		// Establish the camera frame of reference
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		// Draw the background onto the screen
		sb.draw(bg, 0, cam.position.y - (cam.viewportWidth / 2) - BG_OFFSET, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Draw the game's instructions
		instructions.draw(sb,
				" Use the arrow keys to move and the \n space bar to jump. Your goal is to \n avoid the sun at all costs, but be \n careful... everything will begin to \n move faster! ",
				cam.viewportWidth / 4 - INSTRUCTIONS_OFFSET, cam.position.y + (cam.viewportWidth / 2));
		sb.end();
		// Draw the contents of the stage (the button)
		sb.begin();
		stage.draw();
		sb.end();

	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void dispose() {
		// Dispose excess objects
		instructions.dispose();
		stage.dispose();
		skin.dispose();
		bg.dispose();
	}

}
