package com.isaplings.travelfriend;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ShareMyLocation extends Activity {

	private static final String TAG = "Debug";
	private ActionBar actionBar;

	Location mLocation;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onCreate(Bundle savedInstanceState) {

		Log.v("Debug", "MyGPS : New Intent Start");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.share_my_location);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// AdHolder update

//		AdView adView = (AdView) findViewById(R.id.ad_mob_view);
//		AdRequest adRequest = new AdRequest.Builder()
//				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//				.addTestDevice("TEST_DEVICE_ID")
//				.addKeyword("health")
//				.build();
//		adView.loadAd(adRequest);

		Bundle bundle = this.getIntent().getExtras();

		mLocation = bundle.getParcelable("LOCATION");
		

		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");
		String locationAddress = mLocation.getExtras().getString("LOCADDRESS");

		Log.v(TAG, "MyGPS : Street Name : " + streetName);
		Log.v(TAG, "MyGPS : CityName : " + cityName);

		// Code for setting action bar icon and title as custom view
		// Fixing bug to resolve, only icon click on action bar should take back
		// to Home

		String abTitle = streetName + "\n" + cityName;
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.ab_title);

		TextView title = (TextView) findViewById(android.R.id.text1);
		title.setText(abTitle);

		actionBar.setIcon(R.drawable.share_location);

		// Set the Location at Action Bar

		// Bug fix put the Street Name / Location in the Action Bar

		Log.v(TAG, "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v(TAG, "MyGPS : Longitude : " + mLocation.getLongitude());
		
		TextView address = (TextView) findViewById(R.id.textView2);
		address.setText(locationAddress);
		
		
	
		Log.v(TAG, "MyGPS : New Intent Complete");
		
	}
	
	public void sendMessage(View view) {
		Double latitude = mLocation.getLatitude();
		Double longitude = mLocation.getLongitude();
		
		String uri = "http://maps.google.com/maps?saddr=" + latitude +","+ longitude;
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String ShareSub = "Here is my location";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ShareSub);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
		startActivity(Intent.createChooser(sharingIntent, "Share via"));

	}
}

