package io.github.wreed12345.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import io.github.wreed12345.util.Util;
import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.NewPlayer;
import io.github.wreed12345.shared.Player;

public class CommandProcessor {
	private List<Game> games;
	private List<Player> onlinePlayers;
	private List<String> usernames;
	
	public CommandProcessor(List<Game> games, List<Player> onlinePlayers, List<String> usernames){
		this.games = games;
		this.onlinePlayers = onlinePlayers;
		this.usernames = usernames;
	}
	
	public void processCommand(String command){
		boolean commandExists = false;
		
		//find out if the command is real
		for(Commands c : Commands.values()){
			
			if(command.contains(c.getCommand())){
				commandExists = true;
				String[] commandSplit = commandSplitter(command);
				
				//check which command it is and execute based off of that.
				switch (c){
					case BAN_IP:
						banIP(commandSplit);
						break;
					case BAN_USERNAME:
						banUsername(commandSplit);
						break;
					case DELETE_ACCOUNT:
						deleteAccount(commandSplit);
						break;
					case GAME_ID:
						gameID(commandSplit);
						break;
					case GAME_STOP:
						gameStop(commandSplit);
						break;
					case GET_PASSWORD:
						getPassword(commandSplit);
						break;
					case SAVE:
						save(commandSplit);
						break;
					case SERVER_INFO:
						serverInfo(commandSplit);
						break;
					case STOP:
						stop(commandSplit);
						break;
					case HELP:
						help(commandSplit);
					case CREATE_ACCOUNT:
						createAccount(commandSplit);
						break;
					default:
						break;
				}
				//exit the method at this point since we know everything went ok.
				return;
			}
		}
		
		if(!commandExists){
			Util.log("Command \"" + command + "\" does not exist. Type \"help\" for a valid list of commands.");
		}
	}
	
	private void banIP(String[] command){
		Util.log("Command not configured yet.");
		//add list to banned list of ip addresses.
	}
	
	private void banUsername(String[] command){
		Util.log("Command not configured yet.");
		//add to list of banned usernames
		
	}
	
	private void deleteAccount(String[] command){
		Util.log("Command not configured yet.");
		//remove from list of online players, make sure they are disconnected. remove from any games.
	}
	
	private void gameID(String[] command){
		if(!commandLength(2, Commands.GAME_ID.getAmountOfArguements(), command)) return;
		String gameName = command[2];
		boolean gameFound = false;
		
		synchronized(games){
			for(Game g : games){
				if(g.getName().equals(gameName)){
					gameFound = true;
					Util.log("Game ID for \"" + gameName + "\" is: " + g.getId());
				}
			}
		}
		if(!gameFound){
			Util.log("No game with the name \"" + gameName + "\" was found...");
		}
		//loop through games and check if the name matches the one in command.
	}
	
	private void gameStop(String[] command){
		Util.log("Command not configured yet.");
		//loop through games and stop the game when it comes to the one with that id.
	}
	
	private void getPassword(String[] command){
		if(!commandLength(2, Commands.GET_PASSWORD.getAmountOfArguements(), command)) return;
		//access file and retrieve password
		String username = command[2];
		File playerInfoFile = new File("playerData/" + username + ".dat");
		if (playerInfoFile.exists()) {
			try{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(playerInfoFile));
				Player player = (Player) in.readObject();
				Util.log("Password for user: \"" + player.getName() + "\" is: \"" + player.getPassword() + "\"");
				in.close();
			}catch(Exception e){
				Util.log("WARNING: Error occured while executing \"get password " + username + "\"");
				e.printStackTrace();
			}
		}else {
			Util.log("Username \"" + username + "\" does not exist.");
		}
	}
	
	private void save(String[] command){
		Util.log("Command not configured yet.");
		//save all games and players.
	}
	
	private void serverInfo(String[] command){
		Util.log("Command not configured yet.");
		//gather system info and display it
	}
	
	private void stop(String[] command){
		System.exit(0);
		//stop server gracefully and send message to users saying that they disconnected and that the server will be back soon.
	}
	
	private void help(String[] command){
		Util.log("Command not configured yet.");
	}
	
	private void createAccount(String[] command){
		if(!commandLength(2, Commands.CREATE_ACCOUNT.getAmountOfArguements(), command)) return;
		String username = command[2];
		String password = command[3];
		
		NewPlayer newPlayer = new NewPlayer(username, password);
		
		synchronized (usernames) {
			for (String s : usernames) {
				if (newPlayer.getRequestedName().equalsIgnoreCase(s)) {
					Util.log("Username is already in use.");
					return;
				}
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


		Util.log("Account created for user: " + player.getName());
		
	}
	
	private boolean commandLength(int prefixLength, int argsLength, String[] command){
		if (prefixLength + argsLength == command.length) return true;
		else {
			Util.log("Incorrect arguments");
			return false;
		}
	}
	
	private String[] commandSplitter(String command){
		return command.split(" ");
	}
}