package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.List;

import com.isaplings.travelfriend.Geocoder.LimitExceededException;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;


// This Code needs to be refactored for the UI Components
// All unwated code needs to be removed
public class GetMyAddressTask extends
		AsyncTask<Location, String, List<Address>> {

	Context mContext;
	Activity appActivity;
	AsyncTaskCompleteListener<List<Address>> mlistener;

	Double longitude, latitude;

	private static final String TAG = "Debug";

	public GetMyAddressTask(Activity activity, Context context,
			AsyncTaskCompleteListener<List<Address>> listener) {
		super();
		mContext = context;
		appActivity = activity;
		mlistener = listener;

	}

	@Override
	protected List<Address> doInBackground(Location... params) {
		// TODO Auto-generated method stub
		Location loc = params[0];

		Geocoder gcd = new Geocoder(mContext);

		List<Address> addresses = null;
		
		//Log.v(TAG, "GPS: Current Thread Before Priority is  set : " + Thread.currentThread().getPriority());
		
		// Backgroud task is run with the highest priority
		// check if it is allowed
		
		
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		

		//Log.v(TAG, "GPS: Current Thread After Priority is  set : " + Thread.currentThread().getPriority());

		try {
			Log.v(TAG,
					"MYGPSLocation : GetCityName Trying to get cityname using Geocoder ");

			addresses = gcd.getFromLocation(loc.getLatitude(),
					loc.getLongitude(), 1);

			latitude = loc.getLatitude();
			longitude = loc.getLongitude();
			Log.v(TAG,
					"MYGPSLocation : Trying to get size  " + addresses.size());

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
		} catch (IllegalArgumentException e){
			Log.v(TAG,
					"MYGPSLocation :  GeoCoder Throws IllegalArgument exception thrown ");
			e.printStackTrace();
			return addresses;
			
		} catch (Exception e){
			Log.v(TAG,
					"MYGPSLocation :  GeoCoder Throws  exception thrown ");
			e.printStackTrace();
			return addresses;
			
		}

		return addresses;
	}

	protected void onPostExecute(List<Address> addressList) {

		/*final ProgressBar progressBar = (ProgressBar) appActivity.getWindow()
				.getDecorView().findViewById(R.id.progressBar1);
		final EditText editText = (EditText) appActivity.getWindow()
				.getDecorView().findViewById(R.id.editTextLocation);
		progressBar.setVisibility(View.INVISIBLE);
*/
		Log.v(TAG, "MYGPSLocation : onPostExecute Method ");
		
		
		if ((addressList != null) && (addressList.size() > 0)) {
			Log.v(TAG, "MYGPSLocation : Print Address Method ");

			String cityName = addressList.get(0).getFeatureName();

			Log.v(TAG, "MYGPSLocation : cityName " + cityName);

			
			/*String s = "I'm here : " + cityName
					+ "\nhttp://maps.google.com/maps?f=q&geocode=&q="
					+ latitude + "," + longitude + "&z=16";
			*/
			
			/*editText.setText(s);

			final ListView listView = (ListView) appActivity.getWindow()
					.getDecorView().findViewById(R.id.address_list);
			CityAddressAdapter adapter = new CityAddressAdapter(mContext,
					R.layout.address_lists_item, addressList);
			listView.setAdapter(adapter);
			*/
		} else {

			/*String s = "unable to find address using geocoder";
			editText.setText(s);*/
			mlistener.onTaskComplete(null);


		}
		Log.v(TAG,
				"MYGPSLocation : onPostExecute Method Completed - calling callback method of list ");

		mlistener.onTaskComplete(addressList);

	}

	protected void onPreExecute() {
		/*final EditText editText = (EditText) appActivity.getWindow()
				.getDecorView().findViewById(R.id.editTextLocation);

		final Button btnGetLocation = (Button) appActivity.getWindow()
				.getDecorView().findViewById(R.id.btnLocation);

		final ProgressBar progressBar = (ProgressBar) appActivity.getWindow()
				.getDecorView().findViewById(R.id.progressBar1);

		btnGetLocation.setEnabled(false);
		progressBar.setVisibility(View.VISIBLE);
		
		editText.setText("Trying to get the City Name / Address \nPlease wait.. connecting to internet");
*/
		
		Log.v(TAG, "MYGPSLocation : Empty PreExecute Method ");
		
		//Placeholder for refresh button integration

	}

}
