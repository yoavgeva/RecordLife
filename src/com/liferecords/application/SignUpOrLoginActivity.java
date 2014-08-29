package com.liferecords.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SignUpOrLoginActivity extends Activity {
	
	Button loginButton,signupButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_or_login);
		setScreenDesign();
		loginButton = (Button) findViewById(R.id.button_login);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpOrLoginActivity.this, LoginActivity.class);
				startActivity(intent);
				
			}
		});
		signupButton = (Button) findViewById(R.id.button_signup_login);
		signupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpOrLoginActivity.this, SignUpActivity.class);
				startActivity(intent);
				
				
			}
		});
	}

	private void setScreenDesign(){
		getActionBar().hide();
	}
}
