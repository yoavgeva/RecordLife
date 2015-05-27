package com.liferecords.application;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.liferecords.model.ModelAdapterItem;

public class LocationFragment extends Fragment {
	private static GoogleMap googleMap;
	private static View view;
	private static double latitude, longitude;
	private static List<ModelAdapterItem> itemsMapFrag = new ArrayList<ModelAdapterItem>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		/*Bundle extra1 = getArguments();
		if(extra1 != null){
			itemsMapFrag = extra1.getParcelableArrayList("mapitems");
		}*/
		view = inflater.inflate(R.layout.location_fragment, container, false);
		
		
		setUpMapIfNeeded();

		return view;
	}

	private  void setUpMapIfNeeded() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();
			if(googleMap != null){
				setUpMap();
			}
					
		}
	}

	private void setUpMap() {
		googleMap.setMyLocationEnabled(true);
		for (int i = 0; i < itemsMapFrag.size(); i++) {
			googleMap.addMarker(new MarkerOptions().position(new LatLng(itemsMapFrag.get(i).latitude, itemsMapFrag.get(i).longitude)));
		}
		
		
		
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		if(googleMap != null){
			setUpMap();
		}
		if(googleMap == null){
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.location_map)).getMap();
			if(googleMap != null){
				setUpMap();
			}
		}
		
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(googleMap !=null){
			FragmentManager manager = getFragmentManager();
			FragmentTransaction trans = manager.beginTransaction();
			trans.remove(this).commit();
		}
	}
	
	/*public void setItems(List<ModelAdapterItem> itemsMap){
		itemsMapFrag = new ArrayList<ModelAdapterItem>();
		itemsMapFrag.addAll(itemsMap);
	}*/
	
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
	}

}
