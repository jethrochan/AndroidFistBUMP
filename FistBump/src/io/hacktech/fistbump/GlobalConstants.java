package io.hacktech.fistbump;

import java.util.HashMap;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.SimpleLogin;

public class GlobalConstants {
	public static String FIREBASE_URL = "https://sweltering-fire-1593.firebaseio.com";
	public static Firebase FIREBASE_ROOT;
	public static Firebase FIREBASE_AUTH;
	public static SimpleLogin SIMPLELOGIN;
	public static String SERVER_URL = "http://107.170.246.175";

	public static void initialize() {
		FIREBASE_ROOT = new Firebase(FIREBASE_URL);
		FIREBASE_AUTH = FIREBASE_ROOT.child("auth");
		SIMPLELOGIN = new SimpleLogin(FIREBASE_AUTH);
	}

	public static void main(String[] args) {
		//this resets everything
		Firebase fb = new Firebase(
				"https://sweltering-fire-1593.firebaseio.com");
		HashMap<String, String> root = new HashMap<String, String>();
		root.put("auth", "");
		fb.setValue(root);
	}
}
