package io.github.wreed12345.shared;

import java.util.HashMap;

public class GameListReply {
	/**
	 * Empty constructor needed for kryo casting
	 */
	public GameListReply(){};
	
	//contains Game Name , game id
	private HashMap<String, Long> games = new HashMap<String, Long>();
	
	/**
	 * New GameListReply to GameListRequest
	 * @param gameNames The names of the game
	 * @param gameIDs The id's of the { gameNames}
	 */
	public GameListReply(String[] gameNames, Long[] gameIDs){
		for(int i = 0; i < gameNames.length; i ++){
			games.put(gameNames[i], gameIDs[i]);
		}
	}
	
	/**
	 * @return Game player is involved in <Game Name, Game ID> Format.
 	 */
	public HashMap<String, Long> getGames(){
		return games;
	}
}
