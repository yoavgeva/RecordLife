package com.liferecords.service;

import com.liferecords.model.HistoryData;
import com.liferecords.model.PostObjectsParse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.p2p.WifiP2pManager.ServiceResponseListener;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MainService extends Service {
	static final String TAG = MainService.class.getSimpleName();
	private PostObjectsParse postObjects;
	private HistoryData account;

	private static final int TIME = 1000 * 60 / 6; // type the minutes last
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, intent.getAction());
			if (intent.getAction().equals(LocationServ.BROADCASTACTION)) {
			}
			if (intent.getAction().equals(ActivityIntentService.ACTION)) {

			}
		}
	};

	private ServiceConnection syncServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			SyncService.LocalBinder binder = (SyncService.LocalBinder) service;
			SyncService syncService = binder.getService();
			syncService.stop = true;
			unbindService(syncServiceConnection);

		}
	};

	private Handler timerHandler = new Handler();
	private Runnable timerTask = new Runnable() {

		@Override
		public void run() {
			timerHandler.postDelayed(timerTask, TIME);

		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		registerReciver();
		startServices();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		stopTimer();
		unregisterReciver();
		stopServices();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		sync();
		startTimer();
		return START_STICKY;
	}

	private void sync() {
		startSyncService();

	}

	private void startSyncService() {
		startService(new Intent(this, SyncService.class));

	}

	private void registerReciver() {
		IntentFilter filter = new IntentFilter(LocationServ.BROADCASTACTION);
		LocalBroadcastManager broadcastManager = LocalBroadcastManager
				.getInstance(this);
		broadcastManager.registerReceiver(receiver, filter);
		filter = new IntentFilter(ActivityIntentService.ACTION);
		broadcastManager.registerReceiver(receiver, filter);

	}

	private void unregisterReciver() {
		LocalBroadcastManager broadcastManager = LocalBroadcastManager
				.getInstance(this);
		broadcastManager.unregisterReceiver(receiver);
	}

	private void startTimer() {
		stopTimer();
		timerHandler.postDelayed(timerTask, TIME);

	}

	private void stopTimer() {
		timerHandler.removeCallbacks(timerTask);
	}

	private void startServices() {
		startService(new Intent(this, LocationServ.class));
		startService(new Intent(this, ActivityService.class));
	}

	private void stopServices() {
		stopService(new Intent(this, LocationServ.class));
		stopService(new Intent(this, ActivityService.class));
		stopSyncService();
	}

	private void stopSyncService() {
		if (!isSyncServiceRunning()) {
			return;
		}
		Intent inte = new Intent(this, SyncService.class);
		bindService(inte, syncServiceConnection, Context.BIND_AUTO_CREATE);

	}

	private boolean isSyncServiceRunning() {
		String uri = SyncService.class.getName();
		boolean isSyncServiceRun = isServiceRun(uri);
		return isSyncServiceRun;
	}

	private boolean isServiceRun(String uri) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(uri)) {
				return true;
			}
		}
		return false;
	}

	private void postDataToParse() {
		postObjects.setLatitude(account.getLatitude());
		postObjects.setLongitude(account.getLongitude());
		postObjects.setAccuracy(account.getAccuracy());

		ParseACL acl = new ParseACL();
		acl.setReadAccess(ParseUser.getCurrentUser(), true);
		acl.setWriteAccess(ParseUser.getCurrentUser(), true);
		postObjects.setACL(acl);
		postObjects.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub

			}
		});
	}
}
