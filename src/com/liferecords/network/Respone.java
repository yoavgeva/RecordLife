package com.liferecords.network;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class Respone {

	static final int CLIENT_ERROR = 600;
	public final int code;
	public final String body;
	
	static Respone fromClientError(){
		return new Respone(CLIENT_ERROR, "");
	}
	static Respone fromError(Exception e){
		JSONObject json = new JSONObject();
		String body;
		try{
			json.put("message","Error talking to server");
			json.put("respone",e.toString());
			body = json.toString();
		} catch(JSONException je) {
			body = "{\"message\": \"Error talking to server\"}";
		}
		return new Respone(CLIENT_ERROR, body);
	}
	
	public Respone(){
		this(200,"");
	}
	Respone(int code,String body){
		this.code = code;
		this.body = body;
	}
	
	public boolean isOK(){
		return code == HttpURLConnection.HTTP_OK;
	}
	@Override
	public String toString() {
		return "Respone [code=" + code + ", body=" + body + "]";
	}
}
