package com.liferecords.service;

import com.liferecords.application.SettingsFragment;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MainService extends Service {
	static final String TAG = MainService.class.getSimpleName();
	private final IBinder settingsBinder = new LocalBinder();

	
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

	long intervalTiming;
	private Handler timerHandler = new Handler();
	private Runnable timerTask = new Runnable() {

		@Override
		public void run() {
			Log.d(TAG, "intervalttiming = " + intervalTiming);
			sync();
			timerHandler.postDelayed(timerTask, intervalTiming);

		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return settingsBinder;
	}
	
	public class LocalBinder extends Binder{
		public MainService getMainService(){
			return MainService.this;
		}
	}

	@Override
	public void onCreate() {
		loadTimingSettings();
		registerReciver();
		startServices();
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		stopTimer();
		unregisterReciver();
		stopServices();
		Log.d(TAG, "Destoryedservice" + intervalTiming);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand running");
		Log.d(TAG, "intervalttiming = " + intervalTiming);
		sync();
		startTimer();
		return START_STICKY;
	}

	private void sync() {
		Log.d(TAG, "intervalttiming sync = " + intervalTiming);
		loadTimingSettings();
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
		timerHandler.postDelayed(timerTask, intervalTiming);

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
	
	private void loadTimingSettings() {
		SharedPreferences prefrences = PreferenceManager
				.getDefaultSharedPreferences(this);
		int interval = prefrences
				.getInt(SettingsFragment.KEY_INTERVAL_TIME, 30);
		intervalTiming = 1000 * 60 * interval;
	}

	
}
