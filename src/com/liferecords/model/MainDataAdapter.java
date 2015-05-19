package com.liferecords.model;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.liferecords.application.R;
import com.parse.ParseUser;

public class MainDataAdapter extends BaseExpandableListAdapter {


	private final Context context;
	private final Model model;	
	private List<DateAdapterItem> itemsGroup;
	private List<ModelAdapterItem> itemsChildrenAlpha;
	private HashMap<DateAdapterItem,List<ModelAdapterItem>> itemsChildren;


	public MainDataAdapter(Context context){
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

		return this.itemsChildren.get(this.itemsGroup.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {

		return this.itemsGroup.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return this.itemsChildren.get(this.itemsGroup.get(groupPosition)).get(childPosition);
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

	public DateAdapterItem getGroupItems(int position){
		return itemsGroup.get(position);
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow_group, parent,false);
		}
		DateAdapterItem item = getGroupItems(groupPosition);
		CheckedTextView txtGroup = (CheckedTextView) convertView.findViewById(R.id.checked_textview_group);
		txtGroup.setText("" + item.dateWithoutTime);
		return convertView;
	}

	public ModelAdapterItem getChildItems(int position){
		return (ModelAdapterItem) itemsChildren.get(position);
		
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		final ModelAdapterItem childView =  (ModelAdapterItem) getChild(groupPosition, childPosition);
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow_details, parent,false);
		}
		setChildView(childView, convertView);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	
private void setChildView(ModelAdapterItem childView,View convertView){
		TextView txtAdress = (TextView) convertView.findViewById(R.id.textViewDetails);
		txtAdress.setText(childView.address);
		TextView txtTime = (TextView) convertView.findViewById(R.id.textView_details_time);
		String instanceTime = childView.recordTime.substring(9, 10) + ":" + childView.recordTime.substring(11, 12) + ":" + childView.recordTime.substring(13, 14);
		txtTime.setText(instanceTime);
		TextView txtMotion = (TextView) convertView.findViewById(R.id.textView_details_battery);
		setMotionPicture(txtMotion,childView);
		TextView txtType = (TextView) convertView.findViewById(R.id.textView_details_type);
		setTypePicture(txtType,childView);
		
	}

private void setTypePicture(TextView txtType, ModelAdapterItem childView) {
	// TODO Auto-generated method stub
	
}


private void setMotionPicture(TextView txtMotion,ModelAdapterItem childView){
	if(childView.motion == 0){
		Bitmap motionIconBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motion_driving);
		 Drawable motionIcon = new BitmapDrawable(context.getResources(), motionIconBit);
		 txtMotion.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null, null, null);
		 txtMotion.setText(context.getResources().getText(R.string.details_motion_driving));
	}
	else if (childView.motion == 1){
		Bitmap motionIconBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motion_cycling);
		 Drawable motionIcon = new BitmapDrawable(context.getResources(), motionIconBit);
		 txtMotion.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null, null, null);
		 txtMotion.setText(context.getResources().getText(R.string.details_motion_cycling));
		
	} else if (childView.motion == 8){
		Bitmap motionIconBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motion_running);
		 Drawable motionIcon = new BitmapDrawable(context.getResources(), motionIconBit);
		 txtMotion.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null, null, null);
		 txtMotion.setText(context.getResources().getText(R.string.details_motion_running));
		
	} else if (childView.motion == 7 || childView.motion == 2){
		Bitmap motionIconBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motion_walking);
		 Drawable motionIcon = new BitmapDrawable(context.getResources(), motionIconBit);
		 txtMotion.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null, null, null);
		 txtMotion.setText(context.getResources().getText(R.string.details_motion_walking));
		 
	} else {
		Bitmap motionIconBit = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_motion_standing);
		 Drawable motionIcon = new BitmapDrawable(context.getResources(), motionIconBit);
		 txtMotion.setCompoundDrawablesWithIntrinsicBounds(motionIcon, null, null, null);
		 txtMotion.setText(context.getResources().getText(R.string.details_motion_standing));
	
		
	} 
}


	private void populate(){
		
		Log.d("check if see", "seen " + ParseUser.getCurrentUser().getUsername());
		this.itemsGroup = this.model.getDateAdapterItems();
		Collections.sort(itemsGroup, new Comparator<DateAdapterItem>() {
			@Override
			public int compare(DateAdapterItem lhs, DateAdapterItem rhs) {
				return lhs.dateWithoutTime - rhs.dateWithoutTime;
			}
		});
		Log.d("check query result", "" + this.itemsGroup.size()  );
		Log.d("check query result", "" + this.itemsGroup.toString()  );
		
		this.itemsChildrenAlpha = this.model.getDataDateAdapterItems();
		Collections.sort(itemsChildrenAlpha, new Comparator<ModelAdapterItem>() {
			@Override
			public int compare(ModelAdapterItem lhs, ModelAdapterItem rhs) {				
				return  (lhs.countId-rhs.countId);
			}		
		});
		
		this.itemsChildren = new HashMap<DateAdapterItem, List<ModelAdapterItem>>();
		for (int i = 0; i < this.itemsGroup.size(); i++) {
			 DateAdapterItem groupObject = (DateAdapterItem)this.itemsGroup.get(i);
			Log.d("check query result of children", "" + this.itemsChildrenAlpha.size()  );
			Log.d("check query result of children", "" + this.itemsChildrenAlpha.get(0).toString()  );
			if(groupObject != null){
				this.itemsChildren.put(groupObject, this.itemsChildrenAlpha);
				Log.d("check query result of children", "" + this.itemsChildren.get(groupObject).toString()  );
			}					
		}	
	}
	
	
}
