package com.liferecords.model;

import java.util.ArrayList;

import com.liferecords.application.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			LayoutInflater infalter = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = infalter.inflate(R.layout.drawer_item, null,false);
		}
		
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon_drawer);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title_drawer);
		imgIcon.setImageResource(R.drawable.ic_launcher);
		txtTitle.setText(navDrawerItems.get(position).getTitle());
		
		return convertView;
	}

}
