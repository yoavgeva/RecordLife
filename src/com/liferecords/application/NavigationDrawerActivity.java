package com.liferecords.application;

import java.util.ArrayList;

import com.liferecords.model.NavDrawerItem;
import com.liferecords.model.NavDrawerListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toolbar;

public class NavigationDrawerActivity extends AppCompatActivity {

	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private CharSequence drawerTitle;
	String[] navMenuTitles = {"main","settings","feedback","privacy policy"};
	TypedArray navMenuIcons;
	android.support.v7.widget.Toolbar toolDrawer;

	protected void onCreateDrawer() {
		setContentView(R.layout.drawer_layout);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_lay);
		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				toolDrawer, 0, 0) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(drawerTitle);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		
		

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		
		drawerListView = (ListView) findViewById(R.id.left_drawer);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2]));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
		Log.d(NavigationDrawerActivity.class.getSimpleName(), ""
				+ navDrawerItems);
		//navMenuIcons.recycle();
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);

		drawerListView.setAdapter(adapter);
		drawerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				displayView(0);

			}
		});

	}

	/*
	 * @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
	 * 
	 * super.onCreate(savedInstanceState);
	 * setContentView(R.layout.drawer_layout);
	 * 
	 * if (savedInstanceState == null) { displayView(0); } }
	 */

	/*
	 * public void set() { drawerTitle = getTitle(); drawerLayout =
	 * (DrawerLayout) findViewById(R.id.drawer_lay); drawerListView = (ListView)
	 * findViewById(R.id.left_drawer); navDrawerItems = new
	 * ArrayList<NavDrawerItem>(); navMenuTitles =
	 * getResources().getStringArray(R.array.navigation_items);
	 * 
	 * navMenuIcons = getResources()
	 * .obtainTypedArray(R.array.navigation_icons); navDrawerItems.add(new
	 * NavDrawerItem(navMenuTitles[0], navMenuIcons .getResourceId(0, -1)));
	 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
	 * .getResourceId(1, -1))); navDrawerItems.add(new
	 * NavDrawerItem(navMenuTitles[2], navMenuIcons .getResourceId(2, -1)));
	 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
	 * .getResourceId(3, -1)));
	 * Log.d(NavigationDrawerActivity.class.getSimpleName(), "" +
	 * navDrawerItems); navMenuIcons.recycle();
	 * 
	 * 
	 * if (navMenuIcons == null) { for (int i = 0; i < navMenuTitles.length;
	 * i++) { navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
	 * 
	 * } } else { for (int i = 0; i < navMenuTitles.length; i++) {
	 * navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
	 * navMenuIcons.getResourceId(i, -1)));
	 * 
	 * } }
	 * 
	 * drawerListView.setOnItemClickListener(new SlideMenuClickListener());
	 * adapter = new NavDrawerListAdapter(getApplicationContext(),
	 * navDrawerItems); drawerListView.setAdapter(adapter);
	 * 
	 * getActionBar().setDisplayHomeAsUpEnabled(true);
	 * getActionBar().setHomeButtonEnabled(true); // add icon for actionbar
	 * drawer // getSupportActionBar().setIcon(R.drawable.ic_launcher);
	 * 
	 * 
	 * drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
	 * R.string.navigation_drawer_desc_open,
	 * R.string.navigation_drawer_desc_close) { public void onDrawerClosed(View
	 * view) { getSupportActionBar().setTitle(drawerTitle);
	 * supportInvalidateOptionsMenu(); }
	 * 
	 * public void onDrawerOpened(View drawerView) {
	 * getSupportActionBar().setTitle(drawerTitle);
	 * supportInvalidateOptionsMenu(); } };
	 * drawerLayout.setDrawerListener(drawerToggle);
	 * 
	 * 
	 * }
	 */

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			displayView(position);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (drawerLayout.isDrawerOpen(drawerListView)) {
				drawerLayout.closeDrawer(drawerListView);
			} else {
				drawerLayout.openDrawer(drawerListView);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerListView);

		return super.onPrepareOptionsMenu(menu);
	}

	private void displayView(int position) {
		switch (position) {
		case 0:
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;

		}

		drawerListView.setItemChecked(position, true);
		drawerListView.setSelection(position);
		drawerLayout.closeDrawer(drawerListView);

	}

	@Override
	public void setTitle(CharSequence title) {
		drawerTitle = title;
		getActionBar().setTitle(drawerTitle);
	}

	@Override
	protected void onPostCreate(@Nullable Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		drawerToggle.onConfigurationChanged(newConfig);
	}

}
