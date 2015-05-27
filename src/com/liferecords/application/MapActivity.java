package com.liferecords.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liferecords.model.ModelAdapterItem;

public class MapActivity extends Activity {

	private GoogleMap map;
	private List<ModelAdapterItem> itemsMapFrag = new ArrayList<ModelAdapterItem>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_fragment);
		Bundle bundle = this.getIntent().getExtras();
		if(bundle!=null){
			itemsMapFrag = bundle.getParcelableArrayList("items");
			Log.d("amout of items in map", "" + itemsMapFrag.size());
		}
		
		map = ((MapFragment)getFragmentManager().findFragmentById(R.id.location_map)).getMap();
		setUpMap();

	}
	
	private void setUpMap() {
		map.setMyLocationEnabled(true);
		for (int i = 0; i < itemsMapFrag.size(); i++) {
			map.addMarker(new MarkerOptions().position(new LatLng(itemsMapFrag.get(i).latitude, itemsMapFrag.get(i).longitude)));
		}
		LatLng FirstLocation = new LatLng(itemsMapFrag.get(0).latitude, itemsMapFrag.get(0).longitude);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(FirstLocation, 15));
		
		
		
	}

}
