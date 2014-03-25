package io.hacktech.fistbump.controller;

import io.hacktech.fistbump.GlobalConstants;
import io.hacktech.fistbump.model.UserPositionModel;

import java.util.List;

import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

public class RealtimeGeo {

	public void updateRealtimePosition(UserPositionModel position) {
		Firebase firebase = GlobalConstants.FIREBASE_ROOT.child("users")
				.child(position.usr).child("position");
		firebase.child("longitude").setValue(position.lon);
		firebase.child("latitude").setValue(position.lat);
	}

	public void startPositionFeed(List<String> users,
			List<ValueEventListener> listeners) {
		if (users.size() != listeners.size()) {
			(new Exception()).printStackTrace();
			return;
		}
		for (int i = 0; i != users.size(); i++) {
			Firebase b = GlobalConstants.FIREBASE_ROOT.child("users").child(
					users.get(i));
			b.addValueEventListener(listeners.get(i));
		}
	}
}
