package com.liferecords.model;

import android.graphics.Bitmap;

public class ModelAdapterItem {

	public Double latitude;
	public Double longitude;
	public double accuracy;
	public String address;
	public String type;
	public int batteryPrecent;
	public boolean batteryCharge;
	public int motion;
	public Double pivotLatitude;
	public Double pivotLongitude;
	public double pivotAccuracy;
	public long recordTime;
	public String userName;
	public int countId;
	public Bitmap streetViewImage;
	
	
	public ModelAdapterItem() {
		
	}
	
	public ModelAdapterItem(ModelAdapterItem item){
		if(item != null){
			latitude = item.latitude;
			longitude = item.longitude;
			accuracy = item.accuracy;
			address = item.address;
			batteryPrecent = item.batteryPrecent;
			batteryCharge = item.batteryCharge;
			motion = item.motion;
			pivotLatitude = item.pivotLatitude;
			pivotLongitude = item.pivotLongitude;
			pivotAccuracy = item.pivotAccuracy;
			recordTime = item.recordTime;
			userName = item.userName;
			countId = item.countId;
			type = item.type;
			streetViewImage = item.streetViewImage;
		}
	}

	@Override
	public String toString() {
		return "ModelAdapterItem [latitude=" + latitude + ", longitude="
				+ longitude + ", accuracy=" + accuracy + ", address=" + address
				+ ", type=" + type + ", batteryPrecent=" + batteryPrecent
				+ ", batteryCharge=" + batteryCharge + ", motion=" + motion
				+ ", pivotLatitude=" + pivotLatitude + ", pivotLongitude="
				+ pivotLongitude + ", pivotAccuracy=" + pivotAccuracy
				+ ", recordTime=" + recordTime + ", userName=" + userName
				+ ", countId=" + countId + ", streetViewImage="
				+ streetViewImage + "]";
	}

	

	
}
