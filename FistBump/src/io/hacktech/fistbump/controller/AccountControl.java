package io.hacktech.fistbump.controller;

import io.hacktech.fistbump.GlobalConstants;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;

public class AccountControl {

	public static void checkLogin(Firebase ref,
			SimpleLoginAuthenticatedHandler handler, String email, String pwd) {
		SimpleLogin auth = new SimpleLogin(ref);
		auth.checkAuthStatus(handler);
	}

	public static void login(String email, String pwd,
			SimpleLoginAuthenticatedHandler handler) {
		GlobalConstants.SIMPLELOGIN.loginWithEmail(email, pwd, handler);
	}

	public static void register(String email, String pwd,
			SimpleLoginAuthenticatedHandler handler) {
		//GlobalConstants.initialize();
		GlobalConstants.SIMPLELOGIN.createUser(email, pwd, handler);
	}

	public static void logout() {
		GlobalConstants.SIMPLELOGIN.logout();
	}

}
