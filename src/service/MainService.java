package service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class MainService extends Service {
	static final String TAG = MainService.class.getSimpleName();
	private static final int TIME = 1000 * 60 / 6; // type the minutes last
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, intent.getAction());
			if (intent.getAction().equals(LocationServ.BROADCASTACTION)) {

			}
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
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		unregisterReciver();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startTimer();
		return START_STICKY;
	}

	private void registerReciver() {
		IntentFilter filter = new IntentFilter(LocationServ.BROADCASTACTION);
		LocalBroadcastManager broadcastManager = LocalBroadcastManager
				.getInstance(this);
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
}
