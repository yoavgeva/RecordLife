package com.liferecords.model;


import java.util.List;
import java.util.TreeMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class MainDataAdapter extends BaseExpandableListAdapter {
	
	
	private final Context context;
	private final Model model;	
	private List<DateAdapterItem> itemsGroup;
	private TreeMap<DateAdapterItem,List<ModelAdapterItem>> itemsChildren;
	
	
	public MainDataAdapter(Context context){
		this.context = context;
		this.model = new Model(this.context);
		populate();
	}
	
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void populate(){
		this.itemsGroup = this.model.getDateAdapterItems();
		
		for (int i = 0; i <= this.itemsGroup.size(); i++) {
			DateAdapterItem groupObject = (DateAdapterItem)this.itemsGroup.get(i);
			this.itemsChildren.put(this.itemsGroup.get(i), this.model.getDataDateAdapterItems(groupObject.dateWithoutTime));
		}
		
		
	}

}
