package com.liferecords.application;

import com.parse.Parse;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class DispatchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "eyqKhSsclg8b8tzuDn9CexsRhFTI3CQlKNKbZe8n",
				"OVA2i67H7LlNNcUQeZffztzWxTcJJmsxrKwRgaro");
		if(ParseUser.getCurrentUser() != null){
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			
			
		} else {
			Intent intent = new Intent(this, SignUpOrLoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	
}
