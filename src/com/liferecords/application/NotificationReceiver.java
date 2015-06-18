package com.liferecords.application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver {
	public static final String TAG = "NotificationUtils";
	private static final int NOTIFICATION_DEFAULT_ON = 1000;
	private static final int NOTIFICATION_DEFAULT_OFF = 4000;
	private static final int NOTIFICATION_DEFAULT_COLOR = Color.YELLOW;

	public static void notificatePush(Context context, int notificationId,
			String tickerText, String contentTitle, String contentText,
			Intent intent) {
		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(contentTitle).setContentText(contentText)
				.setSound(soundUri).setTicker(tickerText);

		// Because clicking the notification opens a new ("special") activity,
		// there's no need to create an artificial back stack.
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		mBuilder.setOnlyAlertOnce(true);

		// Gets an instance of the NotificationManager service
		NotificationManager notifyMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Builds the notification and issues it.
		notifyMgr.notify(notificationId, mBuilder.build());
	}
}
