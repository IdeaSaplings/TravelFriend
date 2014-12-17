package com.isaplings.travelfriend;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;

//code integrated from http://www.jameselsey.co.uk/blogs/techblog/how-to-add-a-splash-screen-to-your-android-application-in-under-5-minutes/

public class SplashActivity extends Activity {
	// Set the display time, in milliseconds (or extract it out as a
	// configurable parameter)
	private final int SPLASH_DISPLAY_LENGTH = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		// Obtain the sharedPreference, default to true if not available
		boolean isSplashEnabled = sp.getBoolean("isSplashEnabled", true);

		if (isSplashEnabled) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// Finish the splash activity so it can't be returned to.
					SplashActivity.this.finish();
					// Create an Intent that will start the main activity.
					Intent mainIntent = new Intent(SplashActivity.this,
							Travel.class);
					SplashActivity.this.startActivity(mainIntent);
					
//	                startActivity(new Intent().setClass(SplashActivity.this, Travel.class).setData(getIntent().getData()));

					
				}
			}, SPLASH_DISPLAY_LENGTH);
		} else {
			// if the splash is not enabled, then finish the activity
			// immediately and go to main.
			finish();
			Intent mainIntent = new Intent(SplashActivity.this, Travel.class);
			SplashActivity.this.startActivity(mainIntent);
		}
	}
}