package service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.liferecords.application.HistoryData;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LocationServ extends Service implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	static final String TAG = LocationServ.class.getSimpleName();
	public static final String BROADCASTACTION = "Location";
	private static final int TIME = 1000 * 60 / 6; // type the minutes last
	public LocationManager locationManager;
	public Location previousBestLocation = null;
	LocationRequest locationRequest;
	LocationClient locationClient;
	Intent intent;
	HistoryData account;
	int counter = 0;

	@Override
	public void onCreate() {

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
		if(location == null){
			return;
		}
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		double accuracy = location.getAccuracy();
		account.setLatitude(latitude);
		account.setLongitude(longitude);
		account.setAccuracy(accuracy);
		intent = new Intent(BROADCASTACTION);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		
		
	}
}
