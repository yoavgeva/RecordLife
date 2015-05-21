package com.liferecords.application;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.liferecords.model.ModelAdapterItem;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationFragment extends Fragment {
	private static GoogleMap googleMap;
	private static View view;
	private static double latitude, longitude;
	private List<ModelAdapterItem> itemsMapFrag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

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
		
		
	}
	
	public void setItems(List<ModelAdapterItem> itemsMap){
		itemsMapFrag = new ArrayList<ModelAdapterItem>();
		itemsMapFrag.addAll(itemsMap);
	}

}
