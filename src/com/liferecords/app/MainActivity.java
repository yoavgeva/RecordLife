package com.liferecords.app;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.liferecords.app.MainFragment.Listener;
import com.liferecords.model.NavDrawerItem;
import com.liferecords.model.NavDrawerListAdapter;
import com.liferecords.service.MainService;
import com.parse.Parse;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity implements Listener {

	
	android.support.v7.app.ActionBar actionBar;
	public static final String CONNECTED_OR_NOT = "connected";
	String[] navMenuTitles;
	TypedArray navMenuIcons;
	
	private ListView listDrawer;
	private DrawerLayout drawerLayout;
	private NavDrawerListAdapter drawerAdapter;
	private ActionBarDrawerToggle drawerToggle;
	
	private ArrayList<NavDrawerItem> navDrawerItems;
	private int realPosition = 0;
	FragmentManager manager;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startMainService();
		setContentView(R.layout.activity_main);
		Parse.initialize(this, "eyqKhSsclg8b8tzuDn9CexsRhFTI3CQlKNKbZe8n",
				"OVA2i67H7LlNNcUQeZffztzWxTcJJmsxrKwRgaro");

		designActionBar();
		populateContent();
		populateNavDrawer();

		if (savedInstanceState == null) {
			checkGpsWorking();
		}

	}

	private void populateNavDrawer() {
		listDrawer = (ListView) findViewById(R.id.navList);
		designDrawerList(listDrawer);
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		addDrawerItems();
		setupDrawer();

	}

	private void designDrawerList(ListView listDrawer2) {
		listDrawer2.setBackgroundColor(Color.parseColor("#f5f5f5"));

	}

	private void setupDrawer() {
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.string.navigation_drawer_desc_open,
				R.string.navigation_drawer_desc_close) {
			@Override
			public void onDrawerOpened(View drawerView) {

				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle("Navigate");
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerClosed(View drawerView) {

				super.onDrawerClosed(drawerView);
				if (realPosition == 0) {
					setJournalName(actionBar);
				} else {
					actionBar.setTitle(navDrawerItems.get(realPosition)
							.getTitle());
				}
				// getSupportActionBar().setTitle(activityTitle);
				invalidateOptionsMenu();
			}
		};

		drawerToggle.setDrawerIndicatorEnabled(true);
		drawerLayout.setDrawerListener(drawerToggle);

	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	private void addDrawerItems() {
		navMenuTitles = getResources().getStringArray(R.array.navigation_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.navigation_icons);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		for (int i = 0; i < navMenuTitles.length; i++) {
			navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons
					.getResourceId(i, -1)));
		}

		drawerAdapter = new NavDrawerListAdapter(this, navDrawerItems);
		listDrawer.setAdapter(drawerAdapter);
		navMenuIcons.recycle();
		listDrawer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);

			}

		});
	}

	private void selectItem(int position) {

		Fragment fragment = null;

		switch (position) {
		case 0:
			fragment = new MainFragment();

			break;
		case 1:
			fragment = new SettingsFragment();
			break;
		case 2:
			fragment = new FeedbackFragment();
			break;
		case 3:
			fragment = new PrivacyFragment();
			break;
		case 4:
			logoutAction();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManag = getFragmentManager();
			fragmentManag.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
			listDrawer.setItemChecked(position, true);
			listDrawer.setSelection(position);

			realPosition = position;
			drawerLayout.closeDrawer(listDrawer);

		} else {
			Log.d(MainActivity.class.getSimpleName(),
					"Error in creating Fragment");
		}

	}

	private void populateContent() {
		manager = getFragmentManager();

		MainFragment mainFragy = new MainFragment();
		FragmentTransaction trans = manager.beginTransaction();

		trans.replace(R.id.content_frame, mainFragy, "main");
		trans.addToBackStack(null);
		trans.commit();
		/*
		 * getFragmentManager().beginTransaction() .add(R.id.main_frag, new
		 * MainFragment()).commit();
		 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * private void setNavigationBarActivity() {
	 * 
	 * navMenuTitles = getResources().getStringArray(R.array.navigation_items);
	 * 
	 * navMenuIcons = getResources().obtainTypedArray(R.array.navigation_icons);
	 * 
	 * Log.d(MainActivity.class.getSimpleName(), "" + navMenuTitles +
	 * navMenuIcons); // set(); }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//int id = item.getItemId();

		/*if (id == R.id.database_manager) {
			Intent dbmman = new Intent(MainActivity.this,
					AndroidDatabaseManager.class);
			startActivity(dbmman);
			return true;
		}*/
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	private void startMainService() {
		int loggedIn = checkIfStillLogged();
		Intent intent = new Intent(this, MainService.class);
		startService(intent);
		Log.d(MainActivity.class.getSimpleName(), "" + loggedIn);
		if (loggedIn == 0) {
			Log.d(MainActivity.class.getSimpleName(), "is calling mainservice");

			checkLogged(1);

		}

	}

	public void logoutAction() {
		ParseUser.logOut();
		checkLogged(0);
		stopMainService();
		updateNamePreference("");
		sendNotifiaction();
		Intent intent = new Intent(MainActivity.this,
				SignUpOrLoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);

	}
	private void updateNamePreference(String name) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingsFragment.KEY_EDIT_NAME_PREFERENCE, name);
		editor.commit();
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

	private void stopMainService() {
		Intent inte = new Intent(this, MainService.class);
		stopService(inte);
	}

	/*
	 * private void setDropDownDayOrWeek(MenuItem item) { View view =
	 * item.getActionView(); if (view instanceof Spinner) { Spinner spinner =
	 * (Spinner) view; ArrayList<String> itemList = new ArrayList<String>();
	 * itemList.add("Day"); itemList.add("Week");
	 * 
	 * ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(this,
	 * android.R.layout.simple_dropdown_item_1line, android.R.id.text1,
	 * itemList); spinner.setAdapter(dayAdapter);
	 * spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
	 * 
	 * @Override public void onItemSelected(AdapterView<?> parent, View view,
	 * int position, long id) { // TODO Auto-generated method stub if (position
	 * == 0) {
	 * 
	 * } else {
	 * 
	 * } }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> parent) { // TODO
	 * Auto-generated method stub
	 * 
	 * } }); }
	 * 
	 * }
	 */

	private void designActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1e88e5")));
		setJournalName(actionBar);
		actionBar.show();
	}

	private void setJournalName(ActionBar actionBar2) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String stra = pref.getString(SettingsFragment.KEY_EDIT_NAME_PREFERENCE,
				null);
		if (!TextUtils.isEmpty(stra)) {
			actionBar2.setTitle(stra  + "'s Journal");
		} else {

			actionBar2.setTitle(ParseUser.getCurrentUser().getUsername()
					+ "'s Journal");
		}

	}

	@Override
	public void onMenuLogOut() {
		logoutAction();

	}

	/*
	 * private void populateContent() { getFragmentManager().beginTransaction()
	 * .replace(android.R.id.content, new MainFragment()).commit(); }
	 */
	private void checkLogged(int connected) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(MainActivity.CONNECTED_OR_NOT, connected);
		editor.commit();

	}

	private int checkIfStillLogged() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		int logged = pref.getInt(CONNECTED_OR_NOT, 0);
		return logged;
	}

	private void sendNotifiaction(){
		
		Intent intent = new Intent(this, DispatchActivity.class);
		NotificationReceiver.notificatePush(this, 1, "LifeRecords", "Come back", "Every moment has a story in it, come back and store those moments.", intent);
	}

}
