package com.isaplings.travelfriend;


import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class ListRestRoomActivity extends  Activity {

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

		Bundle bundle = this.getIntent().getExtras();
		// List<Place> placesList = bundle.getParcelableArrayList("Place");

		Location mLocation = bundle.getParcelable("LOCATION");
		
		
		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");
				
		
		
		Log.v(TAG, "MyGPS : Street Name : " + streetName);
		Log.v(TAG, "MyGPS : CityName : " + cityName);
		
		actionBar = getActionBar();
		
		actionBar.setTitle(streetName);
		actionBar.setSubtitle(cityName);

		actionBar.setIcon(R.drawable.rest_room);
		
		
		// Set the Location at Action Bar

		// Bug fix put the Street Name / Location in the Action Bar

		Log.v(TAG, "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v(TAG, "MyGPS : Longitude : " + mLocation.getLongitude());
		
		List<String> types = new ArrayList<String>();
		types.add("rest_room");
		types.add("toilet");
		types.add("rest room");
			
		
		String keyword = null;

		ListPOIPlacesActivity.getPOIList(ListRestRoomActivity.this, this, mLocation, types, keyword);

		Log.v(TAG, "MyGPS : New Intent Complete");

	}

}

