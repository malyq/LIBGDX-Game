package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.FirstGame;

/**
 * Game state that shows a screen for when the user gets a game over. From here
 * the player can either retry or return to main menu.
 * 
 * @author Malyq McElroy
 *
 */
public class GameOverState extends State {
	// Offsets for the elements to be displayed on the game over screen
	private static final int BG_OFFSET = 80;
	private static final int SCORE_OFFSET = 55;
	private static final int GAMEOVER_OFFSET_X = 45;
	private static final int GAMEOVER_OFFSET_Y = 35;
	// Create a texture for the background
	private Texture bg;
	// Create a BitmapFont for displaying text
	BitmapFont losingMessage = new BitmapFont();
	BitmapFont scoreText = new BitmapFont();
	// Create a stage for handling GUI elements
	private Stage stage;
	// Create buttons for returning to the main menu and retrying
	private TextButton menuButton, playButton;
	// A skin takes a json file which defines basic operations of all the GUI
	// elements available
	private Skin skin;
	// Create a variable to expand the scope of the GameStateManager
	final GameStateManager gsmGlobal;

	/**
	 * GameOverState constructor
	 * 
	 * @param gsm
	 *            allows the transfer of control to either a PlayState or MenuState
	 */
	public GameOverState(GameStateManager gsm) {
		// Call super's constructor
		super(gsm);
		// Texture for the background
		bg = new Texture("spaceBigger.png");
		// Set the position of the camera
		cam.setToOrtho(false, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Expand the scope of the GSM
		gsmGlobal = gsm;
		// Create a new stage and skin for GUI elements
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		// Create a main menu button
		menuButton = new TextButton("Main Menu", skin);
		// Set the width and height of the button
		menuButton.setWidth(200f);
		menuButton.setHeight(50f);
		// Set the position of the button
		menuButton.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f);
		// Add a listener to the button that transfers control to a menu state
		menuButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transfer control to a new menu state
				gsmGlobal.set(new MenuState(gsmGlobal), 0);
			}
		});
		// Create a retry button
		playButton = new TextButton("Retry", skin);
		// Set the width and height of the button
		playButton.setWidth(200f);
		playButton.setHeight(50f);
		// Set the position of the button
		playButton.setPosition(Gdx.graphics.getWidth() / 2 - 100f, Gdx.graphics.getHeight() / 2 - 10f - 100);
		// Add a retry button that transfers control to a play state
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Transfer control to a new play state
				gsmGlobal.set(new PlayState(gsmGlobal), 0);
			}
		});
		// Add both buttons to the stage
		stage.addActor(menuButton);
		stage.addActor(playButton);
		Gdx.input.setInputProcessor(stage);
		;
	}

	@Override
	public void render(SpriteBatch sb) {
		// Establish the camera frame of reference
		sb.setProjectionMatrix(cam.combined);
		// Begin drawing on the batch
		sb.begin();
		// Draw the background
		sb.draw(bg, 0, cam.position.y - (cam.viewportWidth / 2) - BG_OFFSET, FirstGame.WIDTH / 2, FirstGame.HEIGHT / 2);
		// Draw the losing message on the screen and color it red
		losingMessage.draw(sb, "GAME OVER", cam.viewportWidth / 2 - GAMEOVER_OFFSET_X,
				cam.position.y - (cam.viewportWidth / 2) - GAMEOVER_OFFSET_Y + (cam.viewportHeight / 2));
		losingMessage.setColor(Color.RED);
		// Draw the player's score on the screen
		scoreText.draw(sb, "Your Score: " + String.format("%.2f", gsmGlobal.scoreVar),
				cam.viewportWidth / 2 - SCORE_OFFSET, cam.position.y - (cam.viewportWidth / 2));
		// End drawing on the batch
		sb.end();
		// Draw the contents of the stage (the buttons)
		sb.begin();
		stage.draw();
		sb.end();
	}

	@Override
	public void update(float dt) {
	}

	@Override
	public void dispose() {
		// Dispose of all excess objects
		bg.dispose();
		skin.dispose();
		losingMessage.dispose();
		scoreText.dispose();
		stage.dispose();
	}

}
