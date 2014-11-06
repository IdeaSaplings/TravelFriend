package com.isaplings.travelfriend;

import java.util.ArrayList;

import org.gmarz.googleplaces.models.Place;
import org.gmarz.googleplaces.models.PlacesResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

			// Show a dialog box here that no results are found
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("Sorry, No data");
			builder.setMessage(
					"No result in your search range. Try changing search range.")
					.setCancelable(false)
					.setPositiveButton("Settings",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// MyActivity.this.finish();
									// Settings to be invoked
									dialog.cancel();

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			return;

		}

		Log.v("Debug", "MyGPS : Before creating new Intent");

		// Use the Parcelable to bundle the ArrayList into the Intent
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("Place",
				(ArrayList<Place>) placesResult.getPlaces());

		// Create a new Intent

		Intent intent = new Intent(mContext, ListPlacesActivity.class);
		intent.putExtras(bundle);

		// androidruntimeexception : calling startactivity() from outside of an
		// activity
		// Hence we need to know the activity

		mActivity.startActivity(intent);
		Log.v("Debug", "MyGPS : Before creating new Intent");

	}
}