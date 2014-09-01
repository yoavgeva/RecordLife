package com.liferecords.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.liferecords.model.HistoryData;

public class LocationServ extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	static final String TAG = LocationServ.class.getSimpleName();
	public static final String BROADCASTACTION = "Location";
	private static final int TIME = 1000 * 60 / 6; // type the minutes last
	LocationRequest locationRequest;
	LocationClient locationClient;
	Intent intent;
	HistoryData account;
	int counter = 0;

	@Override
	public void onCreate() {

		Context content = this;
		account = new HistoryData(content);
		locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(TIME);
		locationRequest.setFastestInterval(TIME);
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

	private void updateLocation(Location location){
		Log.d(TAG, "Location: " + location);
		if(location != null){
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			float accuracy = location.getAccuracy();
			account.updateGeo(latitude, longitude, accuracy);
			intent = new Intent(BROADCASTACTION);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		}
				
		
	}
	
}
