package com.liferecords.application;

import com.liferecords.service.MainService;
import com.liferecords.service.MainService.LocalBinder;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	MainService mainService;

	public static final String KEY_LIST_PREFERENCE = "listTiming";
	public static final String KEY_INTERVAL_TIME = "timeInterval";

	private ListPreference listInstanceTimes;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_LIST_PREFERENCE)) {
			listInstanceTimes.setSummary("Current Value is "
					+ listInstanceTimes.getEntry().toString());
			long setTi = Long.parseLong(listInstanceTimes.getValue());
			
			updateTimingPreference(setTi);
			Log.d(getActivity().getClass().getSimpleName(), "" + setTi);
		}

		
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_page);
		listInstanceTimes = (ListPreference) getPreferenceScreen()
				.findPreference(KEY_LIST_PREFERENCE);
		
	}

	@Override
	public void onResume() {

		super.onResume();
		listInstanceTimes.setSummary("Current Value is "
				+ listInstanceTimes.getEntry().toString());
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {

		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void updateTimingPreference(long timeValue){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(KEY_INTERVAL_TIME, timeValue);
		editor.commit();
	}
	
	
}
