package com.liferecords.model;

public class DataDateAdapterItem {
	
	public String dateTime;
	public double latitude;
	public double longitude;
	public double accuracy;
	public String address;
	public String typeAdress;
	public boolean batteryCharged;
	public int batteryPrec;
	public int motion;
	public double pivotLatitude;
	public double pivotLongitude;
	public double pivotAccuracy;
	
	public DataDateAdapterItem(){
		
	}

	public DataDateAdapterItem(DataDateAdapterItem item){
		if(item != null){
			dateTime = item.dateTime;
			latitude = item.latitude;
			longitude = item.longitude;
			accuracy = item.accuracy;
			address = item.address;
			typeAdress = item.typeAdress;
			batteryCharged = item.batteryCharged;
			batteryPrec = item.batteryPrec;
			motion = item.motion;
			pivotLatitude = item.pivotLatitude;
			pivotLongitude = item.pivotLongitude;
			pivotAccuracy = item.pivotAccuracy;
			
		}
	}
}
