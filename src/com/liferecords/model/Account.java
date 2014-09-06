package com.liferecords.model;

import android.content.Context;

import com.liferecords.network.Network;

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
	
	
}
