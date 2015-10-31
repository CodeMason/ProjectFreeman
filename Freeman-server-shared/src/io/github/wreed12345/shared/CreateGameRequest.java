package io.github.wreed12345.shared;

public class CreateGameRequest {
	public CreateGameRequest(){}
	
	private String gamename;
	private int moneyToStart;
	
	/**
	 * Creates a new CreateGame request. Eventually this will hold more than just a string
	 * @param gamename
	 */
	public CreateGameRequest (String gamename){
		this.gamename = gamename;
	}
	
	/**
	 * @return The requested game name
	 */
	public String getGameName(){
		return gamename;
	}
	
	/**
	 * @param moneyToStart Money that each user will start with *unimplemented*
	 */
	public void setMoneyToStart(int moneyToStart){
		this.moneyToStart = moneyToStart;
	}
	
	/**
	 * @return The amount of money each player starts with
	 */
	public int getMoneyToStart(){
		return moneyToStart;
	}
}
