package com.liferecords.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liferecords.application.MapActivity;
import com.liferecords.application.R;
import com.parse.ParseUser;

public class MainDataAdapter extends BaseExpandableListAdapter {

	private final Context context;
	private final Model model;
	private List<DateAdapterItem> itemsGroup;
	//private List<ModelAdapterItem> itemsChildrenAlpha;
	//private List<ModelAdapterItem> itemsChildrenBeta;
	private HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren;

	// added haspmap and list<dateadapteritem>
	public MainDataAdapter(Context context, List<DateAdapterItem> itemsGroup,
			HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren) {
		this.context = context;
		this.model = new Model(this.context);
		
		this.itemsGroup = itemsGroup;
		
		this.itemsChildren = itemsChildren;

		// populate();
	}

	/*
	 * public void setData(List<DateAdapterItem>
	 * itemsGroupSet,HashMap<DateAdapterItem, List<ModelAdapterItem>>
	 * itemsChildrenSet){ if(itemsGroupSet != null || itemsChildrenSet != null){
	 * itemsGroup = itemsGroupSet; itemsChildren = itemsChildrenSet; } }
	 */

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

		TextView txtGroup = (TextView) convertView
				.findViewById(R.id.checked_textview_group);
		setGroupContent(txtGroup, convertView, groupPosition);

		return convertView;
	}

	private void setGroupContent(TextView txtGroup, View convertView,
			int groupPosition) {
		final DateAdapterItem item = getGroupItems(groupPosition);
		setDayDesign(txtGroup, item);

		ImageView imageGroupMap = (ImageView) convertView
				.findViewById(R.id.group_map_icon);
		setPictureImage(R.drawable.ic_map, imageGroupMap);
		imageGroupMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<ModelAdapterItem> itemsMap = new ArrayList<ModelAdapterItem>();
				ModelAdapterItem addedItem = new ModelAdapterItem();
				addedItem = itemsChildren.get(item).get(0);
				itemsMap.add(addedItem);

				for (int i = 1; i < itemsChildren.get(item).size(); i++) {

					float distance = distanceBetween(addedItem.latitude,
							addedItem.longitude,
							itemsChildren.get(item).get(i).latitude,
							itemsChildren.get(item).get(i).longitude);
					if (distance >= 100) {
						addedItem = itemsChildren.get(item).get(i);
						itemsMap.add(addedItem);
					}
				}
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("items",
						(ArrayList<? extends Parcelable>) itemsMap);
				Intent intent = new Intent(context, MapActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});

	}

	private void setDayDesign(TextView txtGroup, DateAdapterItem item) {
		String date = Integer.toString(item.timeCreated);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		Calendar calendar = new GregorianCalendar(year, month - 1, day);
		Log.d(MainDataAdapter.class.getSimpleName(), "" + year + month + day);
		String dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
				Calendar.LONG, Locale.getDefault());
		String monthName = calendar.getDisplayName(Calendar.MONTH,
				Calendar.LONG, Locale.getDefault());
		txtGroup.setText(dayName + ", " + monthName + " " + day);
		txtGroup.setTypeface(setTypeFaceRoboto());
		txtGroup.setTextSize(18f);
		txtGroup.setTextColor(Color.parseColor("#3066BC"));

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
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ModelAdapterItem addedItem = new ModelAdapterItem();
				addedItem = childView;
				Bundle bundle = new Bundle();
				bundle.putParcelable("item", addedItem);
				Log.d(MainDataAdapter.class.getSimpleName(), "" + addedItem);

				Intent intent = new Intent(context, MapActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);

			}
		});
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
				.findViewById(R.id.textviewdetails);
		setAdressDesign(childView, txtAdress);

		TextView txtTime = (TextView) convertView
				.findViewById(R.id.textview_details_time);
		setTimeText(txtTime, childView);
		TextView txtMotion = (TextView) convertView
				.findViewById(R.id.textview_details_motion);
		setMotionPicture(txtMotion, childView);
		TextView txtType = (TextView) convertView
				.findViewById(R.id.textView_details_type);
		setTypeText(txtType, childView);
		TextView imgBattery = (TextView) convertView
				.findViewById(R.id.textview_details_battery);
		setBatteryPicture(imgBattery, childView);
		ImageView imgType = (ImageView) convertView
				.findViewById(R.id.imageview_details_type);
		setTypeImage(imgType, childView);
		View viewBorder = convertView.findViewById(R.id.borderline_details);
		setBorderLinesDesign(viewBorder);

	}

	private void setBorderLinesDesign(View viewBorder) {
		viewBorder.setBackgroundColor(Color.parseColor("#CED3E8"));

	}

	private void setAdressDesign(ModelAdapterItem childView, TextView txtAdress) {
		txtAdress.setText(childView.address);
		txtAdress.setTextSize(14f);
		txtAdress.setTypeface(setTypeFaceRoboto());

	}

	private Typeface setTypeFaceRoboto() {
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"roboto_regular.ttf");
		return type;
	}

	private void setTimeText(TextView txtTime, ModelAdapterItem childView) {
		String instanceTime = childView.recordTime.substring(9, 11) + ":"
				+ childView.recordTime.substring(11, 13) + ":"
				+ childView.recordTime.substring(13, 15);
		txtTime.setText(instanceTime);
		txtTime.setTypeface(setTypeFaceRoboto());

	}

	private void setBatteryPicture(TextView imgBattery,
			ModelAdapterItem childView) {
		if ((childView.batteryPrecent < 200000) && (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_0battery_charging,
					imgBattery, childView);
		} else if ((childView.batteryPrecent < 200000)
				&& (!childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_0battery_discharging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 400000)
				&& (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_20battery_charging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 400000)
				&& (!childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_20battery_discharging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 600000)
				&& (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_40battery_charging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 600000)
				&& (!childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_40battery_discharging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 800000)
				&& (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_60battery_charging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 800000)
				&& (!childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_60battery_discharging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 1000000)
				&& (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_80battery_charging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent < 1000000)
				&& (!childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_80battery_discharging,
					imgBattery, childView);

		} else if ((childView.batteryPrecent == 1000000)
				&& (childView.batteryCharge)) {
			setPictureAndTextBattery(R.drawable.ic_100battery_charging,
					imgBattery, childView);

		} else {
			setPictureAndTextBattery(R.drawable.ic_100battery_discharging,
					imgBattery, childView);
		}

	}

	private void setPictureImage(int resDrawable, ImageView imgView) {
		Bitmap bitPic = BitmapFactory.decodeResource(context.getResources(),
				resDrawable);
		imgView.setImageBitmap(bitPic);
	}

	private void setTypeText(TextView txtType, ModelAdapterItem childView) {
		if (childView.type.equals("airport")) {

			txtType.setText(R.string.details_type_airport);
		} else if (childView.type.equals("bus_station")
				|| childView.type.equals("train_station")
				|| childView.type.equals("transit_station")) {

			txtType.setText(R.string.details_type_bus);
		} else if (childView.type.equals("park")
				|| childView.type.equals("natural_feature")) {

			txtType.setText(R.string.details_type_park);
		} else if (childView.type.equals("parking")) {

			txtType.setText(R.string.details_type_parking);
		} else {

			txtType.setText(R.string.details_type_road);
		}

		txtType.setTextColor(Color.parseColor("#8D9AA0"));

	}

	private void setTypeImage(ImageView imgType, ModelAdapterItem childView) {
		if (childView.type.equals("airport")) {

			setPicture(R.drawable.ic_type_airport, imgType);
		} else if (childView.type.equals("bus_station")
				|| childView.type.equals("train_station")
				|| childView.type.equals("transit_station")) {

			setPicture(R.drawable.ic_type_bus, imgType);
		} else if (childView.type.equals("park")
				|| childView.type.equals("natural_feature")) {

			setPicture(R.drawable.ic_type_park, imgType);
		} else if (childView.type.equals("parking")) {

			setPicture(R.drawable.ic_type_parking, imgType);
		} else {

			setPicture(R.drawable.ic_type_road, imgType);
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

	private void setPicture(int resDrawable, ImageView image) {
		Bitmap motionIconBit = BitmapFactory.decodeResource(
				context.getResources(), resDrawable);

		image.setImageBitmap(motionIconBit);
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
		txtSubject.setTypeface(setTypeFaceRoboto());
	}

	private void setPictureAndTextBattery(int resDrawable, TextView txtSubject,
			ModelAdapterItem childView) {
		Bitmap motionIconBit = BitmapFactory.decodeResource(
				context.getResources(), resDrawable);
		Drawable motionIcon = new BitmapDrawable(context.getResources(),
				motionIconBit);
		txtSubject.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null,
				null, null);
		setBatteryText(txtSubject, childView);
	}

	private void setBatteryText(TextView txtSubject, ModelAdapterItem childView) {
		String batteryPrecentage = Integer.toString(childView.batteryPrecent);
		if (batteryPrecentage.equals("1000000")) {
			txtSubject.setText("100%");
		} else {
			txtSubject.setText(batteryPrecentage.substring(0, 2) + "%");
		}
		txtSubject.setTypeface(setTypeFaceRoboto());

	}

	public float distanceBetween(double startlangtitude, double startlongitude,
			double endlangitude, double endlongitude) {
		float[] results = new float[2];
		Location.distanceBetween(startlangtitude, startlongitude, endlangitude,
				endlongitude, results);
		float distance = results[0];
		return distance;
	}

	/*
	 * private void populate() {
	 * 
	 * Log.d("check if see", "seen " +
	 * ParseUser.getCurrentUser().getUsername()); this.itemsGroup =
	 * this.model.getDateAdapterItems(); Collections.sort(itemsGroup, new
	 * Comparator<DateAdapterItem>() {
	 * 
	 * @Override public int compare(DateAdapterItem lhs, DateAdapterItem rhs) {
	 * return rhs.dateWithoutTime - lhs.dateWithoutTime; } });
	 * Log.d("check query result", "" + this.itemsGroup.size());
	 * Log.d("check query result", "" + this.itemsGroup.toString());
	 * 
	 * this.itemsChildrenAlpha = this.model.getDataDateAdapterItems();
	 * Collections.sort(itemsChildrenAlpha, new Comparator<ModelAdapterItem>() {
	 * 
	 * @Override public int compare(ModelAdapterItem lhs, ModelAdapterItem rhs)
	 * { return (rhs.countId - lhs.countId); } });
	 * 
	 * this.itemsChildren = new HashMap<DateAdapterItem,
	 * List<ModelAdapterItem>>();
	 * 
	 * for (int i = 0; i < this.itemsGroup.size(); i++) { DateAdapterItem
	 * groupObject = (DateAdapterItem) this.itemsGroup .get(i);
	 * Log.d("check query result of children", "" +
	 * this.itemsChildrenAlpha.size()); Log.d("check query result of children",
	 * "" + this.itemsChildrenAlpha.get(0).toString()); this.itemsChildrenBeta =
	 * new ArrayList<ModelAdapterItem>(); for (int j = 0; j <
	 * this.itemsChildrenAlpha.size(); j++) { ModelAdapterItem childrenObject =
	 * this.itemsChildrenAlpha .get(j); if (groupObject.dateWithoutTime ==
	 * childrenObject.dateOnly) { this.itemsChildrenBeta.add(childrenObject); }
	 * } if (groupObject != null) { this.itemsChildren.put(groupObject,
	 * this.itemsChildrenBeta); Log.d("check query result of children", "" +
	 * this.itemsChildren.get(groupObject).toString()); } } }
	 */

	public void swapData(List<DateAdapterItem> itemsGroup,
			HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren) {
		this.itemsGroup.clear();
		this.itemsGroup.addAll(itemsGroup);
		this.itemsChildren.clear();
		this.itemsChildren.putAll(itemsChildren);
		notifyDataSetChanged();
	}

}
