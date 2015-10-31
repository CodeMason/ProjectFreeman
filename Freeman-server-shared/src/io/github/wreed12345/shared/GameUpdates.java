package io.github.wreed12345.shared;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

/**
 * Compacts various updates into a string rather than an Object
 * @author William
 */
public class GameUpdates {
	
	/**Tells server that this separates various strings */
	private static final String SEPERATOR = "!@!";
	/**When receiving will let server know this is a game update message*/
	private static final int GAME_UPDATE = 0;
	
	/**Identifier for a chat message*/
	private static final int CHAT_MESSAGE = 1;
	/**Identifier for country loose health request*/
	private static final int COUNTRY_LOOSE_HEALTH_REQUEST = 2;
	
	/**
	 * Generates chat message string to send to server
	 * @param gameID ID of the game
	 * @param player player who is sending this
	 * @param message message to be sent
	 * @return string of a compacted chat message
	 */
	public static String chatMessage(long gameID, String player, String message){
		return update(CHAT_MESSAGE, gameID + SEPERATOR + player + SEPERATOR + message);
		//return GAME_UPDATE + SEPERATOR + gameID + SEPERATOR + player + SEPERATOR + player;
	}
	
	/**
	 * Generates a string to send country loose health request to server
	 * @param gameID ID of the game being played
	 * @param countryName the country to loose health
	 * @param amount the amount of damage that is beignd dealth. //move this to server calculation?
	 * @return string of a compacted country loose health message to send to the server
	 */
	public static String countryLooseHealth(long gameID, String countryName, int amount){
		return update(COUNTRY_LOOSE_HEALTH_REQUEST, gameID + SEPERATOR + countryName + SEPERATOR + amount);
	}
	
	/**
	 * Convenience method to help build update messages
	 * @param updateType Identifier of the update type
	 * @param content message content
	 * @return update string
	 */
	private static String update(int updateType, String content){
		return GAME_UPDATE + SEPERATOR + updateType + SEPERATOR + content;
	}
	
}