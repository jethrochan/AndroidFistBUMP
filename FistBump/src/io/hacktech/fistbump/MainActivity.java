package io.hacktech.fistbump;

import com.google.android.gms.common.ConnectionResult;
import io.hacktech.fistbump.controller.Geo;
import com.google.android.gms.common.GooglePlayServicesUtil;

import io.hacktech.fistbump.fragments.LocationFragment;
import io.hacktech.fistbump.usersetup.NotLoggedInLandingActivity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;


public class MainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalConstants.initialize();//initialize constants
		runLocationFragment();
		Geo.startGPS(this, new LocationListener(){

			@Override
			public void onLocationChanged(Location arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
				
			}
		});

	    SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
	    boolean loggedIn = sharedPrefs.getBoolean("loggedIn", false);
	    if (!loggedIn) {
	        Intent intent = new Intent(getBaseContext(), NotLoggedInLandingActivity.class);
			startActivity(intent);
	    } else {
			setContentView(R.layout.activity_main);
			bindFindUsersButton();
			bindFistBumpButton();
			if (readyToGo()) {
				if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
					getSupportFragmentManager().beginTransaction()
			        	.add(android.R.id.content, new LocationFragment()).commit();
			    }
			}
	    }
	}

	private void runLocationFragment() {
        getSupportFragmentManager().beginTransaction()
    		.add(android.R.id.content,
    				new LocationFragment()).commit();
	}
	
	private void bindFindUsersButton() {
		Button buttonOne = (Button)findViewById(R.id.find_users_btn);
		buttonOne.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), LocationActivity.class);
				startActivity(intent);
		    }
		});
	}

	private void bindFistBumpButton() {
		Button button = (Button)findViewById(R.id.bump_user_btn);
		button.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), NFCCommunication.class);
				startActivity(intent);
		    }
		});
	}

	private static int getMajorVersion(int glEsVersion) {
		return ((glEsVersion & 0xffff0000) >> 16);
	}
	  
	private static int getVersionFromPackageManager(Context context) {
		PackageManager packageManager = context.getPackageManager();
		FeatureInfo[] featureInfos = packageManager
				.getSystemAvailableFeatures();
		if (featureInfos != null && featureInfos.length > 0) {
			for (FeatureInfo featureInfo : featureInfos) {
				// Null feature name means this feature is the open
				// gl es version feature.
				if (featureInfo.name == null) {
					if (featureInfo.reqGlEsVersion != FeatureInfo.GL_ES_VERSION_UNDEFINED) {
						return getMajorVersion(featureInfo.reqGlEsVersion);
					} else {
						return 1; // Lack of property means OpenGL ES
									// version 1
					}
				}
			}
		}
		return 1;
	}
	  
	protected boolean readyToGo() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (status == ConnectionResult.SUCCESS) {
			if (getVersionFromPackageManager(this) >= 2) {
				return (true);
			} else {
				Toast.makeText(this, R.string.no_maps, Toast.LENGTH_LONG)
						.show();
				finish();
			}
		} else {
			Toast.makeText(this, R.string.no_maps, Toast.LENGTH_LONG).show();
			finish();
		}

		return (false);
	}
}
