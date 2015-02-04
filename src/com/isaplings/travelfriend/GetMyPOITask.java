package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;

// Refactoring of the code is complete for preExecute and postExecute
// Business logic need to be improved for collecting POIData
// Remove unwanted log message
// last modified Navine - 06 Nov 2014

public class GetMyPOITask extends AsyncTask<Location, String, PlacesResult> {

	Activity appActivity;
	AsyncTaskCompleteListener<PlacesResult> mlistener;

	List<String> mTypes;
	String mKeyword;

	String qType;

	Double longitude, latitude;

	ProgressDialog progressDialog;

	// private static final String TAG = "Debug";

	public GetMyPOITask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener) {
		super();
		this.appActivity = activity;
		this.mlistener = listener;
		this.mTypes = new ArrayList<String>();
		this.mTypes.add("hospital");
		this.mTypes.add("dentist");
		this.mTypes.add("doctor");

		this.mKeyword = null;

		this.qType = "nearbysearch";

	}

	public GetMyPOITask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener,
			List<String> types, String keyword) {
		super();
		this.appActivity = activity;
		this.mlistener = listener;
		this.mTypes = types;
		this.mKeyword = keyword;

		this.qType = "nearbysearch";

	}

	public GetMyPOITask(Activity activity,
			AsyncTaskCompleteListener<PlacesResult> listener,
			List<String> types, String keyword, String queryType) {
		super();
		this.appActivity = activity;
		this.mlistener = listener;
		this.mTypes = types;
		this.mKeyword = keyword;

		this.qType = queryType;

	}

	@Override
	protected PlacesResult doInBackground(Location... params) {
		Location loc = params[0];

		GooglePlaces googlePlaces = new GooglePlaces(Travel.appContext
				.getResources().getString(R.string.api_key));

		PlacesResult placesResult = null;

		// Log.v(TAG, "GPS: Current Thread Before Priority is  set : " +
		// Thread.currentThread().getPriority());

		// Backgroud task is run with the highest priority
		// check if it is allowed

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

		// Log.v(TAG, "GPS: Current Thread After Priority is  set : " +
		// Thread.currentThread().getPriority());

		try {
			// Log.v(TAG,
			// "MYGPSLocation : GetPOIDetails using Google Places Library  : type "
			// + qType);

			latitude = loc.getLatitude();
			longitude = loc.getLongitude();

			SharedPreferences pref;

			pref = Travel.appContext
					.getSharedPreferences("TravelFriendPref", 0);

			int maxRadius = pref.getInt("MaxRadius", 50) * 1000;

			// Log.v(TAG, "MYGPSLocation : Latitude " + latitude);
			// Log.v(TAG, "MYGPSLocation : Longitude " + longitude);

			if (qType.equals("nearbysearch")) {
				// Log.v(TAG, "MYGPSLocation : Executing Nearby Search  ");

				placesResult = (PlacesResult) googlePlaces.getNearbyPlaces(
						mTypes, mKeyword, 2500, latitude, longitude);
				// Log.v(TAG, "MYGPSLocation : Size of Result1 - places  "
				// + placesResult.getResults().size());
				// Log.v(TAG,
				// "MYGPSLocation : result1 - places - [Status Code]  "
				// + placesResult.getStatusCode());

			} else if (qType.equals("textsearch")) {
				// Log.v(TAG, "MYGPSLocation : Executing Text Search  ");
				placesResult = (PlacesResult) googlePlaces.getTextPlaces(
						mTypes, mKeyword, 2500, latitude, longitude);

				// Log.v(TAG, "MYGPSLocation : Size of Result1 - places  "
				// + placesResult.getResults().size());
				// Log.v(TAG,
				// "MYGPSLocation : result1 - places - [Status Code]  "
				// + placesResult.getStatusCode());

			}

			if ((placesResult.getStatusCode() != StatusCode.OK)
					|| (placesResult.getResults().size() < 10)) {
				// Then hop to the next radius
				// Log.v(TAG,
				// "MYGPSLocation : Trying to get POI details at level 2 radius");

				if (qType.equals("nearbysearch")) {

					placesResult = (PlacesResult) googlePlaces.getNearbyPlaces(
							mTypes, mKeyword, 25000, latitude, longitude);
					// Log.v(TAG, "MYGPSLocation : Size of result[2] - places  "
					// + placesResult.getResults().size());
					// Log.v(TAG,
					// "MYGPSLocation : result[2] - places - [Status Code]  "
					// + placesResult.getStatusCode());

				} else if (qType.equals("textsearch")) {
					// Log.v(TAG, "MYGPSLocation : Executing Text Search  ");
					placesResult = (PlacesResult) googlePlaces.getTextPlaces(
							mTypes, mKeyword, 25000, latitude, longitude);
					// Log.v(TAG, "MYGPSLocation : Size of result[2] - places  "
					// + placesResult.getResults().size());
					// Log.v(TAG,
					// "MYGPSLocation : result[2] - places - [Status Code]  "
					// + placesResult.getStatusCode());

				}

				if ((placesResult.getStatusCode() != StatusCode.OK)
						|| (placesResult.getResults().size() < 10)) {
					// Then hop to the next radius
					// Log.v(TAG,
					// "MYGPSLocation : Trying to get POI details at level 3 radius");

					// placesResult =(PlacesResult)
					// googlePlaces.getNearbyPlaces(mTypes, mKeyword, "distance"
					// , latitude, longitude);

					if (qType.equals("nearbysearch")) {

						placesResult = (PlacesResult) googlePlaces
								.getNearbyPlaces(mTypes, mKeyword, maxRadius,
										latitude, longitude);
						// Log.v(TAG,
						// "MYGPSLocation : Size of result[3] - places  "
						// + placesResult.getResults().size());
						// Log.v(TAG,
						// "MYGPSLocation : result[3] - places - [Status Code]  "
						// + placesResult.getStatusCode());

					} else if (qType.equals("textsearch")) {
						// Log.v(TAG,
						// "MYGPSLocation : Executing Text Search  ");
						placesResult = (PlacesResult) googlePlaces
								.getTextPlaces(mTypes, mKeyword, maxRadius,
										latitude, longitude);
						// Log.v(TAG,
						// "MYGPSLocation : Size of result[3] - places  "
						// + placesResult.getResults().size());
						// Log.v(TAG,
						// "MYGPSLocation : result[3] - places - [Status Code]  "
						// + placesResult.getStatusCode());
					}

				}
			}

		} catch (IOException e) {
			// Log.v(TAG,
			// "MYGPSLocation :  GetPlaces Throws IOException Not Available exception thrown ");
			e.printStackTrace();
			return null;
			/*
			 * } catch (JSONException e) { Log.v(TAG,
			 * "MYGPSLocation :  GetPlaces Throws LimitExceed Service Not Available exception thrown "
			 * ); e.printStackTrace(); return placesResult;
			 */
		} catch (Exception e) {
			// Log.v(TAG,
			// "MYGPSLocation :  GetPlaces Throws  exception thrown ");
			e.printStackTrace();
			return null;

		}

		return placesResult;
	}

	protected void onPostExecute(PlacesResult placesList) {

		// Log.v(TAG, "MYGPSLocation : onPostExecute Method ");

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		// Log.v(TAG,
		// "MYGPSLocation : onPostExecute Method Completed - calling callback method of list ");

		mlistener.onTaskComplete(placesList);

	}

	protected void onPreExecute() {
		// Log.v(TAG, "MY GPS : Invoking Progress Dialog");
		// Fix for Trav
		if (progressDialog == null) {
			// Log.v(TAG, "MY GPS : Initialising Progress Dialog");
			// Fix for Trav50
			progressDialog = new ProgressDialog(appActivity.getApplicationContext());
		}
		progressDialog = ProgressDialog.show(appActivity, "Please Wait...",
				"Retrieving information.");
		progressDialog.setCancelable(false);

	}

}
