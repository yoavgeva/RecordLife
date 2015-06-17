package com.liferecords.application;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.liferecords.service.MainService;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	MainService mainService;

	public static final String KEY_LIST_PREFERENCE = "listTiming";
	public static final String KEY_INTERVAL_TIME = "timeInterval";
	public static final String KEY_EDIT_NAME_PREFERENCE = "editNamePref";

	private ListPreference listInstanceTimes;
	private EditTextPreference editPref;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_LIST_PREFERENCE)) {
			listInstanceTimes.setSummary("Current Value is "
					+ listInstanceTimes.getEntry().toString());
			long setTi = Long.parseLong(listInstanceTimes.getValue());

			updateTimingPreference(setTi);
			Log.d(SettingsFragment.class.getSimpleName(), "" + setTi);
		}
		if (key.equals(KEY_EDIT_NAME_PREFERENCE)) {
			editPref.setSummary(editPref.getText());
			String namePre = editPref.getText();
			updateNamePreference(namePre);
			Log.d(SettingsFragment.class.getSimpleName(), namePre);
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_page);
		listInstanceTimes = (ListPreference) getPreferenceScreen()
				.findPreference(KEY_LIST_PREFERENCE);
		editPref = (EditTextPreference) getPreferenceScreen().findPreference(
				KEY_EDIT_NAME_PREFERENCE);
		checkEditNameEmpty();

	}

	private void checkEditNameEmpty() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String stra = pref.getString(KEY_EDIT_NAME_PREFERENCE, null);
		if(stra != null ){
			editPref.setSummary(stra);
		}
		
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

	private void updateTimingPreference(long timeValue) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(KEY_INTERVAL_TIME, timeValue);
		editor.commit();
	}

	private void updateNamePreference(String name) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(KEY_EDIT_NAME_PREFERENCE, name);
		editor.commit();
	}

}
