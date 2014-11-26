package com.isaplings.travelfriend;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class ListPOIPlacesActivity extends Activity {

	private static final String TAG = "Debug";
	private ActionBar actionBar;

	public void onCreate(Bundle savedInstanceState) {

		Log.v("Debug", "MyGPS : New Intent Start");

		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.list_places_details);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle bundle = this.getIntent().getExtras();
		// List<Place> placesList = bundle.getParcelableArrayList("Place");

		Location mLocation = bundle.getParcelable("LOCATION");
		
		
		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");
				
		
		
		Log.v("Debug", "MyGPS : Street Name : " + streetName);
		Log.v("Debug", "MyGPS : CityName : " + cityName);
		
		actionBar = getActionBar();
		
		actionBar.setTitle(streetName);
		actionBar.setSubtitle(cityName);

		actionBar.setIcon(R.drawable.hospital);
		
		
		// Set the Location at Action Bar

		// Bug fix put the Street Name / Location in the Action Bar

		Log.v("Debug", "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v("Debug", "MyGPS : Longitude : " + mLocation.getLongitude());

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				ListPOIPlacesActivity.this, this, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(ListPOIPlacesActivity.this, fm);

	
		if (mLocation != null) {
			// btnGetLocation.setEnabled(true);
			Log.v(TAG,
					"MyGPSLocation : GetPOIDetails Task Execute for Location :"
							+ mLocation.getLatitude() + ","
							+ mLocation.getLongitude());
			poiTask.execute(mLocation);
		}

		/*
		 * ListView listView = (ListView) findViewById(R.id.places_list);
		 * PlaceAdapter adapter = new PlaceAdapter(this,
		 * R.layout.places_lists_item, placesList);
		 * listView.setAdapter(adapter);
		 */

		Log.v("Debug", "MyGPS : New Intent Complete");

	}

}
