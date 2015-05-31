package com.liferecords.application;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	public static final String KEY_LIST_PREFERENCE = "listTiming";
	public static final String KEY_INTERVAL_TIME = "timeInterval";

	private ListPreference listInstanceTimes;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_LIST_PREFERENCE)) {
			listInstanceTimes.setSummary("Current Value is "
					+ listInstanceTimes.getEntry().toString());
			int setTime = Integer.parseInt(listInstanceTimes.getValue());
			updateTimingPreference(setTime);
			Log.d(getActivity().getClass().getSimpleName(), "" + setTime);
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
	
	private void updateTimingPreference(int timeValue){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(KEY_INTERVAL_TIME, timeValue);
		editor.commit();
	}
}
