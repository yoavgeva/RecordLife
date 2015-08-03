package com.liferecords.app;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.liferecords.model.PostObjectsParse;
import com.parse.Parse;
import com.parse.ParseObject;

public class LifeRecordsApp extends Application {
	

	private static SharedPreferences preferences;


	public LifeRecordsApp() {

	}

	@Override
	public void onCreate() {
		ParseObject.registerSubclass(PostObjectsParse.class);
		Parse.initialize(this, "eyqKhSsclg8b8tzuDn9CexsRhFTI3CQlKNKbZe8n",
				"OVA2i67H7LlNNcUQeZffztzWxTcJJmsxrKwRgaro");
		preferences = getSharedPreferences("com.liferecords.application",
				Context.MODE_PRIVATE);
		super.onCreate();
	}
}