package com.liferecords.service;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.ActivityRecognitionClient;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class ActivityService extends Service implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	static final String TAG = ActivityService.class.getSimpleName();
	PendingIntent pendingInte;
	ActivityRecognitionClient activityClient;
	private static final int TIME = 1000 * 60 / 6; // type the minutes last

	@Override
	public void onCreate() {
		activityClient = new ActivityRecognitionClient(this, this, this);
		Intent inte = new Intent(this, ActivityIntentService.class);
		pendingInte = PendingIntent.getService(this, 0, inte,
				PendingIntent.FLAG_UPDATE_CURRENT);
		activityClient.connect();
	}

	@Override
	public void onDestroy() {
		if (activityClient.isConnected()) {
			activityClient.removeActivityUpdates(pendingInte);
			activityClient.disconnect();
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		stopSelf();

	}

	@Override
	public void onConnected(Bundle arg0) {
		activityClient.requestActivityUpdates(TIME, pendingInte);

	}

	@Override
	public void onDisconnected() {
		activityClient.removeActivityUpdates(pendingInte);
		stopSelf();

	}

}
