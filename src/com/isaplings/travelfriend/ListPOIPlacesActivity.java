package com.isaplings.travelfriend;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

public class ListPOIPlacesActivity {

	// private static final String TAG = "Debug";

	public static void getPOIList(Activity mActivity, Context mContext,
			Location mLocation, List<String> mTypes, String mKeyword) {

		if (mActivity.isFinishing()) {
			return;
		}

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				mActivity, mContext, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(mActivity, fm, mTypes, mKeyword);

		if (mLocation != null) {

			poiTask.execute(mLocation);
		}

		// Log.v("Debug", "MyGPS : New Intent Complete");

	}

	public static GetMyPOITask getPOIListTask(Activity mActivity, Context mContext,
			Location mLocation, List<String> mTypes, String mKeyword) {

		if (mActivity.isFinishing()) {
			return null;
		}

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				mActivity, mContext, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(mActivity, fm, mTypes, mKeyword);

		if (mLocation != null) {

			poiTask.execute(mLocation);
			//return poiTask;
		}

		return poiTask;
		// Log.v("Debug", "MyGPS : New Intent Complete");

	}


	
	public static void getPOIList(Activity mActivity, Context mContext,
			Location mLocation, List<String> mTypes, String mKeyword,
			String queryType) {
	
		if (mActivity.isFinishing()) {
			return;
		}

		
		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				mActivity, mContext, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(mActivity, fm, mTypes,
				mKeyword, queryType);

		if (mLocation != null) {

			poiTask.execute(mLocation);
		}

	}

}
