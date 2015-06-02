package com.liferecords.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.liferecords.application.SettingsFragment;
import com.liferecords.model.Model;

public class LocationServ extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	static final String TAG = LocationServ.class.getSimpleName();
	public static final String BROADCASTACTION = "com.liferecords.service."
			+ LocationServ.class.getSimpleName() + ".BROADCAST";
	//private static final int TIME = 1000 * 60 * 30; // type the minutes last
	LocationRequest locationRequest;
	LocationClient locationClient;
	Intent intent;
	Model model;
	int counter = 0;
	long intervalTiming = 1000 * 60 * 30;

	@Override
	public void onCreate() {

		Context content = this;
		model = new Model(content);
		loadTimingSettings();
		Log.d(TAG, "intervalttiming = " + intervalTiming);
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(intervalTiming);
		locationRequest.setFastestInterval(intervalTiming);
		locationClient = new LocationClient(this, this, this);
		locationClient.connect();

	}

	@Override
	public void onDestroy() {
		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
			locationClient.disconnect();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLocationChanged(Location arg0) {
		updateLocation(arg0);
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		stopSelf();
	}

	@Override
	public void onConnected(Bundle arg0) {
		locationClient.requestLocationUpdates(locationRequest, this);
	}

	@Override
	public void onDisconnected() {
		locationClient.removeLocationUpdates(this);
		stopSelf();
	}

	private void updateLocation(Location location) {
		Log.d(TAG, "Location: " + location);
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			float accuracy = location.getAccuracy();
			model.account.data.updateGeo(latitude, longitude, accuracy);
			Log.d(TAG, "latitude: " + model.account.data.getLatitude()
					+ " longitude: " + model.account.data.getLongitude());
			intent = new Intent(BROADCASTACTION);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}

	}

	private void loadTimingSettings() {
		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(this);
		int interval = prefrences
				.getInt(SettingsFragment.KEY_INTERVAL_TIME, 30);
		intervalTiming = 1000 * 60 * interval;
	}

}
