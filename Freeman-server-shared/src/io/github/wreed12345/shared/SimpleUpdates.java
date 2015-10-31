package io.github.wreed12345.shared;

public class SimpleUpdates {
	/**Tells server that this seperates various strings */
	private static final String SEPERATOR = "!@!";
	/**When receiving will let server know this is a game update message*/
	private static final int SIMPLE_UPDATE = 1;
	
	/**Identifier for a Game List Request*/
	private static final int GAME_LIST_REQUEST = 2;
	
	/**
	 * Generates convenience method string for game list request
	 * @param player player to get the game list for
	 * @return string of the message to send to the server
	 */
	public static String gameListRequest(String player){
		return update(GAME_LIST_REQUEST, player);
	}
	
	/**Identifier for a game join request*/
	private static final int GAME_JOIN_REQUEST = 3;
	
	/**
	 * Generates a server request string for a game join request
	 * @param gameID ID of the game to join
	 * @return string of message to send to the server
	 */
	public static String gameJoinRequest(long gameID){
		return update(GAME_JOIN_REQUEST, String.valueOf(gameID));
	}
	
	/**
	 * Convenience method to help build update messages
	 * @param updateType Identifier of the update type
	 * @param content message content
	 * @return update string
	 */
	private static String update(int updateType, String content){
		return SIMPLE_UPDATE + SEPERATOR + updateType + SEPERATOR + content;
	}
	
}
