package io.hacktech.fistbump;

import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.hacktech.fistbump.controller.Geo;
import io.hacktech.fistbump.fragments.MapFragment;
import io.hacktech.fistbump.model.UserPositionModel;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SearchResultsActivity extends BaseActivity 
		implements LocationListener, ConnectionCallbacks, 
		OnConnectionFailedListener {
	LocationClient client;
	Handler handler;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresults);
        
		client = new LocationClient(this, this, this);
		handler = new Handler();
    }

    @Override
    protected void onStop() {
        // If the client is connected
        if (client.isConnected()) {
            client.removeLocationUpdates(this);
        }
        client.disconnect();
        super.onStop();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	client.connect();
    }
    
    @Override
    protected void onPause() {
    	client.disconnect();
    	super.onPause();
    }
    
    private void zoomLocation(Location loc) {
		MapFragment map = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		map.zoomToLocation(loc);
    }
    
    private void panToLocation(Location loc) {
		MapFragment map = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		map.panToLocation(loc);
    }
    
    private void addMarker(final UserPositionModel model) {
		MapFragment map = (MapFragment)getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		map.addMarker(model);
    }

	@Override
	public void onLocationChanged(Location loc) {
		panToLocation(loc);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onConnected(Bundle arg0) {
		Location location = client.getLastLocation();
		zoomLocation(location);
		
        LocationRequest request = LocationRequest.create();
		client.requestLocationUpdates(request, this);
		
		Thread thr = new Thread(new UpdateMapTask(location));
		thr.start();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
	
	private class UpdateMapTask implements Runnable {
		private Location location;
		
		public UpdateMapTask(Location loc) {
			this.location = loc;
		}
		@Override
		public void run() {
			List<UserPositionModel> people = Geo.getNearbyFolks(location);
			for (UserPositionModel person : people) {
				final UserPositionModel p = person;
				handler.post(new Runnable() {
					@Override
					public void run() {
						addMarker(p);
					}
				});
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (client.isConnected()) {
				location = client.getLastLocation();
				run();
			}
		}
	}
}
