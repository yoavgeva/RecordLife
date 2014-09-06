package com.liferecords.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.liferecords.model.Model;

public class SyncService extends IntentService {
	static final String TAG = SyncService.class.getSimpleName();
	public static final String ACTION = "com.liferecords.service."
			+ SyncService.class.getSimpleName() + ".BROADCAST";
	public boolean stop;
	private Model model;
	private final IBinder binder = new LocalBinder();

	public class LocalBinder extends Binder {
		public SyncService getService() {
			return SyncService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public SyncService() {
		super(SyncService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		updateBatteryLevel();
		//updateAddress();
		sendStatus();
		broadcast();

	}

	private void broadcast() {
		if (stop) {
			return;
		}
		Intent intent = new Intent(ACTION);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

	}

	@Override
	public void onCreate() {
		Context content = this;
		model = new Model(content);
		super.onCreate();
	}

	private void updateBatteryLevel() {
		if (stop) {
			return;
		}
		Intent batteryIntent = registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		int status = batteryIntent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		boolean isCharging = false;
		if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
			isCharging = true;
		}
		int batteryLevel = (int) (((float) level * (float) scale) * 100.0f);
		Log.d(TAG, "battery prec: " + batteryLevel + ". is charging : "
				+ isCharging);
		model.account.data.setBatteryPrecent(batteryLevel);
		model.account.data.setBatteryCharge(isCharging);
	}

	private void updateAddress() {
		if (stop) {
			return;
		}
		model.account.data.sendGetAddress();
	}
	
	private void sendStatus(){
		if(stop){
			return;
		}
		model.account.data.postDataToParse();
	}

}
