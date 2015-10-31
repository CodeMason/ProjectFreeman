package io.github.wreed12345;

import io.github.wreed12345.placable.Rocket;
import io.github.wreed12345.shared.Constants;
import io.github.wreed12345.shared.CreateGameRequest;
import io.github.wreed12345.shared.GameListReply;
import io.github.wreed12345.shared.LoginRequest;
import io.github.wreed12345.shared.NewPlayer;
import io.github.wreed12345.shared.Player;
import io.github.wreed12345.shared.SimpleUpdates;
import io.github.wreed12345.shared.countries.Country;
import io.github.wreed12345.shared.placeable.AttackType;
import io.github.wreed12345.shared.placeable.Placeable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BlowfishSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class JoinGameScreen implements Screen {

	private AssetManager assetManager;
	private com.badlogic.gdx.Game game;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;

	private String[] games = { "" };
	private HashMap<String, Long> gameListMap;

	private Skin skin;
	private Stage stage;

	private Client client;// pass this along as you go
	private Preferences prefs;

	// TODO: CLEAN THIS UP. ITS GROSS
	public JoinGameScreen(AssetManager assetManager, com.badlogic.gdx.Game game) {
		this.assetManager = assetManager;
		this.game = game;

		prefs = Gdx.app.getPreferences("freeman.dat");

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		// set up camera, etc
		camera = new OrthographicCamera(w, h);
		camera.translate(w / 2, h / 2);
		camera.update();

		batch = new SpriteBatch();

		background = assetManager.get("data/JoinGameScreen/images/background.png", Texture.class);
		skin = assetManager.get("data/JoinGameScreen/ui/uiskin.json", Skin.class);

		setupClient();
		setupStage();

		loginAttempt(prefs.getString("username"), prefs.getString("password"));
		client.sendTCP(SimpleUpdates.gameListRequest(prefs.getString("username")));

	}

	private boolean setScreen;
	private io.github.wreed12345.shared.Game playableGame;
	private Listener listener;

	/**
	 * Logs into the server from info in the launcher
	 * 
	 * @param username
	 * @param password
	 */
	private void loginAttempt(String username, String password) {
		client.sendTCP(new LoginRequest(username, password));

		listener = new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof io.github.wreed12345.shared.Game) {
					io.github.wreed12345.shared.Game gameRecieved = (io.github.wreed12345.shared.Game) object;
					setScreen = true;
					playableGame = gameRecieved;

				} else if (object instanceof GameListReply) {
					GameListReply gameListReply = (GameListReply) object;
					games = gameListReply.getGames().keySet().toArray(
							new String[gameListReply.getGames().keySet().size()]);
					gameListMap = gameListReply.getGames();

					gameList.setItems(games);
					gameList.setSelectedIndex(-1);
					
				} else if (object instanceof String) {
					String response = (String) object;
					if (response.equals("login sucessful")) {
						Log.info("Freeman-Client", "Logged in for user: " + prefs.getString("username"));
					} else if(response.equals("Username does not exist")){
						new Dialog("Username does not exist error...", skin, "dialog").show(stage);
					} else {
						new Dialog(response, skin, "dialog") {
						}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);
					}
				}
			}
		};

		client.addListener(listener);
	}

	public static final String SERVER_ADRESS = "69.124.180.8";

	/**
	 * Connects client and registers classes
	 */
	private void setupClient() {
		client = new Client(12228, 4096);
		client.start();
		try {
			client.connect(5000, SERVER_ADRESS, 54555, 54777);
		} catch (IOException e) {
			Log.info("Freeman-Client", "Could not connect to server!");
			new Dialog("Could not connect to server!", skin, "dialog") {
			}.button("Return", true).key(Keys.ENTER, true).key(Keys.ESCAPE, true).show(stage);

			// TODO: got dialog to work... is that enouguh? game.setScreen(new
			// MenuScreen(assetManager, game));
			return;
		}
		Kryo kryo = client.getKryo();
		kryo.register(io.github.wreed12345.shared.Game.class, 100);
		kryo.register(Country.class, 102);
		kryo.register(Player.class, new BlowfishSerializer(new FieldSerializer<Player>(kryo, Player.class),
				Constants.key), 103);
		kryo.register(NewPlayer.class, new BlowfishSerializer(new FieldSerializer<NewPlayer>(kryo,
				NewPlayer.class), Constants.key), 104);
		kryo.register(String.class, 1);
		kryo.register(LoginRequest.class, new BlowfishSerializer(new FieldSerializer<LoginRequest>(kryo,
				LoginRequest.class), Constants.key), 106);
		kryo.register(CreateGameRequest.class, 107);
		kryo.register(ArrayList.class, 108);
		kryo.register(GameListReply.class, 109);
		kryo.register(HashMap.class, 110);
		kryo.register(Placeable.class, 111);
		kryo.register(AttackType.class, 112);
		kryo.register(Rocket.class, 200);
		kryo.register(Long.class, 113);
		
	}

	private List gameList;

	/**
	 * Sets up everything needed for the stage. UI stuff
	 */
	void setupStage() {
		// be very concerned when we go to android
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		Gdx.input.setInputProcessor(stage);

		gameList = new List(games, skin);

		Window window = new Window("Current Games", skin);
		window.setWidth(200);
		window.add(gameList).width(180);
		window.setPosition(300, 300);

		Label title = new Label("  Join a Game  ", skin);
		title.setPosition(Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, Gdx.graphics.getHeight() * .8f);

		TextButton joinButton = new TextButton(" Random Game ", skin);
		joinButton.setPosition(800, 400);
		TextButton createGame = new TextButton(" Create Game ", skin);
		createGame.setPosition(800, 350);

		joinButton.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				client.sendTCP("random-join");// convert to enum thing?
			}
		});

		createGame.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				listener = null;
				game.setScreen(new CreateGameScreen(assetManager, game, client));
			}
		});

		// join games listener
		gameList.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				int index = gameList.getSelectedIndex();
				long gameID = gameListMap.get(games[index]);
				client.sendTCP(SimpleUpdates.gameJoinRequest(gameID));
			}
		});

		// judge when to send join game things to server

		stage.addActor(title);
		stage.addActor(window);
		stage.addActor(joinButton);
		stage.addActor(createGame);
	}

	private void draw(float delta) {
		camera.update();
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
		if (setScreen) {
			listener = null;
			game.setScreen(new GameScreen(assetManager, game, client, playableGame));
		}
		stage.act();
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
