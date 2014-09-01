package com.liferecords.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	EditText usernameText;
	EditText passwordText;
	Button signinButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setScreenDesign();
		usernameText = (EditText) findViewById(R.id.editText_username_login);
		passwordText = (EditText) findViewById(R.id.editText_password_login);
		signinButton = (Button) findViewById(R.id.button_loginscreen);
		signinButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkLoginErrors();
				loginParseUser();

			}
		});
	}

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private void setScreenDesign() {
		getActionBar().hide();
	}

	private void checkLoginErrors() {
		boolean validationError = false;
		StringBuilder validationErrorMessage = new StringBuilder(getResources()
				.getString(R.string.error_intro));
		if (isEmpty(usernameText)) {
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_username));
		}
		if (isEmpty(passwordText)) {
			if (validationError) {
				validationErrorMessage.append(getResources().getString(
						R.string.error_join));
			}
			validationError = true;
			validationErrorMessage.append(getResources().getString(
					R.string.error_blank_password));
		}
		validationErrorMessage.append(getResources().getString(
				R.string.error_end));

		if (validationError) {
			Toast.makeText(LoginActivity.this,
					validationErrorMessage.toString(), Toast.LENGTH_LONG)
					.show();
			return;
		}
	}

	private void loginParseUser() {
		final ProgressDialog pDlg = new ProgressDialog(LoginActivity.this);
		pDlg.setTitle(R.string.progress_title);
		pDlg.setMessage("Login to LifeRecords. Please Wait.");
		pDlg.show();

		ParseUser.logInInBackground(usernameText.getText().toString(),
				passwordText.getText().toString(), new LogInCallback() {

					@Override
					public void done(ParseUser user, ParseException e) {
						pDlg.dismiss();
						if (e != null) {
							Toast.makeText(LoginActivity.this, e.getMessage(),
									Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(LoginActivity.this,
									DispatchActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
							finish();
						}

					}
				});
	}
}
