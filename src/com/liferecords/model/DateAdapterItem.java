package com.liferecords.model;

public class DateAdapterItem {
	public int dateWithoutTime;
	public String dateString;
	
	public DateAdapterItem(){
		
	}

	public DateAdapterItem(DateAdapterItem item) {
		if (item != null) {
			dateWithoutTime = item.dateWithoutTime;
			dateString = item.dateString;
		}
	}

}
