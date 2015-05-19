package com.liferecords.model;

public class DateAdapterItem {
	public Integer dateWithoutTime;

	public int timeCreated;

	public DateAdapterItem() {

	}

	public DateAdapterItem(DateAdapterItem item) {
		super();
		if (item != null) {
			dateWithoutTime = item.dateWithoutTime;

			timeCreated = item.timeCreated;
		}
	}

	@Override
	public String toString() {
		return "DateAdapterItem [dateWithoutTime=" + dateWithoutTime
				+ ", timeCreated=" + timeCreated + "]";
	}

}
