package com.liferecords.model;

import android.text.format.Time;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("HistoryParse")
public class PostObjectsParse extends ParseObject {

	
	public PostObjectsParse() {

	};

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser value) {
		put("user", value);
	}

	public double getLatitude() {
		return getDouble("latitude");
	}

	public void setLatitude(double latitude) {
		put("latitude", latitude);
	}

	public double getLongitude() {
		return getDouble("longitude");
	}

	public void setLongitude(double longitude) {
		put("longitude", longitude);
	}

	public double getAccuracy() {
		return getDouble("accuracy");
	}

	public void setAccuracy(double d) {
		put("accuracy", d);
	}

	public long getDateInstance() {
		return getLong("date");
	}

	public void setDate(long refreshTime) {
		put("date", refreshTime);
	}

	public String getAddress(){
		return getString("address");
	}
	public void setAddress(String address){
		put("address",address);
	}
	public int getBatteryPrec(){
		return getInt("batteryPrec");
	}
	public void setBatteryPrec(int batteryPrec){
		put("batteryPrec",batteryPrec);
	}
	public boolean getBatteryCharge(){
		return getBoolean("batteryCharge");
	}
	public void setBatteryCharge(boolean batteryChrage){
		put("batteryCharge",batteryChrage);
	}
	public int getMotion(){
		return getInt("motion");
	}
	public void setMotion(int motion){
		put("motion",motion);
	}
	public Double getPivotLatitude(){
		return getDouble("pivotlatitude");
	}
	public void setPivotLatitude(Double pivotLatitude){
		put("pivotlatitude",pivotLatitude);
	}
	
	public Double getPivotLongitude(){
		return getDouble("pivotlongitude");
	}
	public void setPivotLongitude(Double pivotLongitude){
		put("pivotlongitude",pivotLongitude);
	}
	public double getPivotAccuracy(){
		return getDouble("pivotaccuracy");
	}
	public void setPivotAccuracy(double pivotAccuracy){
		put("pivotaccuracy",pivotAccuracy);
	}
	
}
