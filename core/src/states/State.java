package states;
/**
 * Super class
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State {
	//Controls what the user sees
	protected OrthographicCamera cam;
	//Each sate has a GSM to transition between states
	protected GameStateManager gsm;
	
	
	/**
	 *  Constructor 
	 * @param gs The GSM used for switching states
	 */
	public State(GameStateManager gs){
		//Initialize the GSM and Camera
		gsm = gs;
		cam = new OrthographicCamera();

	}
		/**
		 * Methods to be implemented by subclasses 
		 */
		public abstract void update(float dt);
		public abstract void render(SpriteBatch sb); 
		public abstract void dispose();
	}

