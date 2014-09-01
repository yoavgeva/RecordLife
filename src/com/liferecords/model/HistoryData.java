package com.liferecords.model;

import android.content.Context;

public class HistoryData {
	private Double latitude;
	private Double longitude;
	private float accuracy;
	private String address;
	public Context content;
	
	
	
	

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
	
	public float getAccuracy() {
		return accuracy;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public void updateGeo(double latitude,double longitude,float accuracy){
		this.accuracy = accuracy;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
