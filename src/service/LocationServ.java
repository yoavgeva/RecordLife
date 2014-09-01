package service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationServ extends Service implements LocationListener,GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener{

	public static final String BROADCASTACTION = "Location";
	private static final int TIME = 1000 * 60 / 6; // type the minutes last
	public LocationManager locationManager;
	public Location previousBestLocation = null;
	LocationRequest locationRequest;
	LocationClient locationClient;
	Intent intent;
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
	public IBinder onBind(Intent intent) {

		return null;
	}



	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

}
