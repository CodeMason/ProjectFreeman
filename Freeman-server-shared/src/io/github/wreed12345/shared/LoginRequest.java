package io.github.wreed12345.shared;

public class LoginRequest {
	private String username;
	private String password;
	
	public LoginRequest(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public LoginRequest(){
		//kryo gives error without
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
}
