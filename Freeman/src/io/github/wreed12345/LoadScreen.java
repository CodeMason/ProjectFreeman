package io.github.wreed12345;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class LoadScreen implements Screen {
	private Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture earth;
	private BitmapFont font = new BitmapFont();
	private AssetManager assetManager;
	
	public LoadScreen(AssetManager assetManager, Game game) {
		this.assetManager = assetManager;
		this.game = game;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// set up camera, etc
		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();

		batch = new SpriteBatch();

		earth = assetManager.get("data/LoadScreen/images/earth.png",Texture.class);
		
		assetManager.load("data/MenuScreen/images/playgame-button.png", Texture.class);
		assetManager.load("data/JoinGameScreen/ui/uiskin.json", Skin.class);
		assetManager.load("data/JoinGameScreen/images/background.png", Texture.class);
		assetManager.load("data/GameScreen/images/map.png", Texture.class);
		assetManager.load("data/GameScreen/images/mapWithColors.png", Pixmap.class);
		assetManager.load("data/GameScreen/images/rocket.png", Texture.class);
		assetManager.load("data/GameScreen/images/rocketIcon.png", Texture.class);
		assetManager.load("data/GameScreen/images/flag.png", Texture.class);
		//assetManager.load("data/GameScreen/images/smoke.p", ParticleEffect.class);
	}
	
	float progress;
	
	@Override
	public void render(float delta) {
		camera.update();
		Gdx.gl.glClearColor(0, 0, 0, .3f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		{	
			font.draw(batch, "FPS " + Gdx.graphics.getFramesPerSecond(), 1200, 700);
			batch.draw(earth, Gdx.graphics.getWidth() / 2 - earth.getWidth() / 2, Gdx.graphics.getHeight() / 2 - earth.getHeight() / 2);
			font.draw(batch, progress * 100 + "%", 50, 50);
		}
		batch.end();
		
		progress = assetManager.getProgress(); // ranges from 0 to 1 TODO: implement this
		if(assetManager.update()){
			assetManager.unload("data/LoadScreen/images/earth.png");
			game.setScreen(new MenuScreen(assetManager, game));
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		earth.dispose();
		font.dispose();
		batch.dispose();
	}

}
