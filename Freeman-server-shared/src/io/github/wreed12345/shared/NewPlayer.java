package io.github.wreed12345.shared;

public class NewPlayer {

	private String requestedName, password;
	
	public NewPlayer(String requestedName, String password){
		this.requestedName = requestedName;
		this.password = password;
	}
	
	public NewPlayer(){
	}

	public String getRequestedName() {
		return requestedName;
	}

	public void setRequestedName(String requestedName) {
		this.requestedName = requestedName;
	}

	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
