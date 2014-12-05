package com.isaplings.travelfriend;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class ListFuelStationActivity extends  Activity {

	private static final String TAG = "Debug";
	private ActionBar actionBar;
	
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

		Log.v(TAG, "MyGPS : New Intent Start");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_places_details);
		
		android.app.ActionBar ab = getActionBar(); 
		ab.setDisplayHomeAsUpEnabled(true);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// AdHolder update

				AdView adView = (AdView) findViewById(R.id.ad_mob_view);
				AdRequest adRequest = new AdRequest.Builder()
						.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
						.addTestDevice("TEST_DEVICE_ID")
						.addKeyword("fuel")
						.build();
				adView.loadAd(adRequest);

		
		Bundle bundle = this.getIntent().getExtras();
		// List<Place> placesList = bundle.getParcelableArrayList("Place");

		Location mLocation = bundle.getParcelable("LOCATION");
		
		
		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");
				
		
		
		Log.v(TAG, "MyGPS : Street Name : " + streetName);
		Log.v(TAG, "MyGPS : CityName : " + cityName);
		
		// Code for setting action bar icon and title as custom view
		// Fixing bug to resolve, only icon click on action bar should take back to Home
				
		String abTitle = streetName + "\n" + cityName;
		actionBar = getActionBar(); 
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.ab_title);

		TextView title = (TextView) findViewById(android.R.id.text1);
		title.setText(abTitle);

		actionBar.setIcon(R.drawable.fuel_pump);
		
		
		// Set the Location at Action Bar

		// Bug fix put the Street Name / Location in the Action Bar

		Log.v(TAG, "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v(TAG, "MyGPS : Longitude : " + mLocation.getLongitude());
		
		List<String> types = new ArrayList<String>();
		types.add("gas_station");
		types.add("gas station");
		
			
		
		String keyword = null;

		ListPOIPlacesActivity.getPOIList(ListFuelStationActivity.this, this, mLocation, types, keyword);

		Log.v(TAG, "MyGPS : New Intent Complete");

	}

}

