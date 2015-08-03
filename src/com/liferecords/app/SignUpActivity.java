package com.liferecords.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liferecords.tools.TextViewAnimator;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

	EditText userNameView, passwordView, passwordAgainView, emailView;
	TextView textWelcome;
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
		// emailView = (EditText) findViewById(R.id.edittext_signup_email);
		signUpButton = (Button) findViewById(R.id.button_signup);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkSignUpErrors();
				// checkForEmailErrors();
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
		// getActionBar().hide();
		setUserNameSignupDesign();
		setSignupButtonDesign();
		setPasswordDesign();
		setPasswordRepetDesign();
		setWelcomeDesign();
	}

	private void checkSignUpErrors() {
		boolean validationError = false;
		StringBuilder validationErrorMessage = new StringBuilder(getResources()
				.getString(R.string.error_intro));
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

	private void signUpParseUser() {
		final ProgressDialog progressDialogSignup = new ProgressDialog(
				SignUpActivity.this);
		progressDialogSignup.setTitle(R.string.progress_title);
		progressDialogSignup.setMessage("Signing to LifeRecords. Please wait");
		progressDialogSignup.show();

		ParseUser user = new ParseUser();
		user.setUsername(userNameView.getText().toString());
		user.setPassword(passwordView.getText().toString());
		// user.setEmail(emailView.getText().toString());
		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				progressDialogSignup.dismiss();
				if (e != null) {
					Toast.makeText(SignUpActivity.this, e.getMessage(),
							Toast.LENGTH_LONG).show();
				} else {

					saveCountIdPref();
					setLoggedInMainService(0);
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

	private void setUserNameSignupDesign() {
		EditText userNameText = (EditText) findViewById(R.id.edittext_signup_name);
		userNameText.setTextSize(25f);

		userNameText.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		userNameText.setHintTextColor(Color.parseColor("#FCFEFC"));
		userNameText.setTextColor(Color.parseColor("#FCFEFC"));
		userNameText.getBackground().setColorFilter(Color.WHITE,Mode.SRC_ATOP);

	}

	private void setSignupButtonDesign() {
		Button button = (Button) findViewById(R.id.button_signup);
		button.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		button.setTextSize(26f);
		button.setTextColor(Color.parseColor("#FCFEFC"));

	}

	private void setPasswordDesign() {
		EditText passwordText = (EditText) findViewById(R.id.edittext_signup_pw);
		passwordText.setTextSize(25f);
		passwordText.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		passwordText.setHintTextColor(Color.parseColor("#FCFEFC"));
		passwordText.setTextColor(Color.parseColor("#FCFEFC"));
		passwordText.getBackground().setColorFilter(Color.WHITE,Mode.SRC_ATOP);

	}

	private void setPasswordRepetDesign() {
		EditText passwordRepetText = (EditText) findViewById(R.id.edittext_signup_pw_repeat);
		passwordRepetText.setTextSize(25f);
		passwordRepetText.setTypeface(setTypeFaceRobotoCondones(),
				Typeface.NORMAL);
		passwordRepetText.setHintTextColor(Color.parseColor("#FCFEFC"));
		passwordRepetText.setTextColor(Color.parseColor("#FCFEFC"));
		passwordRepetText.getBackground().setColorFilter(Color.WHITE,Mode.SRC_ATOP);

	}

	private void setLoggedInMainService(int connected) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(MainActivity.CONNECTED_OR_NOT, connected);
		editor.commit();

	}

	private Typeface setTypeFaceRobotoCondones() {
		Typeface type = Typeface.createFromAsset(getAssets(),
				"robotocondensed_light.ttf");
		return type;
	}

	private void setWelcomeDesign() {
		textWelcome = (TextView) findViewById(R.id.welcome_text_signup);

		textWelcome.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		textWelcome.setTextSize(25f);
		textWelcome.setTextColor(Color.parseColor("#FCFEFC"));
		animateMottoText(textWelcome);

	}

	private void animateMottoText(TextView textWelcome) {

		Animation mottoTextViewAnim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in_left);
		textWelcome.startAnimation(mottoTextViewAnim);
		String text = getString(R.string.welcome_text_signup);
		TextViewAnimator animator = TextViewAnimator.newInstance(textWelcome,
				text, 70);
		animator.start();
	}

	/*
	 * // if register will use email than use it private boolean
	 * isValidEmailAddress(String email) { String ePattern =
	 * "/[A-Z0-9._%+-]+@[A-Z0-9.-]+.[A-Z]{2,4}$/i"; Pattern p =
	 * java.util.regex.Pattern.compile(ePattern); Matcher m = p.matcher(email);
	 * return m.matches(); }
	 */
	/*
	 * // if register will use email than use it private void
	 * checkForEmailErrors() { if
	 * (!isValidEmailAddress(emailView.getText().toString())) {
	 * Toast.makeText(SignUpActivity.this,
	 * getResources().getString(R.string.error_invalid_email),
	 * Toast.LENGTH_LONG).show(); return; }
	 * 
	 * }
	 */
}
