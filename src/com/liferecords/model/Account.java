package com.liferecords.model;

import com.liferecords.network.Network;
import com.parse.ParseUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Account {

	public HistoryData data;
	private static final String TAG = Account.class.getSimpleName();
	private final Context content;
	private final Network network;
	
	
	
	public Account(Context content, Network network) {
		super();
		this.content = content;
		this.network = network;
		this.data = new HistoryData(content, network);
	}
	
	public void saveAccount(){
		SharedPreferences sharePred = PreferenceManager.getDefaultSharedPreferences(content);
		SharedPreferences.Editor editor = sharePred.edit();
		editor.putString("account","" + ParseUser.getCurrentUser()).commit();
	}
	
	public void loadAccount(){
		SharedPreferences sharePref = PreferenceManager.getDefaultSharedPreferences(content);
		String value = sharePref.getString("account", null);
	}
}
