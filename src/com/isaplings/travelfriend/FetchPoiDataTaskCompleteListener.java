package com.isaplings.travelfriend;


import org.gmarz.googleplaces.models.PlacesResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

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
					"No results in your search range. Try changing search range.")
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

		PlaceAdapter adapter = new PlaceAdapter(mActivity,
				R.layout.places_lists_item, placesResult.getPlaces());
		listView.setAdapter(adapter);
        
	}
}