package io.github.wreed12345.shared;

import java.io.Serializable;
import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;

public class Player implements Serializable {

	private static final long serialVersionUID = 6933670152252281282L;

	private String name;
	private String password;
	private ArrayList<Long> gameIDs = new ArrayList<Long>();
	private transient Connection connection;

	/**
	 * creates a new player 
	 * @param name Username of the player
	 * @param password Password of the player
	 */
	public Player(String name, String password) {
		this.name = name;
		this.password = password;
	}

	/**
	 * @return the name of the player
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Sets the name of the player
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return amount of games the player is in
	 */
	public int getAmountOfGames() {
		return gameIDs.size();
	}

	/**
	 * @return players password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the users password
	 * @param password password to be set to
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return ArrayList of game IDS the player is in
	 */
	public ArrayList<Long> getGameIDs() {
		return gameIDs;
	}

	/**
	 * Sets the game IDs of the player
	 * @param gameIDs that the player is in
	 */
	public void setGameIDs(ArrayList<Long> gameIDs) {
		this.gameIDs = gameIDs;
	}
	
	/**
	 * @return The connection of the player
	 */
	public Connection getConnection(){
		return connection;
	}
	
	/**
	 *Sets the connection of the player
	 * @param connection Connection to be set to
	 */
	public void setConnection(Connection connection){
		this.connection = connection;
	}

}