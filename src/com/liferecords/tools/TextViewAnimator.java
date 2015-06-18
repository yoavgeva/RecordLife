package com.liferecords.tools;

import android.os.Handler;
import android.widget.TextView;



public class TextViewAnimator {

	private TextView textView;
	private CharSequence text;
	private long delay;
	private int index;
	private Handler timerHandler = new Handler();
	private Runnable animationTask = new Runnable() {
		@Override
		public void run() {
			textView.setText(text.subSequence(0, index++));
			if (index <= text.length()) {
				timerHandler.postDelayed(animationTask, delay);
			}
		}
	};

	public static TextViewAnimator newInstance(TextView textView,
			CharSequence text, long delay) {
		TextViewAnimator instance = new TextViewAnimator();
		instance.textView = textView;
		instance.text = text;
		instance.delay = delay;
		return instance;
	}

	public void start() {
		textView.setText("");
		timerHandler.postDelayed(animationTask, delay);
	}

}
