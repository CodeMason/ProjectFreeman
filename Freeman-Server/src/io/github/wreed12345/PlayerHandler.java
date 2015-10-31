package io.github.wreed12345;

import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.LoginRequest;
import io.github.wreed12345.shared.NewPlayer;
import io.github.wreed12345.shared.Player;
import io.github.wreed12345.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;

public class PlayerHandler {
	List<Game> games;
	List<Player> onlinePlayers;
	List<String> usernames;

	public PlayerHandler(List<Game> games, List<Player> onlinePlayers, List<String> usernames) {
		this.games = games;
		this.onlinePlayers = onlinePlayers;
		this.usernames = usernames;
		
		loadBannedNames();
	}

	public void loadPlayerInfo() {
		try {
			File playerInfoFile = new File("playerInfo.dat");
			if (!playerInfoFile.exists()) {
				playerInfoFile.createNewFile();
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(playerInfoFile));
				out.writeInt(0);
				out.close();
			}
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(playerInfoFile));
			int users = in.readInt();

			for (int i = 0; i < users; i++) {
				String playerName = (String) in.readObject();
				usernames.add(playerName);
			}

			in.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void savePlayer(Player player) {
		try {
			File playerFile = new File("playerData/" + player.getName() + ".dat");
			FileOutputStream fileOut = new FileOutputStream(playerFile, false);// overwright
																				// previous
																				// file
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(player);
			fileOut.flush();
			out.flush();
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean someonesOnline;

	// saves online player info. if they r not online it is saved since it is
	// saved when they logout
	public void savePlayerData() {
		try {
			File playerFile;
			FileOutputStream fileOut = null;
			ObjectOutputStream out = null;

			synchronized (onlinePlayers) {
				for (Player player : onlinePlayers) {
					playerFile = new File("playerData/" + player.getName() + ".dat");
					fileOut = new FileOutputStream(playerFile, false);
					out = new ObjectOutputStream(fileOut);
					out.writeObject(player);
					fileOut.flush();
					out.flush();
					someonesOnline = true;
				}
			}
			if (!someonesOnline)
				return;
			fileOut.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void savePlayerInfo() {// save amount of players and there names so
		// it can be loaded
		try {
			File playerInfoFile = new File("playerInfo.dat");
			FileOutputStream fileOut = new FileOutputStream(playerInfoFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeInt(usernames.size());
			for (String s : usernames) {
				out.writeObject(s);
			}
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private String[] bannedUsernames;
	public void loadBannedNames(){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("banned names.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			Log.info("banned names.txt not found!");
			System.exit(1);
		}
		
		ArrayList<String> names = new ArrayList<String>();
		try{
			String line = br.readLine();
			
			while(line != null){
				names.add(line);
				line = br.readLine();
			}
			br.close();
			bannedUsernames = names.toArray(new String[names.size()]);
		}catch (IOException e){
			e.printStackTrace();
		} 
	}

	public void checkAndCreatePlayer(NewPlayer newPlayer, Connection connection) {// create
		// players
		// file.
		synchronized (usernames) {
			for (String s : usernames) {
				if (newPlayer.getRequestedName().equalsIgnoreCase(s)) {
					connection.sendTCP("Username is already in use.");
					return;
				}
			}
		}
		
		for(String s : bannedUsernames){
			if(newPlayer.getRequestedName().contains(s)){
				connection.sendTCP("Username not aloud to contain: " + s);
				return;
			}
		}
		
		// not already in use create file for user.
		Player player = new Player(newPlayer.getRequestedName(), newPlayer.getPassword());

		try {
			File playerFile = new File("playerData/" + player.getName() + ".dat");
			playerFile.createNewFile();
			FileOutputStream fileOut = new FileOutputStream(playerFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			usernames.add(player.getName());
			out.writeObject(player);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		connection.sendTCP("Account Created");

		Util.log("Account created for user: " + player.getName());
	}

	public void loginPlayer(LoginRequest loginRequeset, Connection connection) {
		boolean usernameExists = false;

		synchronized (usernames) {
			for (String s : usernames) {
				if (s.equals(loginRequeset.getUsername())) {
					usernameExists = true;
					break;
				}
			}
		}

		if (!usernameExists) {
			connection.sendTCP("Username does not exist");
			return;
		}
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("playerData/"
					+ loginRequeset.getUsername() + ".dat"));
			Player player = (Player) in.readObject();

			// match pass
			if (!player.getPassword().equals(loginRequeset.getPassword())) {
				connection.sendTCP("Incorrect Password");
				in.close();
				return;
			}

			player.setConnection(connection);
			onlinePlayers.add(player);
			Util.log(player.getName() + " has logged in.");
			connection.sendTCP("login sucessful");
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void logoutPlayer(Connection connection) {
		// removes players from arraylist when they disconnect
		boolean remove = false;
		Player player = null;

		synchronized (onlinePlayers) {
			for (Player p : onlinePlayers) {
				if (p.getConnection().equals(connection)) {
					Util.log(p.getName() + " has logged off.");
					remove = true;
					player = p;
					break;
				}
			}
		}
		if (remove) {
			savePlayer(player);
			player.setConnection(null);
			onlinePlayers.remove(onlinePlayers.indexOf(player));
			player = null;
			remove = false;
		}
		if (onlinePlayers.size() == 0) {
			someonesOnline = false;
		}
	}

}
