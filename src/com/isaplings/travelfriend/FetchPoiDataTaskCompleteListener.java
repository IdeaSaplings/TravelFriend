package com.isaplings.travelfriend;


import java.util.List;

import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.result.PlacesResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.widget.ListView;

class FetchPoiDataTaskCompleteListener extends Activity implements
		AsyncTaskCompleteListener<PlacesResult> {

	Activity mActivity;
	Context mContext;
	Location mLocation;

	public FetchPoiDataTaskCompleteListener(Activity activity, Context context, Location location) {
		super();
		this.mActivity = activity;
		this.mContext = context;
		this.mLocation = location; 

	}

	@Override
	public void onTaskComplete(PlacesResult placesResult) {

		// We can also execute the postExecute Method Here

		if ((placesResult == null) || (placesResult.getResults().size() <= 0)) {

			
			// Show a dialog box here that no results are found
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("No Results");
			builder.setMessage(
					"No results in the search range. Try changing search range.")
					.setCancelable(false)
					.setPositiveButton("Settings",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// MyActivity.this.finish();
									// Settings to be invoked
									dialog.cancel();
									mActivity.finish();
									return;

								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									mActivity.finish();
									return;
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			
			
			return;


		}

		//List the Address here
		
		ListView listView = (ListView) mActivity.getWindow().getDecorView().findViewById(R.id.places_list);

		@SuppressWarnings("unchecked")
		List<Place> placesList = (List<Place>) placesResult.getResults();
		
		PlaceAdapter adapter = new PlaceAdapter(mActivity,
				R.layout.places_lists_item, placesList, mLocation);
		listView.setAdapter(adapter);
        
	}
}