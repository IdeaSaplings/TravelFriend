package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.List;

import com.isaplings.travelfriend.Geocoder.LimitExceededException;
import com.isaplings.travelfriend.MyLocation.LocationResult;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Address;
//import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

// Since this Base Class for TravelFriend
// there will be many changes to this class
// Please ensure there are no regressions to this file


// Please go through the comments to see 
// the pending changes


// Major part of the code need to be refactores
// especially the inner class and method definitions


// Last Modified by Navine on 30/Sep/2014

public class Travel extends Activity implements OnClickListener {

	private Button btnGetLocation = null;
	private EditText editLocation = null;
	private ProgressBar pb = null;

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

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		editLocation = (EditText) findViewById(R.id.editTextLocation);

		btnGetLocation = (Button) findViewById(R.id.btnLocation);
		btnGetLocation.setOnClickListener(this);

	}

	public void onClick(View v) {
		// Closes the Activity
		finish();

	}

	public void onRefresh() {

		Log.v(TAG, "MyGPSLocation : onClick");

		editLocation.setText("Please!! move your device to"
				+ " see the changes in coordinates." + "\n Refresh again..");

		pb.setVisibility(View.VISIBLE);

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
				}

				else {
					mLocation = location;
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					Log.v(TAG, "GPS Location - Latitude " + latitude);
					Log.v(TAG, "GPS Location - Longitude " + longitude);

					if ((latitude != null) && (longitude != null)) {

						Log.v(TAG, "MYGPSLocation : flag is true");
						Log.v(TAG,
								"MYGPSLocation : Calling Async Task to get the City Name");

						/*----------to get City-Name from coordinates ------------- */
						getAddressFromLocation(appContext);
						//

					} else
						Log.v(TAG, "MYGPSLocation : flag is false");

				}

			}

			@Override
			protected void getAddressFromLocation(Context context) {
				Log.v(TAG, "MYGPSLocation : getAddressFromLocation called ");
				// (new GetAddressTask(appContext)).execute(mLocation);
				// This class will get the address from location

				class GetAddressTask extends
						AsyncTask<Location, String, List<Address>> {
					Context mContext;

					GetAddressTask(Context context) {
						super();
						mContext = context;

					}

					@Override
					protected List<Address> doInBackground(Location... params) {
						Location loc = params[0];

						Geocoder gcd = new Geocoder(mContext);

						List<Address> addresses = null;

						try {
							Log.v(TAG,
									"MYGPSLocation : GetCityName Trying to get cityname using Geocoder ");

							// We are extracting only one address for processing
							
							addresses = gcd.getFromLocation(loc.getLatitude(),
									loc.getLongitude(), 1);

							Log.v(TAG, "MYGPSLocation : Trying to get size  "
									+ addresses.size());

						} catch (IOException e) {
							Log.v(TAG,
									"MYGPSLocation :  GeoCoder Throws IOException Not Available exception thrown ");
							e.printStackTrace();
							return addresses;
						} catch (LimitExceededException e) {
							Log.v(TAG,
									"MYGPSLocation :  GeoCoder Throws LimitExceed Service Not Available exception thrown ");
							e.printStackTrace();
							return addresses;
						}

						return addresses;

					}

					@Override
					protected void onPostExecute(List<Address> addressList) {
						pb.setVisibility(View.INVISIBLE);

						Log.v(TAG, "MYGPSLocation : onPostExecute Method ");
						if ((addressList != null) && (addressList.size() > 0)) {
							Log.v(TAG, "MYGPSLocation : Print Address Method ");

							String cityName = addressList.get(0)
									.getFeatureName();

							String s = "I'm here : "
									+ cityName
									+ "\nhttp://maps.google.com/maps?f=q&geocode=&q="
									+ latitude + "," + longitude + "&z=16";
							editLocation.setText(s);
							
							
							// Extracted from Address Component in JSONObject
							// SubLocality - route || administrative_area_level_2 || administrative_area_level_1
							// Locality - locality || political
							// SubAdminArea - administrative_area_level_2 || country
							
							
							actionBar.setTitle(addressList.get(0).getSubLocality());
							actionBar.setSubtitle(addressList.get(0).getLocality()
									+ "-"
									+ addressList.get(0).getSubAdminArea());

							
							btnGetLocation.setEnabled(true);

							ListView listView = (ListView) findViewById(R.id.address_list);
							CityAddressAdapter adapter = new CityAddressAdapter(
									mContext, R.layout.address_lists_item,
									addressList);
							listView.setAdapter(adapter);
						} else {
							String s = "unable to find address using geocoder";
							editLocation.setText(s);
							btnGetLocation.setEnabled(true);

						}

						Log.v(TAG,
								"MYGPSLocation : onPostExecute Method Completed ");

					}

					@Override
					protected void onPreExecute() {

						Log.v(TAG, "MYGPSLocation : onPreExecute Method Start ");

						btnGetLocation.setEnabled(false);
						pb.setVisibility(View.VISIBLE);
						editLocation
								.setText("Try to get the City Name / Address \n Please wait.. connecting to internet");
						Log.v(TAG,
								"MYGPSLocation : onPreExecute Method Completed ");

					}

				}

				// Calling the AddressTask - by passing the parameter from
				// getAddressFromLocation

				GetAddressTask addTask = new GetAddressTask(context);
				if (mLocation != null) {
					Log.v(TAG,
							"MyGPSLocation : Get Address Execute for Location :"
									+ mLocation.getLatitude() + ","
									+ mLocation.getLongitude());
					addTask.execute(mLocation);
				}

			}

		}; // LocationResult Definition Ends

		MyLocation myLocation = new MyLocation(Travel.this, this);
		Boolean flag = false;

		flag = myLocation.getLocation(locationResult);

		Log.v(TAG, "MyGPSLocation : flag1 return value " + flag);

		Log.v(TAG,
				"MyGPSLocation : All steps executed - wait for GPS/Network Update Action");

	}

}
