package io.hacktech.fistbump.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.hacktech.fistbump.SherlockMapFragment;
import io.hacktech.fistbump.model.UserPositionModel;

public class MapFragment extends SherlockMapFragment {
	private GoogleMap googleMap;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        googleMap = getMap();
        return root;
    }
    
	public void zoomToLocation(Location location) {
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
        .zoom(15)                   // Sets the zoom
        .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	public void panToLocation(Location loc) {
		LatLng latlng = new LatLng(loc.getLatitude(), loc.getLongitude());
		googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
	}
	
	public void addMarker(UserPositionModel p) {
		googleMap.addMarker(new MarkerOptions().position(new LatLng(p.lat, p.lon))
				.title(p.usr)
				.snippet(p.activity));
	}
}
