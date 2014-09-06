package com.liferecords.model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;

import com.liferecords.network.Network;
import com.liferecords.network.Respone;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class HistoryData {
	private static final String TAG = HistoryData.class.getSimpleName();
	private Double latitude;
	private Double longitude;
	private double accuracy;
	private String address;
	private final Context content;
	private int batteryPrecent;
	private boolean batteryCharge;
	private int motion;
	private Network network;
	private Double pivotLatitude;
	private Double pivotLongitude;
	private double pivotAccuracy;
	private PostObjectsParse account;
	private long refreshTime;

	public int getBatteryPrecent() {
		toLoadPref();
		return batteryPrecent;
	}

	public void setBatteryPrecent(int batteryPrecent) {
		this.batteryPrecent = batteryPrecent;
		account.setBatteryPrec(this.batteryPrecent);
		Log.d(TAG, "batteryprec: " + this.batteryPrecent);
		toSavePref();
	}

	public boolean isBatteryCharge() {
		toLoadPref();
		return batteryCharge;
	}

	public void setBatteryCharge(boolean batteryCharge) {
		this.batteryCharge = batteryCharge;
		account.setBatteryCharge(this.batteryCharge);
		Log.d(TAG, "charge: " + this.batteryCharge);
		toSavePref();
	}

	public int getMotion() {
		toLoadPref();
		return motion;
	}

	public void setMotion(int motion) {
		this.motion = motion;
		account.setMotion(this.motion);
		Log.d(TAG, "motion: " + this.motion);
		toSavePref();
	}

	public double getPivotLatitude() {
		return pivotLatitude;
	}

	public void setPivotLatitude(double pivotLatitude) {
		this.pivotLatitude = pivotLatitude;
	}

	public double getPivotLongitude() {
		return pivotLongitude;
	}

	public void setPivotLongitude(double pivotLongitude) {
		this.pivotLongitude = pivotLongitude;
	}

	public double getPivotAccuracy() {
		return pivotAccuracy;
	}

	public void setPivotAccuracy(double pivotAccuracy) {
		this.pivotAccuracy = pivotAccuracy;
	}

	public HistoryData(Context content, Network network) {
		super();
		this.content = content;
		this.network = network;
		account = new PostObjectsParse();
	}

	public HistoryData(Context content) {
		super();
		this.content = content;
		account = new PostObjectsParse();
	}

	public Double getLatitude() {
		toLoadPref();
		return latitude;
	}

	public Double getLongitude() {
		toLoadPref();
		return longitude;
	}

	public double getAccuracy() {
		toLoadPref();
		return accuracy;
	}

	public String getAddress() {
		toLoadPref();
		return address;
	}

	public void setAddress(String address) {

		this.address = address;
		toSavePref();
	}

	public void updateGeo(double latitude, double longitude, double accuracy) {
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
		account.setLatitude(this.latitude);
		Log.d(TAG, "latitude: " + this.latitude);
		account.setLongitude(this.longitude);
		Log.d(TAG, "longitude: " + this.longitude);
		account.setAccuracy(this.accuracy);
		Log.d(TAG, "accuracy: " + this.accuracy);
		toSavePref();
	}

	/*
	 * public void setObjectsInAccountToParse() { account.setLatitude(latitude);
	 * account.setLongitude(longitude); account.setAccuracy(accuracy);
	 * account.setAddress(address); account.setBatteryCharge(batteryCharge);
	 * account.setBatteryPrec(batteryPrecent); account.setMotion(motion);
	 * account.setPivotLatitude(pivotLatitude);
	 * account.setPivotLongitude(pivotLongitude);
	 * account.setPivotAccuracy(pivotAccuracy); }
	 */

	public void sendGetAddress() {

		toLoadPref();
		if (pivotLongitude != null && pivotLatitude != null) {
			float distance = distanceBetween(latitude, longitude,
					pivotLatitude, pivotLongitude);
			if (distance < 100) {
				return;
			}
		}
		Log.d(TAG, "lattitude: " + latitude + " longitude: " + longitude);
		if (latitude == null && longitude == null) {
			return;
		}
		Respone respone = network.getAddress(latitude, longitude);
		if (respone == null || !respone.isOK()) {
			return;
		}
		try {
			JSONObject result = new JSONObject(respone.body);
			JSONObject results = result.getJSONArray("results")
					.getJSONObject(0);
			this.address = results.getString("formatted_address");
			Log.d(TAG, "address is : " + this.address);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		this.pivotLatitude = this.latitude;
		this.pivotLongitude = this.longitude;
		this.pivotAccuracy = this.accuracy;
		toSavePref();
	}

	public float distanceBetween(double startlangtitude, double startlongitude,
			double endlangitude, double endlongitude) {
		float[] results = new float[2];
		Location.distanceBetween(startlangtitude, startlongitude, endlangitude,
				endlongitude, results);
		float distance = results[0];
		return distance;
	}

	public void postDataToParse() {
		toLoadPref();

		account.setLatitude(this.getLatitude());
		account.setLongitude(this.getLongitude());
		account.setAccuracy(this.getAccuracy());
		account.setAddress(this.getAddress());
		account.setBatteryCharge(this.isBatteryCharge());
		account.setBatteryPrec(this.getBatteryPrecent());
		account.setMotion(this.getMotion());
		account.setPivotLatitude(this.getPivotLatitude());
		account.setPivotLongitude(this.getPivotLongitude());
		account.setPivotAccuracy(this.getPivotAccuracy());

		account.setUser(ParseUser.getCurrentUser());
		refreshTime = new Date().getTime();
		account.setDate(refreshTime);

		ParseACL acl = new ParseACL();
		acl.setReadAccess(ParseUser.getCurrentUser(), true);
		acl.setWriteAccess(ParseUser.getCurrentUser(), true);
		account.setACL(acl);
		account.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void toSavePref() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(content);
		SharedPreferences.Editor editor = sharedPref.edit();
		int latitudeInt = latitude.intValue();
		editor.putInt("latitude", latitudeInt);
		int longitudeInt = longitude.intValue();
		editor.putInt("longitude", longitudeInt);
		editor.putInt("accuracy", (int) accuracy);
		editor.putString("address", address);
		editor.putBoolean("charge", batteryCharge);
		editor.putInt("batteryPrec", batteryPrecent);
		editor.putInt("motion", motion);
		// might have to add for pivot if work
		editor.commit();
	}

	private void toLoadPref() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(content);
		int prefLatitude = sharedPref.getInt("latitude", 0);
		double latitudeDou = (double) prefLatitude;
		latitude = (Double) latitudeDou;
		int prefLongitude = sharedPref.getInt("longitude", 0);
		double longitudeDou = (double) prefLongitude;
		longitude = (Double) longitudeDou;
		accuracy = (double) sharedPref.getInt("accuracy", 0);
		address = sharedPref.getString("address", "");
		batteryCharge = sharedPref.getBoolean("charge", false);
		batteryPrecent = sharedPref.getInt("batteryPrec", -1);
		motion = sharedPref.getInt("motion", -1);
	}

}
