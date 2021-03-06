package com.liferecords.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;

import com.liferecords.db.DataDBAdapter;
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
	private String typeAddress;
	private final Context content;
	private int batteryPrecent;
	private boolean batteryCharge;
	private int motion;
	private Network network;
	private Double pivotLatitude;
	private Double pivotLongitude;
	private double pivotAccuracy;
	private PostObjectsParse account;
	private Time refreshTime;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	DataDBAdapter helper;
	private int countId = 1;
	private JSONArray typeArray;
	private int dateWithoutTime;

	public int getBatteryPrecent() {
		toLoadPref();
		return batteryPrecent;
	}

	public void setBatteryPrecent(int batteryPrecent) {
		this.batteryPrecent = batteryPrecent;
		Log.d(TAG, "batteryprec: " + this.batteryPrecent);
		editor.putInt("batteryPrec", batteryPrecent);
		editor.commit();
	}

	public boolean isBatteryCharge() {
		toLoadPref();
		return batteryCharge;
	}

	public void setBatteryCharge(boolean batteryCharge) {
		this.batteryCharge = batteryCharge;
		Log.d(TAG, "charge: " + this.batteryCharge);
		editor.putBoolean("charge", batteryCharge);
		editor.commit();
	}

	public int getMotion() {
		toLoadPref();
		return motion;
	}

	public void setMotion(int motion) {
		this.motion = motion;
		Log.d(TAG, "motion: " + this.motion);
		editor.putInt("motion", motion);
		editor.commit();
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
		sharedPref = PreferenceManager.getDefaultSharedPreferences(content);
		editor = sharedPref.edit();
		helper = new DataDBAdapter(content);
	}

	public HistoryData(Context content) {
		super();
		this.content = content;
		account = new PostObjectsParse();
		sharedPref = PreferenceManager.getDefaultSharedPreferences(content);
		editor = sharedPref.edit();
		helper = new DataDBAdapter(content);

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

	public void updateGeo(double latitude, double longitude, double accuracy) {
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
		Log.d(TAG, "latitude: " + this.latitude);
		Log.d(TAG, "longitude: " + this.longitude);
		Log.d(TAG, "accuracy: " + this.accuracy);
		editor.putLong("latitude", Double.doubleToRawLongBits(latitude));
		editor.putLong("longitude", Double.doubleToRawLongBits(longitude));
		editor.putLong("accuracy", Double.doubleToRawLongBits(accuracy));
		editor.commit();
	}

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
		Respone respone = network.getAddress(latitude, longitude);
		if (respone == null || !respone.isOK()) {
			return;
		}
		try {
			JSONObject result = new JSONObject(respone.body);
			JSONObject results = result.getJSONArray("results")
					.getJSONObject(0);
			this.address = results.getString("formatted_address");
			this.typeArray = results.getJSONArray("types");
			this.typeAddress = typeArray.getString(0);
			Log.d(TAG, "address is : " + this.address + " types: "
					+ this.typeArray + "type: " + this.typeAddress);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		this.pivotLatitude = this.latitude;
		this.pivotLongitude = this.longitude;
		this.pivotAccuracy = this.accuracy;
		editor.putString("type", typeAddress);
		editor.putString("address", address);
		editor.putLong("pivotlatitude",
				Double.doubleToRawLongBits(pivotLatitude));
		editor.putLong("pivotlongitude",
				Double.doubleToRawLongBits(pivotLongitude));
		editor.putLong("pivotaccuracy",
				Double.doubleToRawLongBits(pivotAccuracy));
		editor.commit();
	}

	public float distanceBetween(double startlangtitude, double startlongitude,
			double endlangitude, double endlongitude) {
		float[] results = new float[2];
		Location.distanceBetween(startlangtitude, startlongitude, endlangitude,
				endlongitude, results);
		float distance = results[0];
		return distance;
	}

	//added the if need to check if works
	public void postDataToParse() {
		toLoadPref();
		if(latitude != null || latitude != 0){

		
		loadToDB();
		loadToParse();
		saveCountID();
		}
	}

	private void toLoadPref() {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(content);
		latitude = Double.longBitsToDouble(sharedPref.getLong("latitude",
				Double.doubleToLongBits(0)));
		longitude = Double.longBitsToDouble(sharedPref.getLong("longitude",
				Double.doubleToLongBits(0)));
		accuracy = Double.longBitsToDouble(sharedPref.getLong("accuracy",
				Double.doubleToLongBits(0)));
		address = sharedPref.getString("address", "");
		batteryCharge = sharedPref.getBoolean("charge", false);
		batteryPrecent = sharedPref.getInt("batteryPrec", -1);
		motion = sharedPref.getInt("motion", -1);
		pivotLatitude = Double.longBitsToDouble(sharedPref.getLong(
				"pivotlatitude", Double.doubleToLongBits(0)));
		pivotLongitude = Double.longBitsToDouble(sharedPref.getLong(
				"pivotlongitude", Double.doubleToLongBits(0)));
		pivotAccuracy = Double.longBitsToDouble(sharedPref.getLong(
				"pivotaccuracy", Double.doubleToLongBits(0)));
		saveTimeRefresh();
		countId = sharedPref.getInt("countid", -1);
		typeAddress = sharedPref.getString("type", "");
	}

	private void saveTimeRefresh() {
		refreshTime = new Time(Time.getCurrentTimezone());
		refreshTime.setToNow();
	}

	private void saveCountID() {
		countId++;
		editor.putInt("countid", countId);
		editor.commit();
	}

	private void loadToDB() {
		toLoadPref();
		dateWithoutTime = Time.getJulianDay(refreshTime.toMillis(false), 0);
		long id = helper.insertData(latitude, longitude, accuracy, address,
				batteryCharge, batteryPrecent, motion, pivotLatitude,
				pivotLongitude, pivotAccuracy, countId, refreshTime
						.format2445(),
				ParseUser.getCurrentUser().getUsername(), typeAddress,dateWithoutTime);
		if (id < 0) {
			Log.d(TAG, "insertData Failed!!!!");
		} else {
			Log.d(TAG, "id: " + id);
		}

	}

	private void loadToParse() {
		account.setLatitude(latitude);
		account.setLongitude(longitude);
		account.setAccuracy(accuracy);
		account.setAddress(address);
		account.setBatteryCharge(batteryCharge);
		account.setBatteryPrec(batteryPrecent);
		account.setMotion(motion);
		account.setPivotLatitude(pivotLatitude);
		account.setPivotLongitude(pivotLongitude);
		account.setPivotAccuracy(pivotAccuracy);
		account.setUser(ParseUser.getCurrentUser());
		account.setDate(refreshTime.toMillis(false));
		account.setDateString(refreshTime.format2445());
		dateWithoutTime = Time.getJulianDay(refreshTime.toMillis(false), 0);
		account.setDateWithoutTime(dateWithoutTime);
		account.setCountId(countId);
		account.setType(typeAddress);

		ParseACL acl = new ParseACL();
		acl.setReadAccess(ParseUser.getCurrentUser(), true);
		acl.setWriteAccess(ParseUser.getCurrentUser(), true);
		account.setACL(acl);
		account.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {

			}
		});
	}

}
