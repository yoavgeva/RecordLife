package com.liferecords.application;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

public class SignUpActivity extends Activity {

	EditText userNameView, passwordView, passwordAgainView;
	Button signUpButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		userNameView = (EditText) findViewById(R.id.edittext_signup_name);
		passwordAgainView = (EditText) findViewById(R.id.edittext_signup_pw_repeat);
		passwordView = (EditText) findViewById(R.id.edittext_signup_pw);
		signUpButton = (Button) findViewById(R.id.button_signup);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean validationError = false;
				StringBuilder validationErrorMessage = new StringBuilder(
						getResources().getString(R.string.error_intro));
				if (isEmpty(userNameView)) {
					validationError = true;
					validationErrorMessage.append(getResources().getString(
							R.string.error_blank_username));
				}
				if (isEmpty(passwordView)) {
					if (validationError) {
						validationErrorMessage.append(getResources().getString(
								R.string.error_join));
					}
					validationError = true;
					validationErrorMessage.append(getResources().getString(
							R.string.error_blank_password));
				}
				if (!isMatching(passwordView, passwordAgainView)) {
					if (validationError) {
						validationErrorMessage.append(getResources().getString(
								R.string.error_join));
					}
					validationError = true;
					validationErrorMessage.append(getResources().getString(
							R.string.error_mismatched_passwords));
				}
				validationErrorMessage.append(getResources().getString(
						R.string.error_end));
				if (validationError) {
					Toast.makeText(SignUpActivity.this, validationErrorMessage,
							Toast.LENGTH_LONG).show();
					return;
				}

				final ProgressDialog progressDialogSignup = new ProgressDialog(
						SignUpActivity.this);
				progressDialogSignup.setTitle(R.string.progress_title);
				progressDialogSignup.setMessage("Signing up. Please wait");
				progressDialogSignup.show();

				ParseUser user = new ParseUser();
				user.setUsername(userNameView.getText().toString());
				user.setPassword(passwordView.getText().toString());
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						progressDialogSignup.dismiss();
						if (e != null) {
							Toast.makeText(SignUpActivity.this, e.getMessage(),
									Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(SignUpActivity.this, DispatchActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);

						}
					}
				});

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean isMatching(EditText etText1, EditText etText2) {
		if (etText1.getText().toString().equals(etText2.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}
}
