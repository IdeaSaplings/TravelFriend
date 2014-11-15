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

		// Testing to show intermediate progress - As of now it is not working
		// you may have to remove this non-working code
		/*setTitleColor(Color.BLUE);

		if (requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS)) {

			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);
			// setProgressBarVisibility(true);

			Log.v("Debug", "MyGPS : Progress bar set to true");

		} else {
			Log.v("Debug", "MyGPS : requestWindowFeature returned false");

		}

*/
		setContentView(R.layout.list_places_details);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle bundle = this.getIntent().getExtras();
		// List<Place> placesList = bundle.getParcelableArrayList("Place");

		Location mLocation = bundle.getParcelable("LOCATION");
		
		
		/*
		 * Need not use StringExtra
		 * 
		 * String streetName = this.getIntent().getStringExtra("STREETNAME");
		* String cityName = this.getIntent().getStringExtra("CITYNAME");
		*
		*/
		
		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");
				
		//Log.v("Debug", "MyGPS : Travel-onBundle: Street Name : " + mLocation.getExtras().getString("STREETNAME"));
		//Log.v("Debug", "MyGPS : Travel-onBundle: CityName : " + mLocation.getExtras().getString("CITYNAME"));

		
		Log.v("Debug", "MyGPS : Street Name : " + streetName);
		Log.v("Debug", "MyGPS : CityName : " + cityName);
		
		actionBar = getActionBar();
		
		actionBar.setTitle(streetName);
		actionBar.setSubtitle(cityName);


		// Set the Location at Action Bar

		// Bug fix put the Street Name / Location in the Action Bar

		Log.v("Debug", "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v("Debug", "MyGPS : Longitude : " + mLocation.getLongitude());

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				ListPOIPlacesActivity.this, this);
		GetMyPOITask poiTask = new GetMyPOITask(ListPOIPlacesActivity.this, fm);

	/*	setProgressBarIndeterminateVisibility(false);
		// setProgressBarVisibility(false);
		setProgressBarIndeterminate(false);
*/
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
