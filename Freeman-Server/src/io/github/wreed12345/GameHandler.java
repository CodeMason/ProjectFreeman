package io.github.wreed12345;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import io.github.wreed12345.shared.CreateGameRequest;
import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.Player;

import com.esotericsoftware.kryonet.Connection;

public class GameHandler {
	List<Game> games;
	List<Player> onlinePlayers;

	public GameHandler(List<Game> games, List<Player> onlinePlayers) {
		this.games = games;
		this.onlinePlayers = onlinePlayers;

	}

	public int gameNumber = 0;

	public void createGame(Connection connection, CreateGameRequest request) {
		synchronized (games) {
			for (Game g : games) {
				if (g.getName().equals(request.getGameName())) {
					connection.sendTCP("  Game name in use  ");
					return;
				}
			}
		}
		Game game = new Game(gameNumber, request.getGameName());
		// send player into game.
		synchronized (onlinePlayers) {
			for (Player p : onlinePlayers) {
				if (p.getAmountOfGames() == 3) {
					connection.sendTCP("You are already in the maximum of 3 games!");
					return;
				}
				if (p.getConnection().equals(connection)) {
					game.addPlayer(p.getName());
					p.getGameIDs().add(game.getId());
				}
			}
		}
		gameNumber++;
		games.add(game);
		connection.sendTCP(game);
		// update other connections
		for (String s : game.getUsernames()) {
			for (Player p : onlinePlayers) {
				if (s.equals(p.getName())) {
					p.getConnection().sendTCP(game);
				}
			}
			// s.getConnection().sendTCP(object)
		}
	}

	public void loadGameAmountNumber() {
		try {
			File gamesInfoFile = new File("gameInfo.dat");
			if (!gamesInfoFile.exists())
				return;
			FileInputStream fileIn = new FileInputStream(gamesInfoFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			int gameAmount = in.readInt();
			gameNumber = gameAmount;
			fileIn.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadGameData() {
		try {
			File gameFile;
			FileInputStream fileIn = null;
			ObjectInputStream in = null;
			for (int i = 0; i <= gameNumber; i++) {
				gameFile = new File("gameInfo/" + i + ".dat");
				if (!gameFile.exists())
					continue;
				fileIn = new FileInputStream(gameFile);
				in = new ObjectInputStream(fileIn);
				Game game = (Game) in.readObject();
				games.add(game);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void saveGameInfo() {
		try {
			File gamesInfoFile = new File("gameInfo.dat");
			FileOutputStream fileOut = new FileOutputStream(gamesInfoFile, false);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeInt(gameNumber);
			out.flush();

			// TODO: monitor this performance one day
			synchronized (games) {
				for (Game g : games) {
					File gameFile = new File("gameInfo/" + g.getId() + ".dat");
					if (!gameFile.exists())
						gameFile.createNewFile();
					fileOut = new FileOutputStream(gameFile, false);
					out = new ObjectOutputStream(fileOut);
					out.writeObject(g);
					out.flush();
				}
			}
			fileOut.close();
			out.flush();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
