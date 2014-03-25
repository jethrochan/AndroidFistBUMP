package io.hacktech.fistbump.usersetup;

import io.hacktech.fistbump.BaseActivity;
import io.hacktech.fistbump.ProfileActivity;
import io.hacktech.fistbump.R;
import io.hacktech.fistbump.controller.AccountControl;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;

public class RegistrationActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		
		bindNextButton();
	}
	
	private void bindNextButton() {
        final RegistrationActivity me = this;
		final ImageButton button = (ImageButton) findViewById(R.id.next_btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				button.setEnabled(false);
				final String emailAddress = ((EditText)findViewById(R.id.email_address)).getText()
						.toString();
				final String password = ((EditText) findViewById(R.id.password)).getText()
						.toString();
				AccountControl.register(emailAddress, password,
						new SimpleLoginAuthenticatedHandler() {
							public void authenticated(
									com.firebase.simplelogin.enums.Error error,
									User user) {
								if (error != null) {
									String error_msg;
									switch (error) {
									case EmailTaken:
										error_msg = "Email already taken";
										break;
									case PermissionDenied:
										error_msg = "App does not have internet privileges.";
										break;
									default:
										error_msg = "Connection error";
										break;
									}
									AlertDialog dialog = new AlertDialog.Builder(
											me).setMessage(error_msg)
											.setTitle("Failure!")
											.setPositiveButton("Ok", null)
											.create();
									dialog.show();
									button.setEnabled(true);
								} else {
									SharedPreferences settings  = getApplicationContext().getSharedPreferences(BaseActivity.APP_SHARED_PREFS, Context.MODE_PRIVATE);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putBoolean("loggedIn", true).commit();
								    editor.putString("email_address", emailAddress);
								    editor.putString("password", password);
								    editor.commit();
									Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
									startActivity(intent);
								}
							}
						});
			}
		});
	}
}
