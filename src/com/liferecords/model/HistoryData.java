package com.liferecords.model;

import android.content.Context;

import com.liferecords.network.Network;

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
	private double pivotLatitude;
	private double pivotLongitude;
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
	
	public void setObjectsInAccountToParse(){
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

}
