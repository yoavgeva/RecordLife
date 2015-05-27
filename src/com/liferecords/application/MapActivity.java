package com.liferecords.application;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.liferecords.model.ModelAdapterItem;

public class MapActivity extends Activity {

	private GoogleMap map;
	private List<ModelAdapterItem> itemsMapFrag = new ArrayList<ModelAdapterItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_fragment);
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			itemsMapFrag = bundle.getParcelableArrayList("items");
			Log.d("amout of items in map", "" + itemsMapFrag.size());
		}

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.location_map)).getMap();
		if (map != null) {
			setUpMap();

		}

	}

	@SuppressLint("InflateParams")
	private void setUpMap() {
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(true);
		View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.custom_marker_layout, null);
		TextView timeText = (TextView) marker.findViewById(R.id.time_text_map);
		timeText.setTextColor(Color.BLACK);
		ImageView motionImage = (ImageView) marker
				.findViewById(R.id.motion_picture_map);
		ImageView batteryImage = (ImageView) marker
				.findViewById(R.id.battery_picture_map);
		
		setMarkerOnMap(motionImage, batteryImage, marker);

		setPolylineOnMap();

		LatLng FirstLocation = new LatLng(itemsMapFrag.get(0).latitude,
				itemsMapFrag.get(0).longitude);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(FirstLocation, 15));

	}

	private void setMarkerOnMap(ImageView motionImage, ImageView batteryImage,
			View marker) {
		for (int i = 0; i < itemsMapFrag.size(); i++) {
			setMotionPicture(motionImage, itemsMapFrag.get(i));
			setBatteryPicture(batteryImage, itemsMapFrag.get(i));
			map.addMarker(new MarkerOptions()
					.position(
							new LatLng(itemsMapFrag.get(i).latitude,
									itemsMapFrag.get(i).longitude))
					.title("title")
					.snippet("snippet")
					.icon(BitmapDescriptorFactory
							.fromBitmap(createDrawableFromView(this, marker))));
		}

	}

	private void setPolylineOnMap() {
		PolylineOptions options = new PolylineOptions().width(5)
				.color(Color.BLUE).geodesic(true);
		for (int i = 0; i < itemsMapFrag.size(); i++) {

			LatLng point = new LatLng(itemsMapFrag.get(i).latitude,
					itemsMapFrag.get(i).longitude);
			options.add(point);

		}
		map.addPolyline(options);

	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;

	}

	private void setMotionPicture(ImageView imgMotion,
			ModelAdapterItem childView) {
		if (childView.motion == 0) {

			setPictureImage(R.drawable.ic_motion_driving, imgMotion);
		} else if (childView.motion == 1) {

			setPictureImage(R.drawable.ic_motion_cycling, imgMotion);

		} else if (childView.motion == 8) {

			setPictureImage(R.drawable.ic_motion_running, imgMotion);

		} else if (childView.motion == 7 || childView.motion == 2) {

			setPictureImage(R.drawable.ic_motion_walking, imgMotion);

		} else {

			setPictureImage(R.drawable.ic_motion_standing, imgMotion);

		}
	}

	private void setPictureImage(int resDrawable, ImageView imgView) {
		Bitmap bitPic = BitmapFactory.decodeResource(getResources(),
				resDrawable);
		imgView.setImageBitmap(bitPic);
	}

	private void setBatteryPicture(ImageView imgBattery,
			ModelAdapterItem childView) {
		if ((childView.batteryPrecent < 200000) && (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_0battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 200000)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_0battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 400000)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_20battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 400000)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_20battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 600000)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_40battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 600000)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_40battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 800000)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_60battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 800000)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_60battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 1000000)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_80battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 1000000)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_80battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent == 1000000)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_100battery_charging, imgBattery);
		} else {
			setPictureImage(R.drawable.ic_100battery_discharging, imgBattery);
		}

	}

}
