package com.liferecords.model;

import java.io.ObjectInputStream.GetField;
import java.util.Date;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class PostObjectsParse extends ParseObject {
	double latitude;
	double longitude;
	double accuracy;
	
	public ParseUser getUser(){
		return getParseUser("user");		
	}
	public void setUser(){
		put("user",ParseUser.getCurrentUser());
	}
	public double getLatitude() {
		return getDouble("latitude");
	}
	public void setLatitude(double latitude) {
		put("latitude",latitude);
	}
	public double getLongitude() {
		return getDouble("longitude");
	}
	public void setLongitude(double longitude) {
		put("longitude",longitude);
	}
	public double getAccuracy() {
		return getDouble("accuracy");
	}
	public void setAccuracy(float accuracy) {
		put("accuracy",accuracy);
	}
	
	public Date getDateInstance(){
		return getDate("date");
	}
	public void setDate(Date dateInstance){
		put("date",dateInstance);
	}
	

}
