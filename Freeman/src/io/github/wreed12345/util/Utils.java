package io.github.wreed12345.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

/**
 * Various Static methods used throughout the game.
 * @author William Reed
 * @since 0.0.11
 */
public class Utils {
	
	/**
	 * Takes a clicked position (x, y) and converts it into the position that
	 * was clicked on the map. (from bottom left corner)
	 * 
	 * @param screenX The x clicked pos
	 * @param screenY the y clicked pos
	 * @return Vector of the clicked map position
	 */
	public static Vector2 convertToMapCordinates(float screenX, float screenY, OrthographicCamera camera) {
		float middleX = Gdx.graphics.getWidth() / 2;
		float middleY = Gdx.graphics.getHeight() / 2;
		// adjust screenY to y down
		screenY = Gdx.graphics.getHeight() - screenY;

		middleX += camera.position.x - Gdx.graphics.getWidth() / 2;
		middleY += camera.position.y - Gdx.graphics.getHeight() / 2;

		float scaledX = (screenX * camera.zoom) - middleX;
		float scaledY = (screenY * camera.zoom) - middleY;

		middleX += scaledX;
		middleY += scaledY;
		float virtualWidth = Gdx.graphics.getWidth() * camera.zoom;
		float virtualHeight = Gdx.graphics.getHeight() * camera.zoom;
		float offsetX = (Gdx.graphics.getWidth() - virtualWidth) / 2;
		float offsetY = (Gdx.graphics.getHeight() - virtualHeight) / 2;

		middleX += offsetX + camera.position.x - Gdx.graphics.getWidth() / 2;
		middleY += offsetY + camera.position.y - Gdx.graphics.getHeight() / 2;

		return new Vector2(middleX, middleY);
	}
	
	/**Determines if the designated position is in the rectangle. clickedX / Y and recX / Y
	 * must be based on the same (0,0) coordinate
	 * 
	 * @param clickedX the clicked x pos
	 * @param clickedY the clicked y pos
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param recX x position of the rectangle
	 * @param recY y position of the rectangle
	 * @return true if the given position is in the rectangle
	 */
	public static boolean clickedPosInBox(int clickedX, int clickedY, int width, int height, int recX, int recY){
			return clickedX >  recX && clickedX < recX + width &&
					clickedY > recY && clickedY < recY + height;
	}
	
	/**@return the distance between two points*/
	public static double distance(double x1, double y1, double x2, double y2) { 
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2)); 
	}
}
