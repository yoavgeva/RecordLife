package com.liferecords.application;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.liferecords.model.MainDataAdapter;
import com.liferecords.service.SyncService;

public class MainFragment extends ListFragment implements LoaderCallbacks<Cursor>   {

	private static final String TAG = MainFragment.class.getSimpleName();
	private static final int LOADER_ID = 1;


	private MainDataAdapter mainAdapter;
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, intent.getAction());
			if (intent.getAction().equals(SyncService.ACTION)) {
				mainAdapter.refresh();
				return;
			}

		}
	};

	public interface Listener {
		void onMenuLogOut();
	}

	private Listener listener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		getActivity().getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			startActivity(new Intent(getActivity(), SettingsActivity.class));
			return true;
		}
		if (id == R.id.action_logout) {
			listener.onMenuLogOut();
			return true;
		}
		if (id == R.id.database_manager) {

			startActivity(new Intent(getActivity(),
					AndroidDatabaseManager.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		setEmptyText("not working");
		setHasOptionsMenu(true);
		
			ExpandableListView lv = (ExpandableListView) getActivity()
					.findViewById(android.R.id.list);
			Context context = getActivity();
			mainAdapter = new MainDataAdapter(context);
			lv.setAdapter(mainAdapter);
		setListShown(false);
		getLoaderManager().initLoader(LOADER_ID, null, this);
		

		super.onActivityCreated(savedInstanceState);
		
	}

	
	public void onStart() {

		registerReceiver();
	/*	if (mainAdapter == null) {
			Context context = getActivity();
			mainAdapter = new MainDataAdapter(context);
			setListAdapter((ListAdapter) mainAdapter);
		} else {
			mainAdapter.refresh();
		}
*/
		super.onStart();
	}

	private void registerReceiver() {
		Context context = this.getActivity();
		LocalBroadcastManager broadCastManager = LocalBroadcastManager
				.getInstance(context);
		IntentFilter filter = new IntentFilter(SyncService.ACTION);
		broadCastManager.registerReceiver(receiver, filter);
	}

	private void unRegisterReceiever() {
		Context context = this.getActivity();
		LocalBroadcastManager broadCastManager = LocalBroadcastManager
				.getInstance(context);
		broadCastManager.unregisterReceiver(receiver);
	}

	@Override
	public void onStop() {
		unRegisterReceiever();
		super.onStop();
	}

	@Override
	public void onDetach() {

		super.onDetach();
		listener = null;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		try {
			listener = (Listener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement listener");
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader loader = new CursorLoader(getActivity());
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if(data != null && data.getCount() > 0){
			data.moveToFirst();
			
		}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		
		
	}
	
}
