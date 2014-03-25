package io.hacktech.fistbump;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class LocationActivity extends BaseActivity 
		implements GooglePlayServicesClient.ConnectionCallbacks, 
		GooglePlayServicesClient.OnConnectionFailedListener, Runnable {
	LocationClient client;
	Handler handler;
	private static final int REQUEST_ID = 1338;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		handler = new Handler();
	    client = new LocationClient(this, this, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		client.connect();
	}

	@Override
	public void onPause() {
		handler.removeCallbacks(this);
		client.disconnect();
		super.onPause();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		boolean anyLuck = false;

		if (result.hasResolution()) {
			try {
				result.startResolutionForResult(this, REQUEST_ID);
				anyLuck = true;
			} catch (IntentSender.SendIntentException e) {
				Log.e(getClass().getSimpleName(),
						"Exception trying to startResolutionForResult()", e);
			}
		}

		if (!anyLuck) {
			Toast.makeText(this, R.string.no_fused, Toast.LENGTH_LONG)
					.show();
			this.finish();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		run();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void run() {
		Location loc = client.getLastLocation();
		if (loc == null) {
			handler.postDelayed(this, 250);
		} else {
			Intent intent = new Intent(this.getBaseContext(),  
					SearchResultsActivity.class);
			startActivity(intent);
		}
	}
}
