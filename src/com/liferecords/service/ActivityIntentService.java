package com.liferecords.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.liferecords.model.Model;

public class ActivityIntentService extends IntentService {
	static final String TAG = ActivityIntentService.class.getSimpleName();
	static final String ACTION = "com.liferecords.service."
			+ ActivityIntentService.class.getSimpleName() + ".BROADCAST";
	Model model;

	public ActivityIntentService() {
		super(ActivityIntentService.class.getSimpleName());

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Context content = this;
		model = new Model(content);
		if(ActivityRecognitionResult.hasResult(intent)){
			ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
			DetectedActivity mostProbaleActivity = result.getMostProbableActivity();
			int activityType = mostProbaleActivity.getType();
			Log.d(TAG, "most probale activity: " + activityType);
			model.account.data.setMotion(activityType);
			intent = new Intent(ACTION);
			LocalBroadcastManager.getInstance(content).sendBroadcast(intent);
		}

	}

}
