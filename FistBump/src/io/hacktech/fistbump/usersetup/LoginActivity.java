package io.hacktech.fistbump.usersetup;

import com.actionbarsherlock.app.SherlockActivity;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;
import com.firebase.simplelogin.User;

import io.hacktech.fistbump.BaseActivity;
import io.hacktech.fistbump.GlobalConstants;
import io.hacktech.fistbump.MainActivity;
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

public class LoginActivity extends SherlockActivity {
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		bindLoginButton();
		GlobalConstants.initialize();
		this.bindLoginButton();
	}
	
	private void bindLoginButton() {
		final LoginActivity me = this;
		final Button login_btn = (Button) findViewById(R.id.login_btn);
		
		// login
		login_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				login_btn.setEnabled(false);
				final String email = ((EditText) findViewById(R.id.email_address))
						.getText().toString();
				final String pwd = ((EditText) findViewById(R.id.password))
						.getText().toString();
				AccountControl.login(email, pwd,
						new SimpleLoginAuthenticatedHandler() {
							@Override
							public void authenticated(
									com.firebase.simplelogin.enums.Error error,
									User user) {
								if (error != null) {
									String error_msg;
									switch (error) {
									case InvalidEmail:
										error_msg = "Invalid email address";
										break;
									case UserDoesNotExist:
										error_msg = "Email address not found";
										break;
									case InvalidPassword:
										error_msg = "Entered password was incorrect";
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
									login_btn.setEnabled(true);
								} else {
								    SharedPreferences settings  = getApplicationContext().getSharedPreferences(BaseActivity.APP_SHARED_PREFS, Context.MODE_PRIVATE);
								    SharedPreferences.Editor editor = settings.edit();
								    editor.putBoolean("loggedIn", true).commit();
								    editor.putString("email_address", email);
								    editor.putString("password", pwd);
								    editor.commit();
									Intent intent = new Intent(getBaseContext(), MainActivity.class);
									startActivity(intent);
								}
							}
						});
			}
		});
	}
}
