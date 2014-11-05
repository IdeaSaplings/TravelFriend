package com.isaplings.travelfriend;

import java.util.ArrayList;

import org.gmarz.googleplaces.models.Place;
import org.gmarz.googleplaces.models.PlacesResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

class FetchPoiDataTaskCompleteListener extends Activity implements
		AsyncTaskCompleteListener<PlacesResult> {

	Activity mActivity;
	Context mContext;

	public FetchPoiDataTaskCompleteListener(Activity mActivity, Context mContext) {
		super();
		this.mActivity = mActivity;
		this.mContext = mContext;

	}

	@Override
	public void onTaskComplete(PlacesResult placesResult) {

		// We can also execute the postExecute Method Here

		if ((placesResult == null) || (placesResult.getPlaces().size() <= 0)) {
			
			return;
			
		}

		
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("Place",
				(ArrayList<Place>) placesResult.getPlaces());

		// Create a new Intent

		Intent intent = new Intent(mContext, ListPlacesActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
		
		intent.putExtras(bundle);

		Log.v("GPS", "MY GPS Working till here" );
		
		//androidruntimeexception calling startactivity() from outside of an activity
		// Hence we need to know the activity
		
		//Activity activity = (Activity) mContext.getApplicationContext();
		
		
		mActivity.startActivity(intent);

		Log.v("GPS", "MY GPS Working till here - Activity Started" );

		
		// btnGetLocation.setEnabled(true);
	}
}