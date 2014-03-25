package io.hacktech.fistbump;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BaseActivity extends SherlockFragmentActivity {
    public static final String APP_SHARED_PREFS = "app_preferences";
    public static final String[] pref_keys = { "loggedIn", "email_address", "password" };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home: {
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
				return true;
			}
			case R.id.profile: {
				Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
				startActivity(intent);
				return true;
			}
			case R.id.find_users: {
				Intent intent = new Intent(getBaseContext(), LocationActivity.class);
				startActivity(intent);
				return true;	
			}
			case R.id.help: {
				// Overload this to clear saved login information
		        SharedPreferences settings = getSharedPreferences(APP_SHARED_PREFS, 0);
		        SharedPreferences.Editor editor = settings.edit();
				editor.remove("loggedIn");
				editor.remove("email_address");
			    editor.remove("password");
				editor.commit();
				
				String url = "http://www.google.com";
				Intent openUrlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(openUrlIntent);
			}
			default: return super.onOptionsItemSelected(item);
		}
	}
}
