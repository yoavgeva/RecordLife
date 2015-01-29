package com.liferecords.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

	EditText userNameView, passwordView, passwordAgainView;
	Button signUpButton;
	SharedPreferences sharedpref;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		setScreenDesign();
		userNameView = (EditText) findViewById(R.id.edittext_signup_name);
		passwordAgainView = (EditText) findViewById(R.id.edittext_signup_pw_repeat);
		passwordView = (EditText) findViewById(R.id.edittext_signup_pw);
		signUpButton = (Button) findViewById(R.id.button_signup);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkSignUpErrors();
				signUpParseUser();
				
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

	private boolean isMatching(EditText etText1, EditText etText2) {
		if (etText1.getText().toString().equals(etText2.getText().toString())) {
			return true;
		} else {
			return false;
		}
	}

	private void setScreenDesign() {
		getActionBar().hide();
		setUserNameSignupDesign();
		setSignupButtonDesign();
		setPasswordDesign();
		setPasswordRepetDesign();
	}
	
	



	private void checkSignUpErrors(){
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

	}
	private void signUpParseUser(){
		final ProgressDialog progressDialogSignup = new ProgressDialog(
				SignUpActivity.this);
		progressDialogSignup.setTitle(R.string.progress_title);
		progressDialogSignup
				.setMessage("Signing to LifeRecords. Please wait");
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
					saveCountIdPref();
					Intent intent = new Intent(SignUpActivity.this,
							DispatchActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();

				}
			}
		});

	}
	private void saveCountIdPref() {
		int countid = 1;
		sharedpref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedpref.edit();
		editor.putInt("countid", countid);
		editor.commit();
	}
	private Typeface setTypeFaceAspire(){
		Typeface type = Typeface.createFromAsset(getAssets(),
				"aspire-demibold.ttf");
		return type;
	}
	private void setUserNameSignupDesign(){
		EditText userNameText = (EditText) findViewById(R.id.edittext_signup_name);
		userNameText.setTextSize(25f);
		userNameText.setHintTextColor(Color.BLACK);
		
	}
	private void setSignupButtonDesign(){
		Button button = (Button) findViewById(R.id.button_signup);
		button.setTypeface(setTypeFaceAspire(), Typeface.BOLD);
		button.setTextSize(35f);

	}
	private void setPasswordDesign() {
		EditText passwordText = (EditText) findViewById(R.id.edittext_signup_pw);
		passwordText.setTextSize(25f);
		passwordText.setHintTextColor(Color.BLACK);
		
	}
	private void setPasswordRepetDesign() {
		EditText passwordRepetText = (EditText) findViewById(R.id.edittext_signup_pw_repeat);
		passwordRepetText.setTextSize(25f);
		passwordRepetText.setHintTextColor(Color.BLACK);
		
	}
}
