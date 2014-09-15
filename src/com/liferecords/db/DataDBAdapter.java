package com.liferecords.db;

import java.util.ArrayList;
import java.util.List;

import com.liferecords.model.DateAdapterItem;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

public class DataDBAdapter {
	HistoryDB helper;

	public DataDBAdapter(Context context) {
		helper = new HistoryDB(context);
	}

	public long insertData(double latitude, double longitude, double accuracy,
			String address, boolean batteryCharged, int batteryPrec,
			int motion, double pivotLatitude, double pivotLongitude,
			double pivotAccuracy, int countId, String timeCreated,
			String parseUser, String typeAddress, int datewithouttime) {
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
		contentValues.put(HistoryDB.USERID, parseUser);
		contentValues.put(HistoryDB.TIMECREATED, timeCreated);
		contentValues.put(HistoryDB.TYPEADDRESS, typeAddress);
		contentValues.put(HistoryDB.DATEWITHOUTTIME, datewithouttime);
		long id = db.insert(HistoryDB.TABLE_HISTORY, null, contentValues);
		db.close();
		return id;
	}

	public int getUserIdData(String userId) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String[] columns = { HistoryDB.COUNTID };
		Cursor cursor = db.query(HistoryDB.TABLE_HISTORY, columns,
				HistoryDB.USERID + " = '" + userId + "'", null, null, null,
				HistoryDB.COUNTID + " ASC");
		int countIndex;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToLast();
			countIndex = cursor.getInt(0);
		} else {
			countIndex = 0;
		}

		return countIndex;
	}

	public void getUserDataBasedOnDate(String day1, String day2) {
		SQLiteDatabase db = helper.getWritableDatabase();

		String[] columns = {};
		Cursor cursor = db.query(HistoryDB.TABLE_HISTORY, columns,
				HistoryDB.TIMECREATED + " BETWEEN '" + day1 + "' AND '" + day2
						+ "'", null, null, null, HistoryDB.TIMECREATED
						+ " DESC");

	}

	
	public void getUserDates(List<DateAdapterItem> dates) {
		SQLiteDatabase db = helper.getWritableDatabase();
		
		
		String[] columns = {HistoryDB.TIMECREATED,HistoryDB.DATEWITHOUTTIME,HistoryDB.COUNTID};
		Cursor cursor = db.query(HistoryDB.TABLE_HISTORY, columns, null, null, HistoryDB.DATEWITHOUTTIME, null, HistoryDB.DATEWITHOUTTIME + " DESC");
		if(cursor == null){
			return;
		}
		if(!cursor.moveToFirst()){
			cursor.close();
			return;
		}
		
		do{
			DateAdapterItem date = new DateAdapterItem();
			dates.add(date);
			date.dateWithoutTime = cursor.getInt(1);
			date.dateString = cursor.getString(0);
			
		} while(cursor.moveToNext());
		cursor.close();
	}

	public ArrayList<Cursor> getData(String Query) {
		// get writable database
		SQLiteDatabase sqlDB = helper.getWritableDatabase();
		String[] columns = new String[] { "mesage" };
		// an array list of cursor to save two cursors one has results from the
		// query
		// other cursor stores error message if any errors are triggered
		ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
		MatrixCursor Cursor2 = new MatrixCursor(columns);
		alc.add(null);
		alc.add(null);

		try {
			String maxQuery = Query;
			// execute the query results will be save in Cursor c
			Cursor c = sqlDB.rawQuery(maxQuery, null);

			// add value to cursor2
			Cursor2.addRow(new Object[] { "Success" });

			alc.set(1, Cursor2);
			if (null != c && c.getCount() > 0) {

				alc.set(0, c);
				c.moveToFirst();

				return alc;
			}
			return alc;
		} catch (SQLException sqlEx) {
			Log.d("printing exception", sqlEx.getMessage());
			// if any exceptions are triggered save the error message to cursor
			// an return the arraylist
			Cursor2.addRow(new Object[] { "" + sqlEx.getMessage() });
			alc.set(1, Cursor2);
			return alc;
		} catch (Exception ex) {

			Log.d("printing exception", ex.getMessage());

			// if any exceptions are triggered save the error message to cursor
			// an return the arraylist
			Cursor2.addRow(new Object[] { "" + ex.getMessage() });
			alc.set(1, Cursor2);
			return alc;
		}

	}

	static class HistoryDB extends SQLiteOpenHelper {

		private final String TAG = HistoryDB.class.getSimpleName();
		private static final String DATABASE_NAME = "liferecordsdb";
		private static final int DATABASE_VERSION = 5;
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
		private static final String TYPEADDRESS = "type";
		private static final String DATEWITHOUTTIME = "datewithouttime";
		private static final String CREATE_TABLE = "CREATE TABLE "
				+ TABLE_HISTORY + " (" + UID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LATITUDE
				+ " DOUBLE NOT NULL, " + LONGITUDE + " DOUBLE NOT NULL, "
				+ ACCURACY + " DOUBLE NOT NULL, " + ADDRESS + " TEXT, "
				+ TYPEADDRESS + " TEXT, " + BATTERYCHARGED + " BOOLEAN, "
				+ BATTERYPREC + " INTEGER NOT NULL, " + MOTION
				+ " INTEGER NOT NULL, " + PIVOTLATITUDE + " DOUBLE NOT NULL, "
				+ PIVOTLONGITUDE + " DOUBLE NOT NULL, " + PIVOTACCURACY
				+ " DOUBLE NOT NULL, " + COUNTID + " INTEGER NOT NULL, "
				+ DATEWITHOUTTIME + " INTEGER NOT NULL, " + USERID
				+ " VARCHAR(255) NOT NULL, " + TIMECREATED + " TEXT);";
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
