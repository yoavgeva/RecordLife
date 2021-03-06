package com.liferecords.model;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liferecords.app.R;

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
			convertView = infalter.inflate(R.layout.drawer_item, parent,false);
		}
		
		ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon_drawer);
		TextView txtTitle = (TextView) convertView.findViewById(R.id.title_drawer);
		
		imgIcon.setImageResource(navDrawerItems.get(position).getIcon());
		setTextDesignDrawer(txtTitle);
		txtTitle.setText(navDrawerItems.get(position).getTitle());
		
		return convertView;
	}
	
	private void setTextDesignDrawer(TextView txtTitle) {
		txtTitle.setTextColor(Color.parseColor("#1e88e5"));
		txtTitle.setTypeface(setTypeFaceRoboto());
		
	}

	private Typeface setTypeFaceRoboto() {
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"roboto_regular.ttf");
		return type;
	}

}
