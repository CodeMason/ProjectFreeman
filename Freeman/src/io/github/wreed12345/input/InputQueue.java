package io.github.wreed12345.input;

import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.InputProcessor;

/**Handles future click events
 * @author William Reed
 * @since 0.0.11
 */
public class InputQueue implements InputProcessor{

	private Queue<FutureClickHandler> clickQueue = new LinkedList<FutureClickHandler>();
	
	/**Queue for handling future click events. Stops the need of some classes
	 * having there own InputProcessor*/
	public static InputQueue inputQueue = new InputQueue();

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(clickQueue.size() == 0) return false;
		
		clickQueue.poll().futureClick(screenX, screenY);
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	public Queue<FutureClickHandler> getClickQueue() {
		return clickQueue;
	}

	public void setClickQueue(Queue<FutureClickHandler> clickQueue) {
		this.clickQueue = clickQueue;
	}
}
