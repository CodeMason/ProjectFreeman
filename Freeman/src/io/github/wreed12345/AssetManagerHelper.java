package io.github.wreed12345;

import com.badlogic.gdx.assets.AssetManager;

public class AssetManagerHelper {
	
	private AssetManager assetManager = new AssetManager();

	/**
	 * Loads all assets into context
	 */
	public void load(){
		
	}
	
	/**
	 * Convenience method to dispose of contents of the AssetManager
	 */
	public void dispose(){
		assetManager.dispose();
	}
	
	/**
	 * Progress of the asset manager
	 * @return Progress of the asset manager
	 */
	public float getProgress(){
		return assetManager.getProgress();
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAs(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	
	
}
