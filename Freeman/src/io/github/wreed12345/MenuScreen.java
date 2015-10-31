package io.github.wreed12345;

import io.github.wreed12345.ui.Button;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class MenuScreen implements Screen{

	private AssetManager assetManager;
	private Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture startTexture; 
	private Button startButton;
	
	public MenuScreen(AssetManager assetManager, Game game){
		this.assetManager = assetManager;
		this.game = game;
		
		startTexture = assetManager.get("data/MenuScreen/images/playgame-button.png", Texture.class);		
		
		startButton = new Button(startTexture, new Vector2(500, 500));
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// set up camera, etc
		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();

		batch = new SpriteBatch();
	}
	
	private void draw(float delta){
		camera.update();
		Gdx.gl.glClearColor(1, 1, 1, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		{
			startButton.draw(batch);
		}
		batch.end();
	}
	
	private void update(float delta){
		if(startButton.isClicked()){
			game.setScreen(new JoinGameScreen(assetManager, game));
		}
	}
	
	@Override
	public void render(float delta) {
		draw(delta);
		update(delta);
		
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
		
	}

}