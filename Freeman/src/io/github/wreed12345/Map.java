package io.github.wreed12345;

import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.countries.Country;
import io.github.wreed12345.ui.CountryDialog;
import io.github.wreed12345.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Creates a new background map
 * @author William Reed
 */
public class Map implements InputProcessor {
	private Texture primaryMap;
	private Pixmap colorMap;
	private OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private io.github.wreed12345.shared.Game playableGame;

	/**
	 * Creates a new map instance
	 * @param primaryMap The map to be drawn on top (the nice looking one)
	 * @param colorMap the color map (ugly looking one)
	 * @param camera the orthographic camera being used.
	 * @param stage Stage in use
	 * @param skin skin in use
	 */
	public Map(Texture primaryMap, Pixmap colorMap, OrthographicCamera camera, Stage stage, Skin skin, io.github.wreed12345.shared.Game playableGame) {
		this.primaryMap = primaryMap;
		this.colorMap = colorMap;
		this.camera = camera;
		this.stage = stage;
		this.skin = skin;
		this.playableGame = playableGame;

		boolean widthMatches = false, heightMatches = false;

		if (primaryMap.getWidth() == colorMap.getWidth())
			widthMatches = true;
		if (primaryMap.getHeight() == primaryMap.getHeight())
			heightMatches = true;

		if (!widthMatches || !heightMatches) {
			System.err.println("The sizes of the primaryMap and colorMap do not match!");
			System.exit(1);// known error so exit with positive number
		}

	}

	/**
	 * Draws the map at the specified location
	 * 
	 * @param batch
	 *            SpriteBatch used to draw the map.
	 */
	public void draw(SpriteBatch batch) {
		batch.draw(primaryMap, 0, 0);
		// TODO: maybe draw map in the middle? who knows.
		// if this is done only move camera
	}

	/** Speed to move the map */
	private final int MOVE_SPEED = 200;
	/** Speed to zoom the map */
	private final int ZOOM_SPEED = 1;
	/** Boolean used to determine moving momentum */
	private boolean upMomentum, downMomentum, leftMomentum, rightMomentum;
	/** Time variable used in movement velocity formula */
	private float moveTime = 0;
	/** Boolean used to determine zooming momentum */
	private boolean inMomentum, outMomentum;
	/** Time variables used in zooming velocity formula */
	private float zoomTime;

	/**
	 * Handles input for moving the map & zooming in and out.
	 */
	// TODO:clean up
	public void input() {
		if (upMomentum || downMomentum || leftMomentum || rightMomentum)
			moveTime += Gdx.graphics.getDeltaTime();

		if (inMomentum || outMomentum)
			zoomTime += Gdx.graphics.getDeltaTime();

		if (stage.getKeyboardFocus() != null)
			return;
		// move up
		if (Gdx.input.isKeyPressed(Keys.W)) {
			float virtualHeight = Gdx.graphics.getHeight() * camera.zoom;
			if ((camera.position.y + (MOVE_SPEED * Gdx.graphics.getDeltaTime()) + Gdx.graphics.getHeight()
					/ 2 - ((Gdx.graphics.getHeight() - virtualHeight) / 2)) > primaryMap.getHeight()) {
				upMomentum = false;
				return;
			}

			camera.position.y += MOVE_SPEED * Gdx.graphics.getDeltaTime();

			upMomentum = true;
			downMomentum = false;
			leftMomentum = false;
			rightMomentum = false;

			moveTime = 0;
			// move left
		} else if (Gdx.input.isKeyPressed(Keys.A)) {
			float virtualWidth = Gdx.graphics.getWidth() * camera.zoom;
			if (((camera.position.x - MOVE_SPEED * Gdx.graphics.getDeltaTime()) - Gdx.graphics.getWidth() / 2 + ((Gdx.graphics
					.getWidth() - virtualWidth) / 2)) < 0) {

				leftMomentum = false;
				return;
			}

			camera.position.x -= MOVE_SPEED * Gdx.graphics.getDeltaTime();

			upMomentum = false;
			downMomentum = false;
			leftMomentum = true;
			rightMomentum = false;

			moveTime = 0;
			// move down
		} else if (Gdx.input.isKeyPressed(Keys.S)) {
			float virtualHeight = Gdx.graphics.getHeight() * camera.zoom;
			if ((camera.position.y - (MOVE_SPEED * Gdx.graphics.getDeltaTime()) - Gdx.graphics.getHeight()
					/ 2 + ((Gdx.graphics.getHeight() - virtualHeight) / 2)) < 0) {
				downMomentum = false;
				return;
			}

			camera.position.y -= MOVE_SPEED * Gdx.graphics.getDeltaTime();

			upMomentum = false;
			downMomentum = true;
			leftMomentum = false;
			rightMomentum = false;

			moveTime = 0;
			// move right
		} else if (Gdx.input.isKeyPressed(Keys.D)) {
			float virtualWidth = Gdx.graphics.getWidth() * camera.zoom;
			if (((camera.position.x + MOVE_SPEED * Gdx.graphics.getDeltaTime()) + Gdx.graphics.getWidth() / 2 - ((Gdx.graphics
					.getWidth() - virtualWidth) / 2)) > primaryMap.getWidth()) {
				rightMomentum = false;
				return;
			}

			camera.position.x += MOVE_SPEED * Gdx.graphics.getDeltaTime();

			upMomentum = false;
			downMomentum = false;
			leftMomentum = false;
			rightMomentum = true;

			moveTime = 0;
		} else if (upMomentum) {
			if (changeValue(moveTime, MOVE_SPEED) < .1)
				upMomentum = false;
			camera.position.y += changeValue(moveTime, MOVE_SPEED);
		} else if (downMomentum) {
			if (changeValue(moveTime, MOVE_SPEED) < .1)
				downMomentum = false;
			camera.position.y -= changeValue(moveTime, MOVE_SPEED);
		} else if (leftMomentum) {
			if (changeValue(moveTime, MOVE_SPEED) < .1)
				leftMomentum = false;
			camera.position.x -= changeValue(moveTime, MOVE_SPEED);
		} else if (rightMomentum) {
			if (changeValue(moveTime, MOVE_SPEED) < .1)
				rightMomentum = false;
			camera.position.x += changeValue(moveTime, MOVE_SPEED);
		}

		// zoom in
		if (Gdx.input.isKeyPressed(Keys.I)) {
			if (camera.zoom - ZOOM_SPEED * Gdx.graphics.getDeltaTime() < .2) {
				inMomentum = false;
				return;
			}
			camera.zoom -= ZOOM_SPEED * Gdx.graphics.getDeltaTime();
			zoomTime = 0;
			outMomentum = false;
			inMomentum = true;
			// zoom out
		} else if (Gdx.input.isKeyPressed(Keys.O)) {
			camera.zoom += ZOOM_SPEED * Gdx.graphics.getDeltaTime();
			zoomTime = 0;
			outMomentum = true;
			inMomentum = false;
		} else if (inMomentum) {
			if (changeValue(zoomTime, ZOOM_SPEED) < .1)
				inMomentum = false;
			camera.zoom -= changeValue(zoomTime, ZOOM_SPEED);
		} else if (outMomentum) {
			if (changeValue(zoomTime, ZOOM_SPEED) < .1)
				outMomentum = false;
			camera.zoom += changeValue(zoomTime, ZOOM_SPEED);
		}
	}

	/**
	 * Calculates the move value to move the map at.
	 * 
	 * @param time
	 *            Time since a move key has been pressed
	 * @param speed
	 *            Default speed being used
	 * @return the value to move at
	 */
	private float changeValue(float time, float speed) {
		return ((speed * Gdx.graphics.getDeltaTime()) / time) * Gdx.graphics.getDeltaTime();
	}

	@Override
	public boolean keyDown(int keycode) {
		// if U is pressed set zoom to original
		if (keycode == Keys.U)
			camera.zoom = 1;
		return true;
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
		stage.unfocusAll();
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		determineCountry(screenX, screenY);
		System.out.println(getRGBValues(screenX, screenY) + "\n");
		return true;
	}

	private void determineCountry(int screenX, int screenY) {
		Vector3 color = getRGBValues(screenX, screenY);

		for (Country c : playableGame.getCountries().values()) {
			if (c.getR() == color.x && c.getG() == color.y && c.getB() == color.z) {
				System.out.println(c.getName() + " has been clicked.");
				
				new CountryDialog(c, skin).show(stage);
				
				return;
			}
		}
		System.out.println("Country clicked is unknown");
	}
	
	public void updateCountry(Game playableGame){
		this.playableGame = playableGame;
	}

	/**
	 * Gets the RGB values of the clicked pixel
	 * 
	 * @param screenX
	 *            X clicked position
	 * @param screenY
	 *            Y clicked position
	 * @return Vector3f of the RGB values.
	 */
	private Vector3 getRGBValues(int screenX, int screenY) {
		Vector2 clickedPos = Utils.convertToMapCordinates(screenX, screenY, camera);
		float newY = colorMap.getHeight() - clickedPos.y;
		int value = colorMap.getPixel((int) clickedPos.x, (int) newY);
		int R = ((value & 0xff000000) >>> 24);
		int G = ((value & 0x00ff0000) >>> 16);
		int B = ((value & 0x0000ff00) >>> 8);

		return new Vector3(R, G, B);
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

	/**
	 * @return The primary (good looking) map associated with this map
	 */
	public Texture getPrimaryMap() {
		return primaryMap;
	}

	/**
	 * Sets the primary map (good looking one) associated with this map
	 * 
	 * @param primaryMap
	 *            Texture to set the map to
	 */
	public void setPrimaryMap(Texture primaryMap) {
		this.primaryMap = primaryMap;
	}
}
