package com.liferecords.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.ActivityRecognition;

public class ActivityService extends Service implements ConnectionCallbacks,
		OnConnectionFailedListener {

	static final String TAG = ActivityService.class.getSimpleName();
	PendingIntent pendingInte;
	GoogleApiClient activityClientApi;
	private static final int TIME = 1000 * 60 * 5; // type the minutes last

	// long intervalTiming;

	@Override
	public void onCreate() {
		// loadTimingSettings();
		activityClientApi = new GoogleApiClient.Builder(this)
				.addApi(ActivityRecognition.API).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).build();
		Intent inte = new Intent(this, ActivityIntentService.class);
		pendingInte = PendingIntent.getService(this, 0, inte,
				PendingIntent.FLAG_UPDATE_CURRENT);
		activityClientApi.connect();
		// activityClient = new ActivityRecognitionClient(this, this, this);

		// activityClient.connect();
	}

	@Override
	public void onDestroy() {
		if (activityClientApi.isConnected()) {
			ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
					activityClientApi, pendingInte);
			// activityClient.removeActivityUpdates(pendingInte);
			activityClientApi.disconnect();
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
		Log.d(TAG, "intervalttiming = " + TIME);
		ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
				activityClientApi, TIME, pendingInte);

	}
/*
	@Override
	public void onDisconnected() {
		activityClient.removeActivityUpdates(pendingInte);
		stopSelf();

	}
*/
	/*
	 * private void loadTimingSettings() { SharedPreferences prefrences =
	 * PreferenceManager .getDefaultSharedPreferences(this); long interval =
	 * prefrences .getLong(SettingsFragment.KEY_INTERVAL_TIME, 30);
	 * intervalTiming = 1000 * 60 * interval; }
	 */

	@Override
	public void onConnectionSuspended(int cause) {
		ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(activityClientApi, pendingInte);
		stopSelf();

	}

}
