/*package com.liferecords.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataDBAdapter {
	HistoryDB helper;
	
	public DataDBAdapter(Context context){
		helper = new HistoryDB(context);
	}
	
	public long insertData(){
		return null;
	}
	
	class HistoryDB extends SQLiteOpenHelper{
		
		private static final String DATABASE_NAME = "liferecordsdb";
		private static final int DATABASE_VERSION = 1;
		private static final String TABLE_NAME = "datausertable";
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
		private static final String PIVOTACCURACT = "pivotaccuracy";
		private static final String COUNTID = "countid";
		private static final String USERID = "userid";
		private static final String TIMECREATED = "timecreated";
		

		public HistoryDB(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
					}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
*/