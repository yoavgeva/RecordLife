package com.liferecords.service;

import com.liferecords.model.HistoryData;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class SyncService extends IntentService {
	static final String TAG = SyncService.class.getSimpleName();
	public static final String ACTION = "com.liferecords.service."
			+ SyncService.class.getSimpleName() + ".BROADCAST";
	public boolean stop;
	private HistoryData model;

	public SyncService() {
		super(SyncService.class.getSimpleName());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		updateBatteryLevel();

	}

	@Override
	public void onCreate() {
		Context content = this;
		model = new HistoryData(content);
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
		if(status == BatteryManager.BATTERY_STATUS_CHARGING){
			isCharging = true;
		}
		int batteryLevel = (int)(((float)level * (float)scale) * 100.0f);
		model.setBatteryPrecent(batteryLevel);
		model.setBatteryCharge(isCharging);
	}
}
