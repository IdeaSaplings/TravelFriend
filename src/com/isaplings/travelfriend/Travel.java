package com.isaplings.travelfriend;

import java.util.List;

import com.isaplings.travelfriend.MyLocation.LocationResult;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
//import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

// Since this Base Class for TravelFriend
// there will be many changes to this class
// Please ensure there are no regressions to this file

// Please go through the comments to see 
// the pending changes

// Major part of the code need to be refactores
// especially the inner class and method definitions

// Last Modified by Navine on 15/Nov/2014

// Modification in progress for getHospital data 15/Nov/2014

public class Travel extends Activity implements OnClickListener {

	/*
	 * private Button btnGetLocation = null; private EditText editLocation =
	 * null; private ProgressBar pb = null;
	 */
	private ActionBar actionBar;

	private static final String TAG = "Debug";
	// private Boolean flag = false;

	private Double latitude;
	private Double longitude;
	private Location mLocation;

	public static Context appContext;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_refresh:
			onRefresh();
			return true;
		case R.id.action_settings:
			// openSettings();
			// Need to check if settings option is required
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);

		appContext = this.getApplicationContext();

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// This need to changed

		actionBar = getActionBar();
		actionBar.setTitle("Current Location");

		/*
		 * pb = (ProgressBar) findViewById(R.id.progressBar1);
		 * pb.setVisibility(View.INVISIBLE);
		 * 
		 * editLocation = (EditText) findViewById(R.id.editTextLocation);
		 * 
		 * btnGetLocation = (Button) findViewById(R.id.btnLocation);
		 * btnGetLocation.setOnClickListener(this);
		 */
		// onRefresh();

	}

	public void onClick(View v) {
		// Closes the Activity
		finish();

	}

	public void onHospital(View v) {

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				Travel.this, appContext, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(Travel.this, fm);

		if (mLocation != null) {
			// btnGetLocation.setEnabled(true);
			Log.v(TAG,
					"MyGPSLocation : GetPOIDetails Task Execute for Location :"
							+ mLocation.getLatitude() + ","
							+ mLocation.getLongitude());
			poiTask.execute(mLocation);
		}

	}

	public void onGetHospital(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListPOIPlacesActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onRefresh() {

		Log.v(TAG, "MyGPSLocation : onClick");

		/*
		 * editLocation.setText("Please!! move your device to" +
		 * " see the changes in coordinates." + "\n Refresh again..");
		 * 
		 * pb.setVisibility(View.VISIBLE);
		 */
		// The following part of code needs refactoring
		// LocationResult needs to be re-factored.
		// GetAddressTask (Async) need to be defined separately
		// Please refer : Extracting Async Task as separate task
		// http://www.jameselsey.co.uk/blogs/techblog/extracting-out-your-asynctasks-into-separate-classes-makes-your-code-cleaner/

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				// Got the location!
				if (location == null) {
					Log.v(TAG, "MyGPSLocation Inside GotLocation: First Step ");

					// implement - show message - #CodeReview
				}

				else {
					mLocation = location;
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					Log.v(TAG, "GPS Location - Latitude " + latitude);
					Log.v(TAG, "GPS Location - Longitude " + longitude);

					if ((latitude != null) && (longitude != null)) {

						Log.v(TAG,
								"MYGPSLocation : Calling Async Task to get the City Name");

						/*----------to get City-Name from coordinates ------------- */
						getAddressFromLocation(appContext);
						//

					} else {
						Log.v(TAG, "MYGPSLocation : flag is false");
					}

				}

			}

			@Override
			protected void getAddressFromLocation(Context context) {
				// This class will get the address from location

				Log.v(TAG, "MYGPSLocation : getAddressFromLocation called ");

				class FetchMyDataTaskCompleteListener implements
						AsyncTaskCompleteListener<List<Address>> {

					@Override
					public void onTaskComplete(List<Address> result) {

						// We can also execute the postExecute Method Here

						Log.v(TAG,
								"MYGPSLocation : Inside onTaskComplete called ");

						// Extracted from Address Component in JSONObject
						// SubLocality - route || administrative_area_level_2 ||
						//   administrative_area_level_1
						// Locality - locality || political
						// SubAdminArea - administrative_area_level_2 || country

						if (result == null) {
							// btnGetLocation.setEnabled(true);
							actionBar.setTitle("Unknown Location");
							actionBar.setSubtitle("check your settings");
							// #codereview - Show message - unable to retreive
							// info - show alert box
							return;
						}

						if ((result != null) & (result.size() > 0)) {

							String streetName = result.get(0).getSubLocality();

							String cityName = result.get(0).getLocality()
									+ ", " + result.get(0).getSubAdminArea();

							actionBar.setTitle(streetName);
							actionBar.setSubtitle(cityName);

							Bundle extras = new Bundle();
							extras.putString("STREETNAME", streetName);
							extras.putString("CITYNAME", cityName);

							mLocation.setExtras(extras);

							// Log.v("Debug",
							// "MyGPS : Travel-onSet: Street Name : " +
							// mLocation.getExtras().getString("STREETNAME"));
							// Log.v("Debug",
							// "MyGPS : Travel-onSet: CityName : " +
							// mLocation.getExtras().getString("CITYNAME"));

						}
					}
				}

				FetchMyDataTaskCompleteListener fm = new FetchMyDataTaskCompleteListener();
				GetMyAddressTask addTask = new GetMyAddressTask(Travel.this,
						appContext, fm);

				if (mLocation != null) {
					// btnGetLocation.setEnabled(true);
					Log.v(TAG,
							"MyGPSLocation : GetMyAddress Task Execute for Location :"
									+ mLocation.getLatitude() + ","
									+ mLocation.getLongitude());
					addTask.execute(mLocation);
				}
			}
		}; // LocationResult Definition Ends

		MyLocation myLocation = new MyLocation(Travel.this, this);
		myLocation.getLocation(locationResult);
		Log.v(TAG,
				"MyGPSLocation : All steps executed - wait for GPS/Network Update Action");
	}
}
