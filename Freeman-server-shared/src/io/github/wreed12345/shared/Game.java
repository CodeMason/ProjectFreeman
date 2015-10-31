package io.github.wreed12345.shared;

import io.github.wreed12345.shared.countries.Country;
import io.github.wreed12345.shared.placeable.Placeable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Game implements Serializable {
	//TODO: change to one line java docs + finish documenting
	
	public Game(){}
	
	private static final long serialVersionUID = -110217021907389225L;
	private static final int MINIMUM_SIZE = 40;
	private static final int MAXIMUM_SIZE = 60;

	private long id;
	private ArrayList<String> usernames = new ArrayList<String>();
	private String turn;
	private String name;
	private boolean joinable = true;
	private int health = 100;
	private ArrayList<String> messages = new ArrayList<String>();
	private ArrayList<Placeable> placeables = new ArrayList<Placeable>();
	private HashMap<String, Country> countries = new HashMap<String, Country>();
	private HashMap<String, Integer> playersMoney = new HashMap<String, Integer>(); 
	
	public Game(long id, String name) {
		this.id = id;
		this.name = name;
		addCountries();
		messages.add("Send messages to your friends & your enemies here!");
	}
	
	private void addCountries() {
//		countries.put("Water", new Country("Water", 255.0f, 255.0f, 255.0f,
	//			"I'm the most abundant liquid on the earth. hoorah"));
		countries.put("Eurano", new Country("Eurano", 229.0f, 158.0f, 255.0f, "Country desc...", 65));
		countries.put("Thardion", new Country("Thardion", 94.0f, 126.0f, 255.0f, "Country desc...", 75));
		countries.put("Ursas", new Country("Ursas", 255.0f, 229.0f, 114.0f, "Country desc...", 40));
		countries.put("Olir", new Country("Olir", 255.0f, 86.0f, 235.0f, "Country desc...", 50));
		countries.put("Itaia", new Country("Itaia", 255.0f, 104.0f, 154.0f, "Country desc...", 40));
		countries.put("Ethana", new Country("Ethana", 179.0f, 104.0f, 255.0f, "Country desc...", 50));
		countries.put("Anglum", new Country("Anglum", 255.0f, 176.0f, 173.0f, "Country desc...", 35));
		countries.put("Agricia", new Country("Agricia", 137.0f, 178.0f, 255.0f, "Country desc...", 30));
		countries.put("Austrius", new Country("Austris", 140.0f, 255.0f, 255.0f, "Country desc...", 10));
		countries.put("Romantano", new Country("Romantano", 255.0f, 176.0f, 107.0f, "Country desc...", 10));
		countries.put("Floreus", new Country("Floreus", 86.0f, 109.0f, 255.0f, "Country desc...", 45));
		countries.put("Ethion", new Country("Ethion", 255.0f, 76.0f, 183.0f, "Country desc...", 25));
		countries.put("Sibular", new Country("Sibalur", 255.0f, 162.0f, 48.0f, "Country desc...", 35));
		countries.put("Vincur", new Country("Vincur", 255.0f, 45.0f, 157.0f, "Country desc...", 85));
		countries.put("Castrius", new Country("Castrius", 58.0f, 255.0f, 111.0f, "Country desc...", 30));
		countries.put("Escartonia", new Country("Escartonia", 105.0f, 71.0f, 255.0f, "Country desc...", 70));
		countries.put("Ebia", new Country("Ebia", 195.0f, 255.0f, 155.0f, "Country desc...", 25));
		countries.put("Germalum", new Country("Germalum", 71.0f, 255.0f, 215.0f, "Country desc...", 55));
		countries.put("Indardia", new Country("Indardia", 114.0f, 255.0f, 102.0f, "Country desc...", 45));
		countries.put("Afrir", new Country("Afrir", 63.0f, 146.0f, 255.0f, "Country desc...", 45));
		countries.put("Vironea", new Country("Vironea", 79.0f, 111.0f, 255.0f, "Country desc...", 35));
		countries.put("Ordeos", new Country("Ordeos", 50.0f, 255.0f, 122.0f, "Country desc...", 45));
		countries.put("Egul", new Country("Egul", 243.0f, 114.0f, 255.0f, "Country desc...", 20));
		countries.put("Castinis", new Country("Castinis", 255.0f, 232.0f, 181.0f, "Country desc...", 40));
		countries.put("Ordennia", new Country("Ordennia", 255.0f, 153.0f, 45.0f, "Country desc...", 35));
		countries.put("Agrea", new Country("Agrea", 117.0f, 149.0f, 255.0f, "Country desc...", 55));
		countries.put("Crasea", new Country("Crasea", 255.0f, 0.0f, 144.0f, "Country desc...", 30));
		countries.put("Castratea", new Country("Castratea", 255.0f, 213.0f, 89.0f, "Country desc...", 55));
		countries.put("Arernaia", new Country("Arernaia", 140.0f, 255.0f, 203.0f, "Country desc...", 30));
		countries.put("Eurion", new Country("Eurion", 255.0f, 240.0f, 229.0f, "Country desc...", 80));
		countries.put("Austribon", new Country("Austribon", 255.0f, 203.0f, 181.0f, "Country desc...", 60));
		countries.put("Engis", new Country("Engis", 214.0f, 255.0f, 196.0f, "Country desc..", 35));
		countries.put("Olova", new Country("Olova", 255.0f, 119.0f, 158.0f, "Country desc...", 35));
		countries.put("Eurium", new Country("Eurium", 91.0f, 238.0f, 255.0f, "Country desc...", 25));
		countries.put("Trenova", new Country("Trenova", 255.0f, 157.0f, 112.0f, "Country desc...", 35));
		countries.put("Malycaia", new Country("Malycaia", 255.0f, 0.0f, 246.0f, "Country desc...", 50));
		countries.put("Maludini", new Country("Maludini", 255.0f, 158.0f, 89.0f, "Country desc...", 25));
		countries.put("Osova", new Country("Osova", 122.0f, 142.0f, 255.0f, "Country desc...", 50));
		countries.put("Ugaia", new Country("Ugaia", 255.0f, 175.0f, 38.0f, "Country desc...", 45));
		countries.put("Rhodeson", new Country("Rhodeson", 255.0f, 48.0f, 255.0f, "Country desc...", 40));
		countries.put("Tunosus", new Country("Tunosus", 76.0f, 150.0f, 255.0f, "Country desc...", 35));
		countries.put("Urontia", new Country("Urontia", 255.0f, 124.0f, 137.0f, "Country desc...", 40));
		countries.put("Afrano", new Country("Afrano", 127.0f, 0.0f, 0.0f, "Country desc...", 45));
		countries.put("Arum", new Country("Arum", 140.0f, 255.0f, 180.0f, "Country desc...", 20));
		countries.put("Ferrarcia", new Country("Ferrarcia", 255.0f, 38.0f, 10.0f, "Ferrarcia is a country rich in oil supplies. It is a generally flat and prone to attacks due to its accesible nature. It has open access to water making it a country dependent on water based trade.", 45));
		countries.put("Antovum", new Country("Antovum", 255.0f, 22.0f, 115.0f, "Country desc...", 55));
		countries.put("Uror", new Country("Uror", 119.0f, 255.0f, 241.0f, "Country desc...", 10));
		countries.put("Anium", new Country("Anium", 127.0f, 0.0f, 55.0f, "Country desc...", 20));
		countries.put("Engia", new Country("Engia", 132.0f, 183.0f, 255.0f, "Country desc...", 15));
		countries.put("Agrum", new Country("Agrum", 21.0f, 0.0f, 255.0f, "Country desc...", 40));
		countries.put("Aquis", new Country("Aquis", 255.0f, 84.0f, 0.0f, "Country desc...", 15));
		countries.put("Timonius", new Country("Timonius", 0.0f, 255.0f, 148.0f, "Country desc...", 20));
		countries.put("Ormia", new Country("Ormia", 163.0f, 255.0f, 227.0f, "Country desc...", 85));
		countries.put("Carthanon", new Country("Carthanon", 255.0f, 25.0f, 59.0f, "Country desc...", 15));
		
	}

	 /** @return The unique ID of this game*/
	public long getId() {
		return id;
	}

	/**
	 * @return the usernames in this game
	 */
	public ArrayList<String> getUsernames() {
		return usernames;
	}

	/**
	 * Sets the people in this game. May be wiser to user getUsernames().add();
	 * @param usernames
	 */
	public void setUsernames(ArrayList<String> usernames) {
		this.usernames = usernames;
	}
	
	/**
	 * @return The player whos turn it is
	 */
	public String getTurn() {
		return turn;
	}

	/**
	 * Sets whos turn in the game it is
	 * @param turn
	 */
	public void setTurn(String turn) {
		this.turn = turn;
	}

	/**
	 * @return The name of this game
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of this game
	 * @param name to be set to
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return true if the game is not in session
	 */
	public boolean isJoinable() {
		return joinable;
	}
	
	/**
	 * Sets the games join ability
	 * @param joinable True if it is not in session
	 */
	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}
	
	/**
	 * Adds a message to the array list
	 */
	public void addMessage(String message){
		if(messages.size() > 99){
			messages.remove(1);
		}
		messages.add(message);
	}
	
	/**
	 * @return the 100 most recent messages on the server
	 */
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	private static final int STARTING_MONEY = 2500000;
	
	/**adds a player to the Game and assigns them a random country*/
	public void addPlayer(String player){
		usernames.add(player);
		determineCountry(player);
		playersMoney.put(player, STARTING_MONEY);
	}
	
	/**Sets the country that the player should own*/
	private void determineCountry(String player){
		ArrayList<Country> countryValues = new ArrayList<Country>(countries.values());
		Collections.shuffle(countryValues, new Random());
		for(Country c : countryValues){
			if(c.getSize() > MINIMUM_SIZE && c.getSize() < MAXIMUM_SIZE && c.getOwner().equals("Unowned")){
				countries.get(c.getName()).setOwner(player);
				return;
			}
		}
	}

	
	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}
	
	public void addPlaceable(Placeable p){
		placeables.add(p);
	}

	public ArrayList<Placeable> getPlaceables() {
		return placeables;
	}

	public void setPlaceables(ArrayList<Placeable> placeables) {
		this.placeables = placeables;
	}

	public HashMap<String, Country> getCountries() {
		return countries;
	}

	public void setCountries(HashMap<String, Country> countries) {
		this.countries = countries;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public HashMap<String, Integer> getPlayersMoney() {
		return playersMoney;
	}

	public void setPlayersMoney(HashMap<String, Integer> playersMoney) {
		this.playersMoney = playersMoney;
	}

}
