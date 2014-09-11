package com.liferecords.model;

import android.content.Context;

import com.liferecords.db.DataDBAdapter;
import com.liferecords.network.Network;
import com.parse.ParseUser;

public class Account {

	public HistoryData data;
	private static final String TAG = Account.class.getSimpleName();
	private Context content;
	private Network network;
	private DataDBAdapter helper;
	
	
	
	public Account(Context content, Network network) {
		super();
		this.content = content;
		this.network = network;
		this.data = new HistoryData(content, network);
	}
	public Account(Context content){
		super();
		this.content = content;
		helper = new DataDBAdapter(content);
	}
	
	public int getCountIdOfUser(ParseUser user){
		int count = helper.getUserIdData(user.toString());
		return count;
	}
}
