package io.hacktech.fistbump.controller;

import io.hacktech.fistbump.BaseActivity;
import io.hacktech.fistbump.ProfileActivity;
import io.hacktech.fistbump.model.UserPositionModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class Geo {
	public static HttpResponse postData(String url, List<NameValuePair> data) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		HttpResponse response = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(data));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return response;
	}

	public static HttpResponse getData(String url) {
		HttpResponse response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			response = client.execute(request);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	public static StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return full string
		return total;
	}

	public static void pingGeoServer(Activity activity, Location loc) {
		SharedPreferences usrpref = activity.getSharedPreferences(
				BaseActivity.APP_SHARED_PREFS, 0);

		String email = usrpref.getString("email_address", "");
		String looking_for = usrpref.getString("looking_for", "");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("usr", email));
		nameValuePairs.add(new BasicNameValuePair("activity", looking_for));
		nameValuePairs.add(new BasicNameValuePair("lon", ""+loc.getLongitude()));
		nameValuePairs
				.add(new BasicNameValuePair("lat", "" + loc.getLatitude()));
		HttpResponse resp = Geo.postData("http://107.170.246.175/geo.php",
				nameValuePairs);
		// Geo.inputStreamToString(resp.getEntity().getContent());
	}
	
	public static void pingGeoServer(Activity activity) {
		SharedPreferences usrpref = activity.getSharedPreferences(
				BaseActivity.APP_SHARED_PREFS, 0);

		String email = usrpref.getString("email_address", "");
		String looking_for = usrpref.getString("looking_for", "");
		Location loc = Geo.getGPSCoord(activity);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("usr", email));
		nameValuePairs.add(new BasicNameValuePair("activity", looking_for));
		nameValuePairs.add(new BasicNameValuePair("lon", ""+loc.getLongitude()));
		nameValuePairs
				.add(new BasicNameValuePair("lat", "" + loc.getLatitude()));
		HttpResponse resp = Geo.postData("http://107.170.246.175/geo.php",
				nameValuePairs);
		// Geo.inputStreamToString(resp.getEntity().getContent());
	}

	public static List<UserPositionModel> getNearbyFolks(Location loc) {
		HttpResponse resp = Geo.getData("http://107.170.246.175/geo.php?lon="
				+ loc.getLongitude() + "&lat=" + loc.getLatitude());
		String str = null;
		try {
			str = Geo.inputStreamToString(resp.getEntity().getContent()).toString();
			Log.i("HttpResponse", str);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<UserPositionModel> list = (new Gson()).fromJson(str,
				(new TypeToken<List<UserPositionModel>>(){}).getType());
		return list;
	}

	public static Location getGPSCoord(Activity activity) {
		LocationManager loc_mgr = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = loc_mgr.getBestProvider(criteria, false);
		Location location = loc_mgr.getLastKnownLocation(provider);
		return location;
	}

	public static void startGPS(Activity activity, LocationListener loc_listener) {
		LocationManager service = (LocationManager) activity
				.getSystemService(Context.LOCATION_SERVICE);
		service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1000,
				loc_listener);
		boolean enabled = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to
		// go to the settings
		if (!enabled) {
			Intent intent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			activity.startActivity(intent);
		}
	}

	public static double getDistanceFromLatLonInKm(double lat1, double lon1,
			double lat2, double lon2) {
		double R = 6371.137f; // Radius of the earth in km
		double dLat = Geo.deg2rad(lat2 - lat1); // deg2rad below
		double dLon = Geo.deg2rad(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = R * c; // Distance in km
		return d;
	}

	public static double deg2rad(double deg) {
		return deg * (Math.PI / 180);
	}
}
