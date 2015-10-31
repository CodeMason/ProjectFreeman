package io.github.wreed12345;

import io.github.wreed12345.shared.CreateGameRequest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class CreateGameScreen implements Screen {

	private AssetManager assetManager;
	private Client client;
	private com.badlogic.gdx.Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	private Skin skin;
	private Stage stage;

	public CreateGameScreen(AssetManager assetManager, com.badlogic.gdx.Game game, Client client) {
		this.assetManager = assetManager;
		this.client = client;
		this.game = game;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// set up camera, etc
		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();

		batch = new SpriteBatch();

		background = assetManager.get("data/JoinGameScreen/images/background.png", Texture.class);
		skin = assetManager.get("data/JoinGameScreen/ui/uiskin.json", Skin.class);

		clientListeners();
		setupStage();
	}

	private boolean setScreen;
	private io.github.wreed12345.shared.Game playableGame;
	private Listener listener;

	private void clientListeners() {
		listener = new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof String) {
					String response = (String) object;
					new Dialog(response, skin, "dialog") {
					}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
				} else if (object instanceof io.github.wreed12345.shared.Game) {
					io.github.wreed12345.shared.Game game = (io.github.wreed12345.shared.Game) object;
					setScreen = true;
					playableGame = game;
				}
			}
		};
		client.addListener(listener);
	}

	private void setupStage() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		Table table = new Table();
		Label nameTitle = new Label("Game Name:", skin);
		final TextField name = new TextField("Game Name", skin);
		TextButton createButton = new TextButton("Create Game", skin);

		table.add(nameTitle);
		table.add(name).row();
		table.add(createButton);

		table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		stage.addActor(table);

		createButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				client.sendTCP(new CreateGameRequest(name.getText()));
			}
		});
	}

	private void draw(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.begin();
		{
			batch.draw(background, 0, 0);
		}
		batch.end();

		stage.draw();
	}

	private void update(float delta) {
		camera.update();
		stage.act();

		if (setScreen) {
			listener = null;
			// client.removeListener(listener);
			game.setScreen(new GameScreen(assetManager, game, client, playableGame));
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
