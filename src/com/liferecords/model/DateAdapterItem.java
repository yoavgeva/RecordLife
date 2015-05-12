package com.liferecords.model;

public class DateAdapterItem {
	public int dateWithoutTime;
	public String dateString;
	public int timeCreated;
	
	public DateAdapterItem(){
		
	}

	public DateAdapterItem(DateAdapterItem item) {
		super();
		if (item != null) {
			dateWithoutTime = item.dateWithoutTime;
			dateString = item.dateString;
			timeCreated = item.timeCreated;
		}
	}

}
