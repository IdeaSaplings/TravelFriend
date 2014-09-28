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

public class Travel extends Activity implements OnClickListener {

	private Button btnGetLocation = null;
	private EditText editLocation = null;
	private ProgressBar pb = null;

	private ActionBar ab;

	private static final String TAG = "Debug";
	// private Boolean flag = false;

	private Double latitude;
	private Double longitude;
	private Location mLocation;

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
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ab = getActionBar();
		ab.setTitle("Current Location");

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
					Log.v(TAG, " Location - Latitude " + latitude);
					Log.v(TAG, " Location - Longitude " + longitude);
				}

			}
		};
		MyLocation myLocation = new MyLocation();
		Boolean flag = false;

		// check if this while loop is necessary

		// while (!flag)
		flag = myLocation.getLocation(this, locationResult);

		if ((latitude != null) && (longitude != null)) {

			Log.v(TAG, "MYGPSLocation : flag is true");
			Log.v(TAG,
					"MYGPSLocation : Calling Async Task to get the City Name");

			/*----------to get City-Name from coordinates ------------- */

			(new GetAddressTask(this)).execute(mLocation);

		} else
			Log.v(TAG, "MYGPSLocation : flag is false");

		Log.v(TAG, "MyGPSLocation : All steps executed");

	}

	private class GetAddressTask extends
			AsyncTask<Location, String, List<Address>> {
		Context mContext;

		public GetAddressTask(Context context) {
			super();
			mContext = context;

		}

		@Override
		protected List<Address> doInBackground(Location... params) {
			// TODO Auto-generated method stub
			Location loc = params[0];

			Geocoder gcd = new Geocoder(mContext);

			List<Address> addresses = null;

			try {
				Log.v(TAG,
						"MYGPSLocation : GetCityName Trying to get cityname using Geocoder ");

				addresses = gcd.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);

				Log.v(TAG,
						"MYGPSLocation : Trying to get size  "
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

		protected void onPostExecute(List<Address> addressList) {
			pb.setVisibility(View.INVISIBLE);

			Log.v(TAG, "MYGPSLocation : onPostExecute Method ");
			if ((addressList != null) && (addressList.size() > 0)) {
				Log.v(TAG, "MYGPSLocation : Print Address Method ");

				String cityName = addressList.get(0).getFeatureName();

				String s = "I'm here : " + cityName
						+ "\nhttp://maps.google.com/maps?f=q&geocode=&q="
						+ latitude + "," + longitude + "&z=16";
				editLocation.setText(s);
				//addressList.g
				ab.setTitle(addressList.get(0).getSubLocality()); 				
				ab.setSubtitle(addressList.get(0).getLocality()+"-"+addressList.get(0).getSubAdminArea());
				//ab.setSubtitle(addressList.get(0).getCountryName() + " " + addressList.get(0).getPostalCode());
				
				/*String[] cityNameTok = cityName.split(",");
				
				ab.setTitle(cityNameTok[0]);
				ab.setSubtitle(cityNameTok[cityNameTok.length-2]+","+cityNameTok[cityNameTok.length-1]);
				// ab.setTitle(cityName);
                */
				btnGetLocation.setEnabled(true);

				ListView listView = (ListView) findViewById(R.id.address_list);
				CityAddressAdapter adapter = new CityAddressAdapter(mContext,
						R.layout.address_lists_item, addressList);
				listView.setAdapter(adapter);
			} else {
				String s = "unable to find address using geocoder";
				editLocation.setText(s);
				btnGetLocation.setEnabled(true);

			}

			Log.v(TAG, "MYGPSLocation : onPostExecute Method Completed ");

		}

		protected void onPreExecute() {
			btnGetLocation.setEnabled(false);
			pb.setVisibility(View.VISIBLE);
			editLocation
					.setText("Try to get the City Name / Address \n Please wait.. connecting to internet");

		}

	}

}
