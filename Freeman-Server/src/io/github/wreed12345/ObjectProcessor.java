package io.github.wreed12345;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;

import io.github.wreed12345.shared.Game;
import io.github.wreed12345.shared.GameListReply;
import io.github.wreed12345.shared.Player;
import io.github.wreed12345.shared.placeable.Placeable;

public class ObjectProcessor {
	List<Game> games;
	List<Player> onlinePlayers; //TOOD: most of these uses are not being synchronized at all....

	public ObjectProcessor(List<Game> games, List<Player> onlinePlayers) {
		this.games = games;
		this.onlinePlayers = onlinePlayers;

	}

	public void processPlaceable(Placeable placeable) {
		// figure out which game its in
		synchronized (games) {
			for (Game g : games) {
				if (g.getId() == placeable.getGameID()) {
					//check if this is the deletion of it.
					if(placeable.getOwner().equals("!@!MARKED_FOR_DEATH")){
						for(Placeable p : g.getPlaceables()){
							if(p.getId().equals(placeable.getId())){
								g.getPlaceables().remove(p);
								
								//send update
								for (String s : g.getUsernames()) {
									for (Player player : onlinePlayers) {
										if (player.getName().equals(s)) {
											// dos nested for loops doe.
											player.getConnection().sendTCP(placeable);
										}
									}
								}
								
								return;
							}
						}
					}
					
					// we have a match at this point
					g.addPlaceable(placeable);

					for (String s : g.getUsernames()) {
						for (Player p : onlinePlayers) {
							if (p.getName().equals(s)) {
								// dos nested for loops doe.
								p.getConnection().sendTCP(placeable);
							}
						}
					}
				}
			}
		}//lol
	}

	public void processString(String string, Connection connection) {
		// my complex system of getting an open game
		if (string.equals("random-join")) {
			synchronized (games) {
				GAMELOOP: for (Game g : games) {
					if (g.isJoinable()) {
						for (Player p : onlinePlayers) {
							if (p.getConnection().equals(connection)) {
								if (p.getAmountOfGames() == 3) {
									connection.sendTCP("You are already in the maximum of 3 games!");
									return;
								}
								for (long l : p.getGameIDs()) {
									if (l == g.getId()) {
										continue GAMELOOP;
									}
								}
								g.addPlayer(p.getName());
								p.getGameIDs().add(g.getId());
								break;
							}
						}
						connection.sendTCP(g);
						for (String s : g.getUsernames()) {
							for (Player p : onlinePlayers) {
								if (p.getName().equals(s)) {
									if (p.getConnection().isConnected()) {
										p.getConnection().sendTCP(g);
									}
								}
							}
						}
						return;
					}
				}
			}
			connection.sendTCP(" No open games available ");
			// signifys game update v
		} else if (string.startsWith("0!@!")) {
			string = string.replaceFirst("0!@!", "");
			String[] content = string.split("!@!");

			// chat message
			if (content[0].equals("1")) {
				String gameID = content[1];
				String player = content[2];
				String message = content[3];
				String messages[] = { "", "", "" };
				if (message.length() > 85) {
					messages[2] = message.substring(85);
					message = message.replace(messages[2], "");
					messages[1] = message.substring(35);
					message = message.replace(messages[1], "");
					messages[1] = messages[1] + "-";
					messages[0] = message + "-";
				} else if (message.length() > 35) {
					messages[1] = message.substring(35);
					message = message.replace(messages[1], "");
					messages[0] = message + "-";
				}

				synchronized (games) {
					for (Game g : games) {
						if (g.getId() == Long.parseLong(gameID)) {
							if (messages[0].equals("")) {// will this suffice?
								message = "[" + player + "] " + message;
								g.addMessage(message);
							} else {
								for (int i = 0; i < messages.length; i++) {
									if (messages[i].equals(""))
										break;
									if (i == 0) {
										g.addMessage("[" + player + "] " + messages[i]);
										continue;
									}
									g.addMessage(messages[i]);
								}
							}
							for (String s : g.getUsernames()) {
								for (Player p : onlinePlayers) {
									if (s.equals(p.getName())) {
										p.getConnection().sendTCP(g);
									}
								}
							}
							return;//right? 2/5/14
						}
					}
				}
				
			//TODO: change to use the statics. change from private to public
			}else if(content[0].equals("2")){
				String gameID = content[1];
				String countryName = content[2];
				String amount = content[3];
				
				synchronized (games) {
					for (Game g : games) {
						if(g.getId() == Long.valueOf(gameID)){
							g.getCountries().get(countryName).looseHealth(Integer.valueOf(amount));
							System.out.println("country " + countryName + "'s health was deducted " + amount);
							
							for (String s : g.getUsernames()) {
								synchronized(onlinePlayers){
									for (Player p : onlinePlayers) {
										if (s.equals(p.getName())) {
											System.out.println("sending game to " + s);
											p.getConnection().sendTCP(g);
										}
									}
								}
							}
							
							return;
						}
					}
				}
			}
			// means simple update message
		} else if (string.startsWith("1!@!")) {
			string = string.replaceFirst("1!@!", "");
			String[] content = string.split("!@!");// Separator regex

			// game list request
			if (content[0].equals("2")) {
				String player = content[1];
				Long[] gameIDs = null;
				ArrayList<String> gameNames = new ArrayList<String>();

				synchronized (onlinePlayers) {
					for (Player p : onlinePlayers) {
						if (p.getName().equals(player)) {
							gameIDs = p.getGameIDs().toArray(new Long[p.getGameIDs().size()]);
							break;
						}
					}
				}
				if (gameIDs == null) {
					return;
				}
				for (Long l : gameIDs) {
					for (Game g : games) {
						if (g.getId() == l.longValue()) {
							gameNames.add(g.getName());
						}
					}
				}
				connection
						.sendTCP(new GameListReply(gameNames.toArray(new String[gameNames.size()]), gameIDs));
				// game join request
			} else if (content[0].equals("3")) {
				long gameID = Long.valueOf(content[1]);
				synchronized (games) {
					for (Game g : games) {
						if (g.getId() == gameID) {
							connection.sendTCP(g);
						}
					}
				}
			}
		}
	}
}
