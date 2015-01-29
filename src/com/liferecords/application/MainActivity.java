 package com.liferecords.application;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.liferecords.model.DateAdapterItem;
import com.liferecords.model.Model;
import com.liferecords.service.MainService;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends Activity  {

	TextView textV;
	ActionBar actionBar;
	private List<DateAdapterItem> dateItems;
	private Model model;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "eyqKhSsclg8b8tzuDn9CexsRhFTI3CQlKNKbZe8n",
				"OVA2i67H7LlNNcUQeZffztzWxTcJJmsxrKwRgaro");
		startMainService();
		actionBar = getActionBar();
		designActionBar();
		//populate();
		
		
		if (savedInstanceState == null) {
			checkGpsWorking();
		}
		textV = (TextView) findViewById(R.id.textview_database);
		textV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent dbmman = new Intent(MainActivity.this, AndroidDatabaseManager.class);
				startActivity(dbmman);
				
			}
		});
		

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
		MenuItem mainMenuSpinnerDayOrWeek = menu.findItem(R.id.menu_main_spinner_day_or_week);
		setDropDownDayOrWeek(mainMenuSpinnerDayOrWeek);
		
		MenuItem datesSpinner = menu.findItem(R.id.menu_main_spinner_dates);
		setDropDownDates(datesSpinner);
		return super.onCreateOptionsMenu(menu);
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
	
	private void setDropDownDayOrWeek(MenuItem item){
		View view = item.getActionView();
		if(view instanceof Spinner){
			Spinner spinner = (Spinner) view;
			ArrayList<String> itemList = new ArrayList<String>();
			itemList.add("Day");
			itemList.add("Week");
			ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, android.R.id.text1,itemList);
			spinner.setAdapter(dayAdapter);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if(position == 0){
						
					} else {
						
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
	}
	
	private void setDropDownDates(MenuItem item){
	
	}

	
	private void designActionBar(){
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.color.transparent);
	}
	
/*	private void populate(){
		this.dateItems = this.model.getDateAdapterItems();
	}*/
}
