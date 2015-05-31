package com.liferecords.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Network {

	private static final String TAG = Network.class.getSimpleName();
	
	protected HttpURLConnection openConnection(URL url) throws IOException{
		return  (HttpURLConnection) url.openConnection();
		
	}
	
	protected Respone get(String urlString){
		try{
			URL url = new URL(urlString);
			HttpURLConnection connection = openConnection(url);
			try {
				connection.connect();
				InputStreamReader reader;
				int responeCode = connection.getResponseCode();
				if(responeCode == HttpURLConnection.HTTP_OK){
					reader = new InputStreamReader(connection.getInputStream());
				} else {
					reader = new InputStreamReader(connection.getErrorStream());
				}
				StringBuilder sb = new StringBuilder();
				char[] buf = new char[512];
				int count;
				while((count = reader.read(buf)) > 0){
					sb.append(buf,0,count);
				}
				return new Respone(responeCode, sb.toString());
			}finally{
				connection.disconnect();
			} 
		}catch (MalformedURLException e ){
			return Respone.fromError(e);
		}catch(IOException e ) {
			return Respone.fromError(e);
			
		}
	}
	
	public Respone getAddress(Double latitude,Double longitude){
		String url = "http://maps.google.com/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=false";
		Log.d(TAG, url);
		if(latitude == null || longitude == null){
			return null;
		}
		return get(url);
		
	}
	public Bitmap getStreetView(double latitude,double longitude){
		final String url = "http://maps.googleapis.com/maps/api/streetview?size=640x640"
				+ "&location="
				+ latitude
				+ ","
				+ longitude
				+ "&sensor=false&key=AIzaSyDgS65mEWkDVVkybtOz1Gq0ZGLzr61t22E";//added browser api key
		Bitmap bitmap = null;
		try{
			InputStream in = new java.net.URL(url).openStream();
			bitmap = BitmapFactory.decodeStream(in);
			Log.d(TAG, "working");
		} catch (Exception e){
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return bitmap;
	}
}
