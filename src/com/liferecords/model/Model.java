package com.liferecords.model;

import com.liferecords.network.Network;
import com.parse.ParseObject;

import android.content.Context;
import android.graphics.Bitmap;

public class Model {

	public final Context content;
	private final Network network;
	public final Account account;
	private final PostObjectsParse posts;
	
	public Model(Context content) {
		super();
		this.content = content;
		this.network = new Network();
		account = new Account(content, network);
		posts = new PostObjectsParse();
	}
	
	public Bitmap sendStreeView(double latitude,double longitude){
		Bitmap bitmap = network.getStreetView(latitude, longitude);
		return bitmap;
	}
}
