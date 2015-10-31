package io.github.wreed12345;

import io.github.wreed12345.AssetManagerHelper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

	public static final String VERSION = "0.0.11 Alpha";
	private AssetManager assetManager;
	private AssetManagerHelper assetManagerHelper = new AssetManagerHelper();

	@Override
	public void create() {
		// setup the socket system
//		Thread singleClient = new Thread(new SingleClient());
//		// set it to a daemon thread so if the main game crashes the sockets
//		// will shutdown
//		singleClient.setDaemon(true);
//		singleClient.start();
		assetManager = assetManagerHelper.getAssetManager();
		loadData();
		setScreen(new LoadScreen(assetManager, this));
	}

	@Override
	public void dispose() {
		assetManager.dispose();
		assetManagerHelper.dispose();
		super.dispose();
	}

	private void loadData() {
		assetManager.load("data/LoadScreen/images/earth.png", Texture.class);
		// stall until loading screen is loaded. should be quick
		assetManager.finishLoading();
	}

	@Override
	public void render() {
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
