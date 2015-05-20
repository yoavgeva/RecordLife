package com.liferecords.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liferecords.application.R;
import com.parse.ParseUser;

public class MainDataAdapter extends BaseExpandableListAdapter {

	private final Context context;
	private final Model model;
	private List<DateAdapterItem> itemsGroup;
	private List<ModelAdapterItem> itemsChildrenAlpha;
	private List<ModelAdapterItem> itemsChildrenBeta;
	private HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren;

	public MainDataAdapter(Context context) {
		this.context = context;
		this.model = new Model(this.context);
		populate();
	}

	@Override
	public int getGroupCount() {

		return this.itemsGroup.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		return this.itemsChildren.get(this.itemsGroup.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return this.itemsGroup.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return this.itemsChildren.get(this.itemsGroup.get(groupPosition)).get(
				childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public DateAdapterItem getGroupItems(int position) {
		return itemsGroup.get(position);
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow_group, parent,
					false);
		}
		DateAdapterItem item = getGroupItems(groupPosition);
		CheckedTextView txtGroup = (CheckedTextView) convertView
				.findViewById(R.id.checked_textview_group);
		txtGroup.setText("" + item.dateWithoutTime);
		return convertView;
	}

	public ModelAdapterItem getChildItems(int position) {
		return (ModelAdapterItem) itemsChildren.get(position);

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final ModelAdapterItem childView = (ModelAdapterItem) getChild(
				groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow_details, parent,
					false);
		}
		setChildView(childView, convertView);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private void setChildView(ModelAdapterItem childView, View convertView) {
		TextView txtAdress = (TextView) convertView
				.findViewById(R.id.textViewDetails);
		txtAdress.setText(childView.address);
		TextView txtTime = (TextView) convertView
				.findViewById(R.id.textView_details_time);
		setTimeText(txtTime, childView);
		TextView txtMotion = (TextView) convertView
				.findViewById(R.id.textView_details_motion);
		setMotionPicture(txtMotion, childView);
		TextView txtType = (TextView) convertView
				.findViewById(R.id.textView_details_type);
		setTypePicture(txtType, childView);
		ImageView imgBattery = (ImageView) convertView
				.findViewById(R.id.imageView_details_battery);
		setBatteryPicture(imgBattery, childView);

	}

	private void setTimeText(TextView txtTime, ModelAdapterItem childView) {
		String instanceTime = childView.recordTime.substring(9, 11) + ":"
				+ childView.recordTime.substring(11, 13) + ":"
				+ childView.recordTime.substring(13, 15);
		txtTime.setText(instanceTime);

	}

	private void setBatteryPicture(ImageView imgBattery,
			ModelAdapterItem childView) {
		if ((childView.batteryPrecent < 20) && (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_0battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 20)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_0battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 40) && (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_20battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 40)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_20battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 60) && (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_40battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 60)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_40battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 80) && (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_60battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 80)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_60battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent < 100)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_80battery_charging, imgBattery);
		} else if ((childView.batteryPrecent < 100)
				&& (!childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_80battery_discharging, imgBattery);
		} else if ((childView.batteryPrecent == 100)
				&& (childView.batteryCharge)) {
			setPictureImage(R.drawable.ic_100battery_charging, imgBattery);
		} else {
			setPictureImage(R.drawable.ic_100battery_discharging, imgBattery);
		}

	}

	private void setPictureImage(int resDrawable, ImageView imgView) {
		Bitmap bitPic = BitmapFactory.decodeResource(context.getResources(),
				resDrawable);
		imgView.setImageBitmap(bitPic);
	}

	private void setTypePicture(TextView txtType, ModelAdapterItem childView) {
		if (childView.type.equals("airport")) {
			setPictureAndText(R.drawable.ic_type_airport,
					R.string.details_type_airport, txtType);
		} else if (childView.type.equals("bus_station")
				|| childView.type.equals("train_station")
				|| childView.type.equals("transit_station")) {
			setPictureAndText(R.drawable.ic_type_bus,
					R.string.details_type_bus, txtType);
		} else if (childView.type.equals("park")
				|| childView.type.equals("natural_feature")) {
			setPictureAndText(R.drawable.ic_type_park,
					R.string.details_type_park, txtType);
		} else if (childView.type.equals("parking")) {
			setPictureAndText(R.drawable.ic_type_parking,
					R.string.details_type_parking, txtType);
		} else {
			setPictureAndText(R.drawable.ic_type_road,
					R.string.details_type_road, txtType);
		}

	}

	private void setMotionPicture(TextView txtMotion, ModelAdapterItem childView) {
		if (childView.motion == 0) {

			setPictureAndText(R.drawable.ic_motion_driving,
					R.string.details_motion_driving, txtMotion);
		} else if (childView.motion == 1) {

			setPictureAndText(R.drawable.ic_motion_cycling,
					R.string.details_motion_cycling, txtMotion);

		} else if (childView.motion == 8) {

			setPictureAndText(R.drawable.ic_motion_running,
					R.string.details_motion_running, txtMotion);

		} else if (childView.motion == 7 || childView.motion == 2) {

			setPictureAndText(R.drawable.ic_motion_walking,
					R.string.details_motion_walking, txtMotion);

		} else {

			setPictureAndText(R.drawable.ic_motion_standing,
					R.string.details_motion_standing, txtMotion);

		}
	}

	private void setPictureAndText(int resDrawable, int resString,
			TextView txtSubject) {
		Bitmap motionIconBit = BitmapFactory.decodeResource(
				context.getResources(), resDrawable);
		Drawable motionIcon = new BitmapDrawable(context.getResources(),
				motionIconBit);
		txtSubject.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null,
				null, null);
		txtSubject.setText(context.getResources().getText(resString));
	}

	private void populate() {

		Log.d("check if see", "seen "
				+ ParseUser.getCurrentUser().getUsername());
		this.itemsGroup = this.model.getDateAdapterItems();
		Collections.sort(itemsGroup, new Comparator<DateAdapterItem>() {
			@Override
			public int compare(DateAdapterItem lhs, DateAdapterItem rhs) {
				return lhs.dateWithoutTime - rhs.dateWithoutTime;
			}
		});
		Log.d("check query result", "" + this.itemsGroup.size());
		Log.d("check query result", "" + this.itemsGroup.toString());

		this.itemsChildrenAlpha = this.model.getDataDateAdapterItems();
		Collections.sort(itemsChildrenAlpha,
				new Comparator<ModelAdapterItem>() {
					@Override
					public int compare(ModelAdapterItem lhs,
							ModelAdapterItem rhs) {
						return (rhs.countId - lhs.countId);
					}
				});

		this.itemsChildren = new HashMap<DateAdapterItem, List<ModelAdapterItem>>();

		for (int i = 0; i < this.itemsGroup.size(); i++) {
			DateAdapterItem groupObject = (DateAdapterItem) this.itemsGroup
					.get(i);
			Log.d("check query result of children", ""
					+ this.itemsChildrenAlpha.size());
			Log.d("check query result of children", ""
					+ this.itemsChildrenAlpha.get(0).toString());
			this.itemsChildrenBeta = new ArrayList<ModelAdapterItem>();
			for (int j = 0; j < this.itemsChildrenAlpha.size(); j++) {
				ModelAdapterItem childrenObject = this.itemsChildrenAlpha
						.get(j);
				if (groupObject.dateWithoutTime == childrenObject.dateOnly) {
					this.itemsChildrenBeta.add(childrenObject);
				}
			}
			if (groupObject != null) {
				this.itemsChildren.put(groupObject, this.itemsChildrenBeta);
				Log.d("check query result of children", ""
						+ this.itemsChildren.get(groupObject).toString());
			}
		}
	}

}
