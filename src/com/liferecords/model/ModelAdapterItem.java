package com.liferecords.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelAdapterItem implements Parcelable {

	public Double latitude;
	public Double longitude;
	public double accuracy;
	public String address;
	public String type;
	public int batteryPrecent;
	public boolean batteryCharge;
	public int motion;
	public Double pivotLatitude;
	public Double pivotLongitude;
	public double pivotAccuracy;
	public String recordTime;
	public int countId;
	public int dateOnly;

	public ModelAdapterItem() {

	}

	
	public ModelAdapterItem(Parcel in) {
		 latitude = in.readByte() == 0x00 ? null : in.readDouble();
	        longitude = in.readByte() == 0x00 ? null : in.readDouble();
	        accuracy = in.readDouble();
	        address = in.readString();
	        type = in.readString();
	        batteryPrecent = in.readInt();
	        batteryCharge = in.readByte() != 0x00;
	        motion = in.readInt();
	        pivotLatitude = in.readByte() == 0x00 ? null : in.readDouble();
	        pivotLongitude = in.readByte() == 0x00 ? null : in.readDouble();
	        pivotAccuracy = in.readDouble();
	        recordTime = in.readString();
	        countId = in.readInt();
	        dateOnly = in.readInt();
	    
	}

	public ModelAdapterItem(ModelAdapterItem item) {
		super();
		if (item != null) {
			latitude = item.latitude;
			longitude = item.longitude;
			accuracy = item.accuracy;
			address = item.address;
			batteryPrecent = item.batteryPrecent;
			batteryCharge = item.batteryCharge;
			motion = item.motion;
			pivotLatitude = item.pivotLatitude;
			pivotLongitude = item.pivotLongitude;
			pivotAccuracy = item.pivotAccuracy;
			recordTime = item.recordTime;

			countId = item.countId;
			type = item.type;
			dateOnly = item.dateOnly;
		
		}
	}

	@Override
	public String toString() {
		return "ModelAdapterItem [latitude=" + latitude + ", longitude="
				+ longitude + ", accuracy=" + accuracy + ", address=" + address
				+ ", type=" + type + ", batteryPrecent=" + batteryPrecent
				+ ", batteryCharge=" + batteryCharge + ", motion=" + motion
				+ ", pivotLatitude=" + pivotLatitude + ", pivotLongitude="
				+ pivotLongitude + ", pivotAccuracy=" + pivotAccuracy
				+ ", recordTime=" + recordTime + ", countId=" + countId
				+ ", dateOnly=" + dateOnly + "]";
	}

	@Override
	public int describeContents() {
		
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		 if (latitude == null) {
	            dest.writeByte((byte) (0x00));
	        } else {
	            dest.writeByte((byte) (0x01));
	            dest.writeDouble(latitude);
	        }
	        if (longitude == null) {
	            dest.writeByte((byte) (0x00));
	        } else {
	            dest.writeByte((byte) (0x01));
	            dest.writeDouble(longitude);
	        }
	        dest.writeDouble(accuracy);
	        dest.writeString(address);
	        dest.writeString(type);
	        dest.writeInt(batteryPrecent);
	        dest.writeByte((byte) (batteryCharge ? 0x01 : 0x00));
	        dest.writeInt(motion);
	        if (pivotLatitude == null) {
	            dest.writeByte((byte) (0x00));
	        } else {
	            dest.writeByte((byte) (0x01));
	            dest.writeDouble(pivotLatitude);
	        }
	        if (pivotLongitude == null) {
	            dest.writeByte((byte) (0x00));
	        } else {
	            dest.writeByte((byte) (0x01));
	            dest.writeDouble(pivotLongitude);
	        }
	        dest.writeDouble(pivotAccuracy);
	        dest.writeString(recordTime);
	        dest.writeInt(countId);
	        dest.writeInt(dateOnly);
	}
	
	public static final Parcelable.Creator<ModelAdapterItem> CREATOR = new Parcelable.Creator<ModelAdapterItem>() {
        @Override
        public ModelAdapterItem createFromParcel(Parcel in) {
            return new ModelAdapterItem(in);
        }

        @Override
        public ModelAdapterItem[] newArray(int size) {
            return new ModelAdapterItem[size];
        }
    };


}
