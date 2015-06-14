package com.liferecords.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.liferecords.application.R;

public class InfoWindowMapAdapter implements InfoWindowAdapter {
	
	private Marker markerShowingInfoWindow;
	private Context content;
	
	

	public InfoWindowMapAdapter(Context content) {
		super();
		this.content = content;
	}

	@Override
	public View getInfoContents(Marker marker) {
		markerShowingInfoWindow = marker;
		LayoutInflater inflater = (LayoutInflater) content.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popUp = inflater.inflate(R.layout.marker_popup_layout, null,false);
		ImageView popUpImage = (ImageView) popUp.findViewById(R.id.steetview_image);
		
		return popUp;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		// TODO Auto-generated method stub
		return null;
	}

}
