package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.List;

import org.gmarz.googleplaces.GooglePlaces;
import org.gmarz.googleplaces.models.Place;
import org.gmarz.googleplaces.models.PlacesResult;
import org.gmarz.googleplaces.models.Result.StatusCode;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

// This Code needs to be refactored for the UI Components

public class GetMyPOITask extends AsyncTask<Location, String, PlacesResult> {

	Context mContext;
	Activity appActivity;
	AsyncTaskCompleteListener<PlacesResult> mlistener;

	Double longitude, latitude;

	private static final String TAG = "Debug";

	public GetMyPOITask(Activity activity, Context context,
			AsyncTaskCompleteListener<PlacesResult> listener) {
		super();
		mContext = context;
		appActivity = activity;
		mlistener = listener;

	}

	@Override
	protected PlacesResult doInBackground(Location... params) {
		// TODO Auto-generated method stub
		Location loc = params[0];

		GooglePlaces googlePlaces = new GooglePlaces(
				"AIzaSyAPL4gar2x7nQKc9p-bRhDa4RCgSL1qTRA");
		Log.v("Test", "Navine : Before get hospitals");

		//Double latitude = 51.503186;
		//Double longitude = -0.12646;

		PlacesResult result = null;

		// Log.v(TAG, "GPS: Current Thread Before Priority is  set : " +
		// Thread.currentThread().getPriority());

		// Backgroud task is run with the highest priority
		// check if it is allowed

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// Log.v(TAG, "GPS: Current Thread After Priority is  set : " +
		// Thread.currentThread().getPriority());

		try {
			Log.v(TAG,
					"MYGPSLocation : GetPOIDetails Trying to get POI Details using Google Places Library  ");
			
			


			 latitude = loc.getLatitude();
			 longitude = loc.getLongitude();

			Log.v(TAG, "MYGPSLocation : Latitude " + latitude);
			Log.v(TAG, "MYGPSLocation : Longitude " + longitude);
			
			result = googlePlaces.getPlaces("hospital", 2500, latitude, longitude);

			Log.v(TAG,
					"MYGPSLocation : Size of result1 - places  " + result.getPlaces().size());
			Log.v(TAG,
					"MYGPSLocation : result1 - places - [Status Code]  " + result.getStatusCode());
			
			if ((result.getStatusCode() != StatusCode.OK) || (result.getPlaces().size()<5)) {
					// Then hop to the next radius
					Log.v(TAG, "MYGPSLocation : Trying to get POI details at level 2 radius");

					
					result = googlePlaces.getPlaces("hospital", 25000, latitude, longitude);
					Log.v(TAG,
							"MYGPSLocation : Size of result[2] - places  " + result.getPlaces().size());
					Log.v(TAG,
							"MYGPSLocation : result[2] - places - [Status Code]  " + result.getStatusCode());
					
					if ((result.getStatusCode() != StatusCode.OK) || (result.getPlaces().size()<5)) {
							// Then hop to the next radius
							Log.v(TAG, "MYGPSLocation : Trying to get POI details at level 3 radius");

							result = googlePlaces.getPlaces("hospital", 50000, latitude, longitude);
							Log.v(TAG,
									"MYGPSLocation : Size of result[2] - places  " + result.getPlaces().size());
							Log.v(TAG,
									"MYGPSLocation : result[2] - places - [Status Code]  " + result.getStatusCode());
					}
			}
				
			

		} catch (IOException e) {
			Log.v(TAG,
					"MYGPSLocation :  GetPlaces Throws IOException Not Available exception thrown ");
			e.printStackTrace();
			return result;
		} catch (JSONException e) {
			Log.v(TAG,
					"MYGPSLocation :  GetPlaces Throws LimitExceed Service Not Available exception thrown ");
			e.printStackTrace();
			return result;
		}catch (Exception e) {
			Log.v(TAG, "MYGPSLocation :  GetPlaces Throws  exception thrown ");
			e.printStackTrace();
			return result;

		}

		return result;
	}

	protected void onPostExecute(PlacesResult placesList) {

		final ProgressBar progressBar = (ProgressBar) appActivity.getWindow()
				.getDecorView().findViewById(R.id.progressBar1);
		final EditText editText = (EditText) appActivity.getWindow()
				.getDecorView().findViewById(R.id.editTextLocation);
		progressBar.setVisibility(View.INVISIBLE);

		Log.v(TAG, "MYGPSLocation : onPostExecute Method ");

		if ((placesList != null) && (placesList.getPlaces().size() > 0)) {
			Log.v(TAG, "MYGPSLocation : Print Address Method ");

			int listSize = placesList.getPlaces().size();

			
			String s = "The result list size is " + listSize + "\n The Status code is " + placesList.getStatusCode();
			
			
			List<Place> places = placesList.getPlaces();
			if (places.size() > 0) {
				Log.v("Test", "Navine Places Size: " + places.size());
				editText.setText(s);
			}

			final ListView listView = (ListView) appActivity.getWindow()
					.getDecorView().findViewById(R.id.address_list);
			PlaceAdapter adapter = new PlaceAdapter(mContext,
					R.layout.address_lists_item, places);
			listView.setAdapter(adapter);
		} else {

			String str = "unable to get address list in this range";
			editText.setText(str);
			mlistener.onTaskComplete(null);

		}
		Log.v(TAG,
				"MYGPSLocation : onPostExecute Method Completed - calling callback method of list ");

		mlistener.onTaskComplete(placesList);

	}

	protected void onPreExecute() {
		final EditText editText = (EditText) appActivity.getWindow()
				.getDecorView().findViewById(R.id.editTextLocation);

		final Button btnGetLocation = (Button) appActivity.getWindow()
				.getDecorView().findViewById(R.id.btnLocation);

		final ProgressBar progressBar = (ProgressBar) appActivity.getWindow()
				.getDecorView().findViewById(R.id.progressBar1);

		btnGetLocation.setEnabled(false);
		progressBar.setVisibility(View.VISIBLE);
		editText.setText("Trying to get the POI details \nPlease wait.. connecting to internet");

	}

}
