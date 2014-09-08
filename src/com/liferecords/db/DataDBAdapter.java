package com.liferecords.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataDBAdapter {
	HistoryDB helper;

	public DataDBAdapter(Context context) {
		helper = new HistoryDB(context);
	}

	public long insertData(double latitude, double longitude, double accuracy,
			String address, boolean batteryCharged, int batteryPrec,
			int motion, double pivotLatitude, double pivotLongitude,
			double pivotAccuracy, int countId, long timeCreated) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(HistoryDB.LATITUDE, latitude);
		contentValues.put(HistoryDB.LONGITUDE, longitude);
		contentValues.put(HistoryDB.ACCURACY, accuracy);
		contentValues.put(HistoryDB.ADDRESS, address);
		contentValues.put(HistoryDB.BATTERYCHARGED, batteryCharged);
		contentValues.put(HistoryDB.BATTERYPREC, batteryPrec);
		contentValues.put(HistoryDB.MOTION, motion);
		contentValues.put(HistoryDB.PIVOTLATITUDE, pivotLatitude);
		contentValues.put(HistoryDB.PIVOTLONGITUDE, pivotLongitude);
		contentValues.put(HistoryDB.PIVOTACCURACY, pivotAccuracy);
		contentValues.put(HistoryDB.COUNTID, countId);
		contentValues.put(HistoryDB.TIMECREATED, timeCreated);
		long id = db.insert(HistoryDB.TABLE_HISTORY, null, contentValues);
		return id;
	}

	static class HistoryDB extends SQLiteOpenHelper {

		private final String TAG = HistoryDB.class.getSimpleName();
		private static final String DATABASE_NAME = "liferecordsdb";
		private static final int DATABASE_VERSION = 1;
		private static final String TABLE_HISTORY = "datausertable";
		private static final String UID = "_id";
		private static final String LATITUDE = "latitude";
		private static final String LONGITUDE = "longitude";
		private static final String ACCURACY = "accuracy";
		private static final String ADDRESS = "address";
		private static final String BATTERYCHARGED = "batterycharged";
		private static final String BATTERYPREC = "batteryprec";
		private static final String MOTION = "motion";
		private static final String PIVOTLATITUDE = "pivotlatitude";
		private static final String PIVOTLONGITUDE = "pivotlongitude";
		private static final String PIVOTACCURACY = "pivotaccuracy";
		private static final String COUNTID = "countid";
		private static final String USERID = "userid";
		private static final String TIMECREATED = "timecreated";
		private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_HISTORY
				+ " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ LATITUDE + " DOUBLE NOT NULL, " + LONGITUDE
				+ " DOUBLE NOT NULL, " + ACCURACY + " DOUBLE NOT NULL, "
				+ ADDRESS + " TEXT, " + BATTERYCHARGED + " BOOLEAN, "
				+ BATTERYPREC + " INTEGER NOT NULL, " + MOTION
				+ " INTEGER NOT NULL, " + PIVOTLATITUDE + " DOUBLE NOT NULL, "
				+ PIVOTLONGITUDE + " DOUBLE NOT NULL, " + PIVOTACCURACY
				+ " DOUBLE NOT NULL, " + COUNTID + " INTEGER NOT NULL, "
				+ USERID + " VARCHAR(255) NOT NULL, " + TIMECREATED
				+ " BIGINT NOT NULL);";
		private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
				+ TABLE_HISTORY;

		public HistoryDB(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				Log.d(TAG, "table created");
				db.execSQL(CREATE_TABLE);
			} catch (SQLException e) {
				Log.d(TAG, "Error in create table is: " + e);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			try {
				Log.d(TAG, "table upgraded");
				db.execSQL(DROP_TABLE);
				onCreate(db);
			} catch (SQLException e) {
				Log.d(TAG, "error in upgrade table: " + e);
			}

		}
		
		

	}

}
