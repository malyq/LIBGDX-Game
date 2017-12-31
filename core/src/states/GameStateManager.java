package states;

import java.util.Stack;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class GameStateManager {
	public Stack<State> states;
    public float scoreVar;
	public GameStateManager() {
		states = new Stack<State>();
	}

	public void push(State sta) {
		states.push(sta);
	}

	public void pop() {
		states.pop();
	}

	public void set(State sta,float score) {
		scoreVar = score;
		states.pop();
		states.push(sta);
	}

	public void render(SpriteBatch sb) {
		states.peek().render(sb);
	}

	public void update(float dt) {
		states.peek().update(dt);

	}
}
