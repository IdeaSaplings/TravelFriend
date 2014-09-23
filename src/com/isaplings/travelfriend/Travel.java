package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


import com.isaplings.travelfriend.MyLocation.LocationResult;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

	private static final String TAG = "Debug";
	// private Boolean flag = false;

	private Double latitude;
	private Double longitude;
	private Location mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		pb = (ProgressBar) findViewById(R.id.progressBar1);
		pb.setVisibility(View.INVISIBLE);

		editLocation = (EditText) findViewById(R.id.editTextLocation);

		btnGetLocation = (Button) findViewById(R.id.btnLocation);
		btnGetLocation.setOnClickListener(this);

	}

	public void onClick(View v) {

		Log.v(TAG, "MyGPSLocation : onClick");

		editLocation.setText("Please!! move your device to"
				+ " see the changes in coordinates." + "\n Try again..");

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

		while (!flag)
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

		@SuppressWarnings("static-access")
		@Override
		protected List<Address> doInBackground(Location... params) {
			// TODO Auto-generated method stub
			Location loc = params[0];

			String cityName = null;
			Geocoder gcd = new Geocoder(mContext, Locale.getDefault());

			Log.v(TAG, "MYGPSLocation is present is " + gcd.isPresent());

			if (!gcd.isPresent())
				return null;

			List<Address> addresses = null;

			try {
				Log.v(TAG,
						"MYGPSLocation : GetCityName Trying to get cityname using Geocoder ");

				// Check if you still need a while loop here - This need to be
				// removed

				while (addresses == null) {
					addresses = gcd.getFromLocation(loc.getLatitude(),
							loc.getLongitude(), 1);
					Log.v(TAG, "MyGPSLocation : loop");
				}
				// addresses = gcd.getFromLocation(80.2699,13.0838, 1);
				Log.v(TAG,
						"MYGPSLocation : Trying to get size  "
								+ addresses.size());

				if ((addresses != null) && (addresses.size() > 0)) {
					Log.v(TAG, "MYGPSLocation : Address is not null");
					cityName = addresses.get(0).getAddressLine(0);
					Log.v(TAG, "MYGPSLocation : Address is " + cityName);

				}

			} catch (IOException e) {
				Log.v(TAG,
						"MYGPSLocation :  GeoCoder Throws Service Not Available exception thrown ");
				e.printStackTrace();
				return addresses;
			}

			return addresses;
		}

		protected void onPostExecute(List<Address> addressList) {
			pb.setVisibility(View.INVISIBLE);

			String cityName = addressList.get(0).getAddressLine(0);
			// String city = addressList.get(0).getLocality();
			String s = "I'm here : " + cityName
					+ "\nhttp://maps.google.com/maps?f=q&geocode=&q="
					+ latitude + "," + longitude + "&z=16";
			editLocation.setText(s);

			btnGetLocation.setEnabled(true);

			ListView listView = (ListView) findViewById(R.id.address_list);
			CityAddressAdapter adapter = new CityAddressAdapter(mContext,
					R.layout.address_lists_item, addressList);
			listView.setAdapter(adapter);

		}

		protected void onPreExecute() {
			btnGetLocation.setEnabled(false);
			pb.setVisibility(View.VISIBLE);
			editLocation
					.setText("Try to get the City Name / Address \n Please wait.. connecting to internet");

		}

	}

}
