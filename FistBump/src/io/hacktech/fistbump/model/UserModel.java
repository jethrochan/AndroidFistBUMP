package io.hacktech.fistbump.model;

import java.util.HashMap;

@SuppressWarnings("serial")
public class UserModel extends HashMap<String, String> {

	public void setEmail(String email) {
		this.put("email", email);
	}

	public void setPwd(String pwd) {
		this.put("pwd", pwd);
	}

	public String getEmail() {
		return this.get("email");
	}

	public String getPwd() {
		return this.get("pwd");
	}
	
	public static void main(String[] args){
		
	System.out.println("test");
	}
}
