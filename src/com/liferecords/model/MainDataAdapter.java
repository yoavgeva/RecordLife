package com.liferecords.model;


import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
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
		
		final ModelAdapterItem childText =  (ModelAdapterItem) getChild(groupPosition, childPosition);
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.listrow_details, parent,false);
		}
		ModelAdapterItem item = getChildItems(childPosition);
		//Log.d("check query result of children", "" + item.toString()  );
		TextView  txtChild = (TextView) convertView.findViewById(R.id.textViewDetails);
		txtChild.setText(childText.address);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
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
		//this.itemsChildrenAlpha = this.model.getDataDateAdapterItems();
		//Log.d("check if see", "seen children alpha " + this.itemsChildrenAlpha.size());
		this.itemsChildrenAlpha = this.model.getDataDateAdapterItems();
		Collections.sort(itemsChildrenAlpha, new Comparator<ModelAdapterItem>() {

			@Override
			public int compare(ModelAdapterItem lhs, ModelAdapterItem rhs) {
				
				return (int) (rhs.recordTime-lhs.recordTime);
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
