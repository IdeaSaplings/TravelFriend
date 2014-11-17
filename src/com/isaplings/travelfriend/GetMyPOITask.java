package com.isaplings.travelfriend;

import java.io.IOException;



import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;





import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

// Refactoring of the code is complete for preExecute and postExecute
// Business logic need to be improved for collecting POIData
// Remove unwanted log message
// last modified Navine - 06 Nov 2014

public class GetMyPOITask extends AsyncTask<Location, String, PlacesResult> {

	Activity appActivity;
	AsyncTaskCompleteListener<PlacesResult> mlistener;

	Double longitude, latitude;
	
	ProgressDialog progressDialog;

	private static final String TAG = "Debug";

	public GetMyPOITask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener) {
		super();
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


		PlacesResult placesResult = null;

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
			
			placesResult = (PlacesResult) googlePlaces.getNearbyPlaces("hospital", "health", 2500, latitude, longitude);

			Log.v(TAG,
					"MYGPSLocation : Size of result1 - places  " + placesResult.getResults().size());
			Log.v(TAG,
					"MYGPSLocation : result1 - places - [Status Code]  " + placesResult.getStatusCode());
			
			if ((placesResult.getStatusCode() != StatusCode.OK) || (placesResult.getResults().size()<5)) {
					// Then hop to the next radius
					Log.v(TAG, "MYGPSLocation : Trying to get POI details at level 2 radius");

					
					placesResult = (PlacesResult) googlePlaces.getNearbyPlaces("hospital","", 25000, latitude, longitude);
					Log.v(TAG,
							"MYGPSLocation : Size of result[2] - places  " + placesResult.getResults().size());
					Log.v(TAG,
							"MYGPSLocation : result[2] - places - [Status Code]  " + placesResult.getStatusCode());
					
					if ((placesResult.getStatusCode() != StatusCode.OK) || (placesResult.getResults().size()<5)) {
							// Then hop to the next radius
							Log.v(TAG, "MYGPSLocation : Trying to get POI details at level 3 radius");

							placesResult =(PlacesResult) googlePlaces.getNearbyPlaces("hospital", "health", 50000, latitude, longitude);
							Log.v(TAG,
									"MYGPSLocation : Size of result[3] - places  " + placesResult.getResults().size());
							Log.v(TAG,
									"MYGPSLocation : result[3] - places - [Status Code]  " + placesResult.getStatusCode());
					}
			}
				
			

		} catch (IOException e) {
			Log.v(TAG,
					"MYGPSLocation :  GetPlaces Throws IOException Not Available exception thrown ");
			e.printStackTrace();
			return placesResult;
		/*} catch (JSONException e) {
			Log.v(TAG,
					"MYGPSLocation :  GetPlaces Throws LimitExceed Service Not Available exception thrown ");
			e.printStackTrace();
			return placesResult;*/
		}catch (Exception e) {
			Log.v(TAG, "MYGPSLocation :  GetPlaces Throws  exception thrown ");
			e.printStackTrace();
			return placesResult;

		}

		return placesResult;
	}

	protected void onPostExecute(PlacesResult placesList) {
		
		Log.v(TAG, "MYGPSLocation : onPostExecute Method ");

		
		if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

		Log.v(TAG,
				"MYGPSLocation : onPostExecute Method Completed - calling callback method of list ");

		
		mlistener.onTaskComplete(placesList);

	}

	protected void onPreExecute() {
		Log.v(TAG,"MY GPS : Invokde  Progress Dialog" );
		progressDialog = ProgressDialog.show(appActivity,"Please Wait...","Retrieving information.");
		progressDialog.setCancelable(false);
		

	}

}
