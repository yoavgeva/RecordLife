package com.liferecords.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liferecords.tools.TextViewAnimator;

public class SignUpOrLoginActivity extends Activity {

	Button loginButton, signupButton;
	TextView mottoTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_or_login);
		setScreenDesign();
		loginButton = (Button) findViewById(R.id.button_login);
		mottoTextView = (TextView) findViewById(R.id.motto_text);
		setGypsyAnimation();
		setMottoDesign();

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpOrLoginActivity.this,
						LoginActivity.class);
				startActivity(intent);

			}
		});
		signupButton = (Button) findViewById(R.id.button_signup_login);
		signupButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignUpOrLoginActivity.this,
						SignUpActivity.class);
				startActivity(intent);

			}
		});
	}

	private void setScreenDesign() {
		// getActionBar().hide();
		setLoginButtonDesign();
		setSignupButtonDesign();
	}

	
	private Typeface setTypeFacerobotocondosedlight() {
		Typeface type = Typeface.createFromAsset(getAssets(),
				"robotocondensed_light.ttf");
		return type;
	}

	private void setLoginButtonDesign() {
		loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setTypeface(setTypeFacerobotocondosedlight(), Typeface.NORMAL);
		loginButton.setTextSize(26f);
		loginButton.setTextColor(Color.parseColor("#FCFEFC"));
	}

	private void setSignupButtonDesign() {
		signupButton = (Button) findViewById(R.id.button_signup_login);
		signupButton.setTypeface(setTypeFacerobotocondosedlight(), Typeface.NORMAL);
		signupButton.setTextSize(26f);
		signupButton.setTextColor(Color.parseColor("#FCFEFC"));

	}

	private void setGypsyAnimation() {
		final LinearLayout signUpLayout = (LinearLayout) findViewById(R.id.linear_signup_login);
		signUpLayout.setVisibility(View.INVISIBLE);
		mottoTextView.setVisibility(View.INVISIBLE);
		final ImageView gypsyImage = (ImageView) findViewById(R.id.logo_image);
		Animation gypsyScaleAnim = AnimationUtils.loadAnimation(this,
				R.anim.scale_down);
		final Animation imageClockwise = AnimationUtils.loadAnimation(this, R.anim.clockwise);
		gypsyImage.startAnimation(gypsyScaleAnim);
		gypsyScaleAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				signUpLayout.setVisibility(View.VISIBLE);
				animateMottoText();
				gypsyImage.startAnimation(imageClockwise);

			}
		});

	}

	private void animateMottoText() {
		mottoTextView.setVisibility(View.VISIBLE);
		Animation mottoTextViewAnim = AnimationUtils.loadAnimation(this,
				R.anim.fade_in_left);
		mottoTextView.startAnimation(mottoTextViewAnim);
		String text = getString(R.string.text_motto_signup_login);
		TextViewAnimator animator = TextViewAnimator.newInstance(mottoTextView,
				text, 70);
		animator.start();
	}

	private void setMottoDesign() {

		mottoTextView.setTypeface(setTypeFacerobotocondosedlight(), Typeface.NORMAL);
		mottoTextView.setTextSize(20f);
		mottoTextView.setTextColor(Color.parseColor("#FCFEFC"));

	}

}
