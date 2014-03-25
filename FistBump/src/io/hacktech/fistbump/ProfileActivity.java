package io.hacktech.fistbump;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends BaseActivity {
	public static String[] fields = { "full_name", "phone_number", "favorite_color","looking_for", "favorite_joke" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		bindFinishedButton();
    }

	private void bindFinishedButton() {
		Button button = (Button)findViewById(R.id.finished_btn);
		button.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				startActivity(intent);
		    }
		});
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	saveProfile();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	restoreProfile();
    }
    
    private void saveProfile() {
        SharedPreferences settings = getSharedPreferences(BaseActivity.APP_SHARED_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        for (int i = 0; i < fields.length; ++i) {
        	String field = fields[i];
			try {
				int editTextNum = i + 1;
				int id = R.id.class.getField("editText" + editTextNum).getInt(0);
	        	EditText editText = (EditText)findViewById(id);
	            editor.putString(field, editText.getText().toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        editor.commit();
    }
    
    private void restoreProfile() {
		SharedPreferences savedProfile = getSharedPreferences(BaseActivity.APP_SHARED_PREFS, 0);

		for (int i = 0; i < fields.length; ++i) {
			String field = fields[i];
			try {
				String savedVal = savedProfile.getString(field, "");
				int editTextNum = i + 1;
				int id = R.id.class.getField("editText" + editTextNum).getInt(0);
				EditText editText = (EditText)findViewById(id);
				editText.setText(savedVal);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
}
