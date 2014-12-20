package com.isaplings.travelfriend.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.result.PlacesResult;
import com.isaplings.travelfriend.AsyncTaskCompleteListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

// Refactoring of the code is complete for preExecute and postExecute
// Business logic need to be improved for collecting POIData
// Remove unwanted log message
// last modified Navine - 05 Dec 2014

public class POITextSearchTask extends AsyncTask<Location, String, PlacesResult> {

	Activity appActivity;
	AsyncTaskCompleteListener<PlacesResult> mlistener;
	
	List<String> mTypes;
	String mSearchText;

	Double longitude, latitude;
	
	ProgressDialog progressDialog;

	private static final String TAG = "Debug";

	public POITextSearchTask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener) {
		super();
		this.appActivity = activity;
		this.mlistener = listener;
		this.mTypes = new ArrayList<String>();
		this.mTypes.add("hospital");
		this.mTypes.add("dentist");
		this.mTypes.add("doctor");		
		
		this.mSearchText = null;

	}

	public POITextSearchTask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener, List<String> types, String keyword) {
		super();
		this.appActivity = activity;
		this.mlistener = listener;
		this.mTypes = types;
		this.mSearchText = keyword;

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
			
			
			
			placesResult = (PlacesResult) googlePlaces.getTextPlaces(mTypes, mSearchText, 10000, latitude, longitude);
					


			Log.v(TAG,
					"MYGPSLocation : Size of Result1 - places  " + placesResult.getResults().size());
			Log.v(TAG,
					"MYGPSLocation : result1 - places - [Status Code]  " + placesResult.getStatusCode());
			
							
			

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

//		
//		if(progressDialog != null && progressDialog.isShowing())
//        {
//            progressDialog.dismiss();
//        }

		Log.v(TAG,
				"MYGPSLocation : onPostExecute Method Completed - calling callback method of list ");

		
		mlistener.onTaskComplete(placesList);

	}

	protected void onPreExecute() {
		Log.v(TAG,"MY GPS : Invokde  Progress Dialog" );
		//progressDialog = ProgressDialog.show(appActivity,"Please Wait...","Retrieving information.");
		//progressDialog.setCancelable(false);
		

	}

}
