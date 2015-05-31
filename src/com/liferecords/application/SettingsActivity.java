package com.liferecords.application;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {

			SettingsFragment settingsFrag = new SettingsFragment();
			getFragmentManager().beginTransaction()
					.replace(android.R.id.content, settingsFrag)
					.commit();
		}
	}

}
