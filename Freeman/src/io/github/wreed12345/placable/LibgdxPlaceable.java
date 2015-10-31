package io.github.wreed12345.placable;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;

import io.github.wreed12345.shared.placeable.Placeable;
import io.github.wreed12345.util.Utils;

public class LibgdxPlaceable extends Placeable implements InputProcessor{
	private static final long serialVersionUID = -6878707522881415606L;
	
	//TODO: FIGURE out visibility
	transient Texture icon;
	transient OrthographicCamera camera;
	transient Client client;
	
	public void addObjects(Texture icon, OrthographicCamera camera, Client client){
		this.icon = icon;
		this.camera = camera;
	}
	
	public void sendToServer(){
		client.sendTCP(Placeable.convertToRawPlaceable(this));
	}
	
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
		Vector2 realClickedPos = Utils.convertToMapCordinates(screenX, screenY, camera);
		
		if(Utils.clickedPosInBox((int)realClickedPos.x, (int)realClickedPos.y, 
				icon.getWidth(), icon.getHeight(), getX(), getY())){
			
			//new PlaceableDialog(this, skin, stage);
			
			return true;
		}
		
		return false;
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

}
