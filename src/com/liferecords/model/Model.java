package com.liferecords.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.liferecords.db.DataDBAdapter;
import com.liferecords.network.Network;

public class Model {

	public final Context content;
	private final Network network;
	public final Account account;
	
	
	
	public Model(Context content) {
		super();
		this.content = content;
		this.network = new Network();
		account = new Account(content, network);
		new PostObjectsParse();
		
	}
	
	public List<DateAdapterItem> getDateAdapterItems(){
		List<DateAdapterItem> items = new ArrayList<DateAdapterItem>();
		DataDBAdapter helper = new DataDBAdapter(content);
		helper.getUserDates(items);
		
		
		return items;
		
	}
	
	public List<DataDateAdapterItem> getDataDateAdapterItems(int dateWithoutTime){
		List<DataDateAdapterItem> items = new ArrayList<DataDateAdapterItem>();
		DataDBAdapter helper = new DataDBAdapter(content);
		helper.getUserData(items, dateWithoutTime);
		return items;
	}
	public Bitmap sendStreeView(double latitude,double longitude){
		Bitmap bitmap = network.getStreetView(latitude, longitude);
		return bitmap;
	}
}
