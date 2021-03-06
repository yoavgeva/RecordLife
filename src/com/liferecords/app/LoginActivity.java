package com.liferecords.app;
//Must!!!! check for problem if get countid = 1 if old user enter new device can happen in db and we don't want to see it
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liferecords.db.DataDBAdapter;
import com.liferecords.model.Account;
import com.liferecords.model.PostObjectsParse;
import com.liferecords.tools.TextViewAnimator;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends Activity {
	EditText usernameText;
	EditText passwordText;
	Button signinButton;
	Account account;
	SharedPreferences sharedpref;
	SharedPreferences.Editor editor;
	private static ArrayList<ParseObject> allObjects;
	DataDBAdapter helper;
	private TextView textWelcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setScreenDesign();
		account = new Account(this);
		helper = new DataDBAdapter(this);

		usernameText = (EditText) findViewById(R.id.edittext_username_login);
		passwordText = (EditText) findViewById(R.id.edittext_password_login);
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
		//getActionBar().hide();
		setLoginButton();
		setUserNameText();
		setPasswordTtext();
		setWelcomeDesign();
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

					final int countNum = checkUserExistInDB(user);
					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("HistoryParse");
					query.whereEqualTo("user",
							ParseUser.getCurrentUser());
					query.whereGreaterThan("countid", countNum);
					query.orderByDescending("countid");
					query.getFirstInBackground(new GetCallback<ParseObject>() {

						@Override
						public void done(ParseObject object,
								ParseException e) {
							if (object == null) {
								Log.d("error in getfirstinbackground",
										e.getMessage());
								saveCountIdPref(countNum);
							} else {
								int countParse = object
										.getInt("countid");
								Log.d("countParse", "countparse: "
										+ countParse);
								saveCountIdPref(countParse);
								queryAllObjects(countParse, countNum);

							}

						}
					});
					setLoggedInMainService(0);
					goToDispatchActivity();

				}

			}
		});
	}

	private int checkUserExistInDB(ParseUser user) {
		int countId = account.getCountIdOfUser(user);
		return countId;
	}

	private void goToDispatchActivity() {
		
		Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	private void saveCountIdPref(int countNum) {
		int countid = countNum + 1;
		sharedpref = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedpref.edit();
		editor.putInt("countid", countid);
		editor.commit();
	}

	private int caculateNumberOfObjects(int countParse, int countNum) {
		return countParse - countNum;
	}

	private void queryAllObjects(int countParse, int countNum) {
		allObjects = new ArrayList<ParseObject>(caculateNumberOfObjects(countParse,
				countNum));
		int numOfQueries = (int) Math.ceil(caculateNumberOfObjects(countParse,
				countNum) / 1000.0);

		for (int i = 0; i < numOfQueries; i++) {
			ParseQuery<ParseObject> query = ParseQuery.getQuery("HistoryParse");
			query.whereEqualTo("user", ParseUser.getCurrentUser());
			query.whereGreaterThan("countid", countNum);
			query.orderByDescending("countid");
			query.setLimit(1000);
			query.setSkip(i * 1000);
			query.findInBackground(new FindCallback<ParseObject>() {

				@Override
				public void done(List<ParseObject> objects, ParseException e) {
					if (e != null) {
						Log.d("queryNot", e.getMessage());
					} else {
						Log.d("queryworking", "size: " + objects.size());
						allObjects.addAll(objects);
						Log.d("queryworking",
								"size all objects: " + allObjects.size());

						Thread myThread = new Thread(new MyThread());
						myThread.start();


					}

				}
			});
		}


	}

	private void addObjectsToDb(ArrayList<ParseObject> allObjects) {

		for (int i = 0; i < allObjects.size(); i++) {

			PostObjectsParse object = (PostObjectsParse) allObjects.get(i);


			helper.insertData(object.getLatitude(), object.getLongitude(),
					object.getAccuracy(), object.getAddress(), object
					.getBatteryCharge(), object.getBatteryPrec(), object.getMotion(),
					object.getPivotLatitude(), object .getPivotLongitude(),
					object.getPivotAccuracy(), object.getCountId(),
					object.getDateString(), ParseUser.getCurrentUser().getUsername(),object.getType(),object.getDateWithoutTime());


		}
	}
	private  class MyThread implements Runnable {

		@Override
		public void run() {
			addObjectsToDb(allObjects);

		}

	}

	private void setLoginButton() {
		Button button = (Button) findViewById(R.id.button_loginscreen);
		button.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		button.setTextSize(26f);
		button.setTextColor(Color.parseColor("#FCFEFC"));

	}

	private Typeface setTypeFaceRobotoCondones(){
		Typeface type = Typeface.createFromAsset(getAssets(),
				"robotocondensed_light.ttf");
		return type;
	}
	
	
	private void setUserNameText(){
		EditText userNameText = (EditText) findViewById(R.id.edittext_username_login);
		userNameText.setTextSize(30f);
		userNameText.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		userNameText.setHintTextColor(Color.parseColor("#FCFEFC"));
		userNameText.setTextColor(Color.parseColor("#FCFEFC"));
		userNameText.getBackground().setColorFilter(Color.WHITE,Mode.SRC_ATOP);
		
		
	}
	private void setPasswordTtext(){
		EditText userNameText = (EditText) findViewById(R.id.edittext_password_login);
		userNameText.setTextSize(30f);
		userNameText.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		userNameText.setHintTextColor(Color.parseColor("#FCFEFC"));
		userNameText.setTextColor(Color.parseColor("#FCFEFC"));
		userNameText.getBackground().setColorFilter(Color.WHITE,Mode.SRC_ATOP);
		
		
		
	}
	
	private void setLoggedInMainService(int connected){
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(MainActivity.CONNECTED_OR_NOT, connected);
		editor.commit();

	}
	private void setWelcomeDesign() {
		textWelcome = (TextView)findViewById(R.id.welcome_text_login);

		textWelcome.setTypeface(setTypeFaceRobotoCondones(), Typeface.NORMAL);
		textWelcome.setTextSize(25f);
		textWelcome.setTextColor(Color.parseColor("#FCFEFC"));
		animateMottoText(textWelcome);

	}
	
	private void animateMottoText(TextView textWelcome) {
		
		Animation mottoTextViewAnim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in_left);
		textWelcome.startAnimation(mottoTextViewAnim);
		String text = getString(R.string.welcome_text_login);
		TextViewAnimator animator = TextViewAnimator.newInstance(textWelcome,
				text, 70);
		animator.start();
	}
	

}
