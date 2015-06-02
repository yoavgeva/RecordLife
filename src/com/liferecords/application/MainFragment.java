package com.liferecords.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.liferecords.model.DateAdapterItem;
import com.liferecords.model.MainDataAdapter;
import com.liferecords.model.Model;
import com.liferecords.model.ModelAdapterItem;
import com.liferecords.service.SyncService;
import com.parse.ParseUser;

public class MainFragment extends ListFragment {

	private static final String TAG = MainFragment.class.getSimpleName();
	private static final int LOADER_ID = 1;

	private MainDataAdapter mainAdapter;
	private ExpandableListView lv;
	private List<DateAdapterItem> itemsGroup;
	private HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren;
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

		lv = (ExpandableListView) getActivity().findViewById(R.id.ex_list_view);

		lv.setAdapter(mainAdapter);
		setListShown(false);
		getLoaderManager().initLoader(LOADER_ID, null, loaderCallBack);

		super.onActivityCreated(savedInstanceState);

	}

	public void onStart() {

		registerReceiver();
		/*
		 * if (mainAdapter == null) { Context context = getActivity();
		 * mainAdapter = new MainDataAdapter(context);
		 * setListAdapter((ListAdapter) mainAdapter); } else {
		 * mainAdapter.refresh(); }
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

	private LoaderManager.LoaderCallbacks<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>> loaderCallBack = new LoaderManager.LoaderCallbacks<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>>() {

		@Override
		public Loader<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>> onCreateLoader(
				int id, Bundle args) {

			return new DataBaseLoader(getActivity().getApplicationContext());
		}

		@Override
		public void onLoadFinished(
				Loader<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>> loader,
				Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>> data) {
			//need to set adapter if used
			setListShown(true);

		}

		@Override
		public void onLoaderReset(
				Loader<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>> loader) {

		}
	};

	public static class DataBaseLoader
			extends
			AsyncTaskLoader<Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>> {

		public DataBaseLoader(Context context) {
			super(context);

		}

		@Override
		protected void onStartLoading() {
			forceLoad();
		}

		@Override
		public Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>> loadInBackground() {
			Log.d("check if see", "seen "
					+ ParseUser.getCurrentUser().getUsername());
			Model model = new Model(getContext());
			List<DateAdapterItem> itemsGroup = model.getDateAdapterItems();
			Collections.sort(itemsGroup, new Comparator<DateAdapterItem>() {
				@Override
				public int compare(DateAdapterItem lhs, DateAdapterItem rhs) {
					return rhs.dateWithoutTime - lhs.dateWithoutTime;
				}
			});
			Log.d("check query result", "" + itemsGroup.size());
			Log.d("check query result", "" + itemsGroup.toString());

			List<ModelAdapterItem> itemsChildrenAlpha = model
					.getDataDateAdapterItems();
			Collections.sort(itemsChildrenAlpha,
					new Comparator<ModelAdapterItem>() {
						@Override
						public int compare(ModelAdapterItem lhs,
								ModelAdapterItem rhs) {
							return (rhs.countId - lhs.countId);
						}
					});

			HashMap<DateAdapterItem, List<ModelAdapterItem>> itemsChildren = new HashMap<DateAdapterItem, List<ModelAdapterItem>>();

			for (int i = 0; i < itemsGroup.size(); i++) {
				DateAdapterItem groupObject = (DateAdapterItem) itemsGroup
						.get(i);
				Log.d("check query result of children",
						"" + itemsChildrenAlpha.size());
				Log.d("check query result of children", ""
						+ itemsChildrenAlpha.get(0).toString());
				List<ModelAdapterItem> itemsChildrenBeta = new ArrayList<ModelAdapterItem>();
				for (int j = 0; j < itemsChildrenAlpha.size(); j++) {
					ModelAdapterItem childrenObject = itemsChildrenAlpha.get(j);
					if (groupObject.dateWithoutTime == childrenObject.dateOnly) {
						itemsChildrenBeta.add(childrenObject);
					}
				}
				if (groupObject != null) {
					itemsChildren.put(groupObject, itemsChildrenBeta);
					Log.d("check query result of children", ""
							+ itemsChildren.get(groupObject).toString());
				}
			}
			Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>> twoData = new Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>>(
					itemsGroup, itemsChildren);
			return twoData;
		}

		@Override
		public void deliverResult(
				Pair<List<DateAdapterItem>, HashMap<DateAdapterItem, List<ModelAdapterItem>>> data) {

			super.deliverResult(data);

		}

	}

}