package com.liferecords.application;

import service.MainService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseUser;

import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "eyqKhSsclg8b8tzuDn9CexsRhFTI3CQlKNKbZe8n",
				"OVA2i67H7LlNNcUQeZffztzWxTcJJmsxrKwRgaro");
		startMainService();
		if (savedInstanceState == null) {
			checkGpsWorking();
		}

	}

	private void checkGpsWorking() {
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean isEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		isEnabled |= locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (!isEnabled) {
			dialogLocationNotWorking();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		if (id == R.id.action_logout) {
			logoutAction();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void startMainService() {
		Intent intent = new Intent(this, MainService.class);
		startService(intent);
	}

	private void logoutAction() {
		ParseUser.logOut();
		stopMainService();
		Intent intent = new Intent(MainActivity.this,
				SignUpOrLoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void dialogLocationNotWorking() {
		AlertDialog.Builder locationDialog = new AlertDialog.Builder(this);
		locationDialog.setTitle(R.string.location_alert_dialog_title);
		locationDialog.setMessage(R.string.location_alert_dialog_message);
		locationDialog.setPositiveButton(
				R.string.location_alert_dialog_positive,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(intent);
					}
				});
		locationDialog.setNegativeButton(
				R.string.location_alert_dialog_negative,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		locationDialog.show();
	}
	
	private void stopMainService(){
		Intent inte = new Intent(this, MainService.class);
		stopService(inte);
	}
}
