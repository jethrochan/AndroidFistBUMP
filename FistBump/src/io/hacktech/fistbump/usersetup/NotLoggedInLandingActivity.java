package io.hacktech.fistbump.usersetup;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import io.hacktech.fistbump.R;

public class NotLoggedInLandingActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notloggedinlanding);
		
		bindLoginButton();
		bindRegistrationButton();
	}

	private void bindLoginButton() {
		Button button = (Button)findViewById(R.id.login_btn);
		button.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), LoginActivity.class);
				startActivity(intent);
		    }
		});
	}
	
	private void bindRegistrationButton() {
		Button button = (Button)findViewById(R.id.register_btn);
		button.setOnClickListener(new Button.OnClickListener() {
		    public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(), RegistrationActivity.class);
				startActivity(intent);
		    }
		});
	}
}
