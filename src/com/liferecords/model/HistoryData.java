package com.liferecords.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.liferecords.network.Network;
import com.liferecords.network.Respone;

public class HistoryData {
	private static final String TAG = HistoryData.class.getSimpleName();
	private Double latitude;
	private Double longitude;
	private double accuracy;
	private String address;
	private Context content;
	private int batteryPrecent;
	private boolean batteryCharge;
	private int motion;
	private Network network;
	private Double pivotLatitude;
	private Double pivotLongitude;
	private double pivotAccuracy;
	private PostObjectsParse account;

	public int getBatteryPrecent() {
		return batteryPrecent;
	}

	public void setBatteryPrecent(int batteryPrecent) {
		this.batteryPrecent = batteryPrecent;
	}

	public boolean isBatteryCharge() {
		return batteryCharge;
	}

	public void setBatteryCharge(boolean batteryCharge) {
		this.batteryCharge = batteryCharge;
	}

	public int getMotion() {
		return motion;
	}

	public void setMotion(int motion) {
		this.motion = motion;
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
	}

	public HistoryData(Context content) {
		super();
		this.content = content;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void updateGeo(double latitude, double longitude, double accuracy) {
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public void setObjectsInAccountToParse() {
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
	}

	public void sendGetAddress(){
		if(pivotLongitude != null && pivotLatitude != null){
			float distance = distanceBetween(latitude, longitude, pivotLatitude, pivotLongitude);
			if(distance < 100) {
				return;
			}
		}
		Respone respone = network.getAddress(latitude, longitude);
		if(respone == null || !respone.isOK()){
			return;
		}
		try{
			JSONObject result = new JSONObject(respone.body);
			JSONObject results = result.getJSONArray("results").getJSONObject(0);
			this.address = results.getString("formatted_address");
			Log.d(TAG, "address is : " + address);
		} catch (JSONException e){
			e.printStackTrace();
		}
		this.pivotLatitude = this.latitude;
		this.pivotLongitude = this.longitude;
		this.pivotAccuracy = this.accuracy;
		
	}

	public float distanceBetween(double startlangtitude, double startlongitude,
			double endlangitude, double endlongitude) {
		float[] results = new float[2];
		Location.distanceBetween(startlangtitude, startlongitude, endlangitude,
				endlongitude, results);
		float distance = results[0];
		return distance;
	}
}
