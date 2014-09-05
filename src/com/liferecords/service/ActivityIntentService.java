package com.liferecords.service;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.liferecords.model.HistoryData;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ActivityIntentService extends IntentService {
	static final String TAG = ActivityIntentService.class.getSimpleName();
	static final String ACTION = "com.liferecords.service."
			+ ActivityIntentService.class.getSimpleName() + ".BROADCAST";
	HistoryData model;

	public ActivityIntentService() {
		super(ActivityIntentService.class.getSimpleName());

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context content = this;
		model = new HistoryData(content);
		if(ActivityRecognitionResult.hasResult(intent)){
			ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
			DetectedActivity mostProbaleActivity = result.getMostProbableActivity();
			int activityType = mostProbaleActivity.getType();
			Log.d(TAG, "most probale activity: " + activityType);
			model.setMotion(activityType);
			intent = new Intent(ACTION);
			LocalBroadcastManager.getInstance(content).sendBroadcast(intent);
		}

	}

}
