package com.isaplings.travelfriend;


import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

public class ListPOIPlacesActivity  {

	private static final String TAG = "Debug";

	
	

	public static void getPOIList(Activity mActivity, Context mContext, Location mLocation, List<String> mTypes, String mKeyword) {

		
		Log.v("Debug", "MyGPS : Latitude : " + mLocation.getLatitude());
		Log.v("Debug", "MyGPS : Longitude : " + mLocation.getLongitude());

		FetchPoiDataTaskCompleteListener fm = new FetchPoiDataTaskCompleteListener(
				mActivity, mContext, mLocation);
		GetMyPOITask poiTask = new GetMyPOITask(mActivity, fm, mTypes, mKeyword);

	
		if (mLocation != null) {
			// btnGetLocation.setEnabled(true);
			Log.v(TAG,
					"MyGPSLocation : GetPOIDetails Task Execute for Location :"
							+ mLocation.getLatitude() + ","
							+ mLocation.getLongitude());
			poiTask.execute(mLocation);
		}

		

		Log.v("Debug", "MyGPS : New Intent Complete");

	}

}
