package io.github.wreed12345;

import io.github.wreed12345.input.CommandListener;
import io.github.wreed12345.shared.Constants;
import io.github.wreed12345.shared.CreateGameRequest;
import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.GameListReply;
import io.github.wreed12345.shared.LoginRequest;
import io.github.wreed12345.shared.NewPlayer;
import io.github.wreed12345.shared.Player;
import io.github.wreed12345.shared.countries.Country;
import io.github.wreed12345.shared.placeable.AttackType;
import io.github.wreed12345.shared.placeable.Placeable;
import io.github.wreed12345.util.ErrorHandler;
import io.github.wreed12345.util.Util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.BlowfishSerializer;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Main {

	public static void main(String[] args) throws IOException {
		long startTime = System.nanoTime();
		Log.set(Log.LEVEL_INFO);
		Main m = new Main();
		m.gameHandler.loadGameAmountNumber();
		m.gameHandler.loadGameData();
		m.games.get(0).getCountries().get("Ferrarcia").setInfo("Ferrarcia is a country rich in oil supplies. It is a generally flat and prone to attacks due to its accesible nature. It has open access to water making it a country dependent on water based trade.");
		m.init();
		m.createListeners();
		m.playerHandler.loadPlayerInfo();
		long ellapsedTime = (System.nanoTime() - startTime) / 1000000;
		Util.log("Server succesfully started up in " + ellapsedTime + "ms.");
	}

	private Server server = new Server(12228, 4096);
	private List<String> usernames = Collections.synchronizedList(new ArrayList<String>());
	private List<Player> onlinePlayers = Collections.synchronizedList(new ArrayList<Player>());
	private List<Game> games = Collections.synchronizedList(new ArrayList<Game>());
	private ObjectProcessor objectProcessor = new ObjectProcessor(games, onlinePlayers);
	private GameHandler gameHandler = new GameHandler(games, onlinePlayers);
	private PlayerHandler playerHandler = new PlayerHandler(games, onlinePlayers, usernames);

	private static final long MILLISECONDS_PER_5_MINUTES = 300000;

	private void init() throws IOException {
		Thread thread = new Thread() {
			public void run() {
				try {
					server.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				playerHandler.savePlayerInfo();
			}
		};
		thread.setDaemon(false);
		
		Thread shutdown = new Thread() {
			public void run() {
				Util.log("Server shutting down...");
				playerHandler.savePlayerInfo();
				playerHandler.savePlayerData();
				gameHandler.saveGameInfo();
				Util.log("Server gracefully shut down.");
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdown);
		thread.start();

		// save game data + player data every 5 minutes
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Util.log("Periodic game and player save occuring.");
				playerHandler.savePlayerInfo();
				playerHandler.savePlayerData();
				gameHandler.saveGameInfo();
				Util.log("Periodic game and player save complete.");
			}

		}, MILLISECONDS_PER_5_MINUTES, MILLISECONDS_PER_5_MINUTES);
		
		//run server console thing.
		Thread cmdListener = new CommandListener(games, onlinePlayers, usernames);
		cmdListener.start();
		
		server.bind(54555, 54777);

		registerClasses();
		setupErrorHandler();
		
		
	}
	
	private void setupErrorHandler(){
		PrintStream origOut = System.err;
		PrintStream errorHandler = new ErrorHandler(origOut);
		System.setErr(errorHandler);
	}

	private void registerClasses() {
		Kryo kryo = server.getKryo();
		kryo.register(Game.class, 100);
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
		kryo.register(Country.class, 102);
		kryo.register(Long.class, 113);
		Util.log("Classes sucesfully registered for serializing");
	}

	private void createListeners() {
		server.addListener(new Listener() {
			public void received(Connection connection, Object object) {
				if (object instanceof NewPlayer) {
					playerHandler.checkAndCreatePlayer((NewPlayer) object, connection);
				} else if (object instanceof LoginRequest) {
					playerHandler.loginPlayer((LoginRequest) object, connection);
				} else if (object instanceof CreateGameRequest) {
					gameHandler.createGame(connection, (CreateGameRequest) object);
				} else if (object instanceof Placeable) {
					objectProcessor.processPlaceable((Placeable) object);
				} else if (object instanceof String) {
					objectProcessor.processString((String) object, connection);
				}
			}

			// log out players
			public void disconnected(Connection connection) {
				playerHandler.logoutPlayer(connection);
			}
		});
		Util.log("Server listeners created");
	}
}