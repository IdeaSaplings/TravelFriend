package com.isaplings.travelfriend;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.models.PlaceDetails;
import com.a2plab.googleplaces.result.PlaceDetailsResult;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;
import com.isaplings.travelfriend.lib.SortPlaceList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

class FetchPoiDataTaskCompleteListener extends Activity implements
		AsyncTaskCompleteListener<PlacesResult> {

	Activity mActivity;
	Context mContext;
	Location mLocation;

	HashMap<String, String> cachemapIdList = new HashMap<String, String>();
	List<PlaceDetails> cachePlaceRecord = new ArrayList<PlaceDetails>();

	public FetchPoiDataTaskCompleteListener(Activity activity, Context context,
			Location location) {
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

		// List the Address here

		ListView listView = (ListView) mActivity.getWindow().getDecorView()
				.findViewById(R.id.places_list);

		@SuppressWarnings("unchecked")
		List<Place> placesList = (List<Place>) placesResult.getResults();

		// Sort the PlacesList
		// sortByDistance(placesList, mLocation.latitude, mLocation.longitude)

		// placesList = SortPlaceList.sortbyRating(placesList);

		placesList = SortPlaceList.sortbyDistance(placesList, mLocation);

		PlaceAdapter adapter = new PlaceAdapter(mActivity,
				R.layout.places_lists_item, placesList, mLocation);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// parent.getItemAtPosition(position).getClass().getName();

				final String uniqueId = parent.getItemAtPosition(position)
						.toString();

				Place placeRecord = (Place) parent.getItemAtPosition(position);

				//String placeId = placeRecord.getId();
				String placeId = new String();

				if (cachemapIdList.containsKey(uniqueId)) {

					Log.v("Debug",
							"MyGPS : Display the CachePlacesList Details in PopWindow");

					placeId = (String) cachemapIdList.get(uniqueId);

					Log.v("Debug", "MyGPS : In Cache Mode:  Unique id is "
							+ uniqueId);
					Log.v("Debug", "MyGPS : In Cache Mode: Place id is "
							+ placeId);

					PlaceDetails mplaceDetails = getPlaceDetailsFromCache(placeId,
							cachePlaceRecord);

					Log.v("Debug", "MyGPS : In cache Mode:  mPlace name is "
							+ mplaceDetails.getName());

					getPopupWindow(mplaceDetails);

				} else {
					// Get the Placedetails from Places API
					// Use the placeID to get the PlaceDetails

					Log.v("Debug",
							" MyGPS : PlacesList : Get the PlaceDetails from Places API");

					Log.v("Debug", "MyGPS : In Get Mode:  Unique id is "
							+ uniqueId);
					Log.v("Debug", "MyGPS : In Get Mode: Place id is "
							+ placeRecord.getId());

					Log.v("Debug", "MyGPS : In Get Mode:  Place name is "
							+ placeRecord.getName());


					
					GetPlaceDetailsTask placeDetails = new GetPlaceDetailsTask(uniqueId);
					placeDetails.execute(placeRecord.getReference());
					
					
									
				}

			}

			@SuppressWarnings("unused")
			class GetPlaceDetailsTask extends
					AsyncTask<String, String, PlaceDetailsResult> {

				String uId;
				
				GetPlaceDetailsTask(String id){
					super();
					this.uId = id;
				}
				
				protected void onPreExecute(String... params) {

				}

				@Override
				protected PlaceDetailsResult doInBackground(String... params) {
					// TODO Auto-generated method stub
					String placeId = params[0];
					
					GooglePlaces gp = new GooglePlaces("AIzaSyAPL4gar2x7nQKc9p-bRhDa4RCgSL1qTRA");
					PlaceDetailsResult placeDetailsResult = new PlaceDetailsResult();
					try {
						 placeDetailsResult =  (PlaceDetailsResult) gp.getPlaceDetails(placeId);
						 if(placeDetailsResult.getStatusCode()!=StatusCode.OK){
							 return null;
						 }
						 
					} catch (IOException e) {

						e.printStackTrace();
						return null;
					}
					Log.v ("Debug", " MyGPS : Successfully executed getPlaceDetails ");


					return placeDetailsResult;
				}

				protected void onPostExecute(PlaceDetailsResult placeDetailsResult) {
					
					
					
					Log.v ("Debug", " MyGPS : getPlaceDetails in PostExecute Method");
					
					if (placeDetailsResult == null) {
						return;
					}
					
					PlaceDetails placeDetails = placeDetailsResult.result;					
					
					if(placeDetailsResult.getStatusCode()==StatusCode.OK){

						Log.v ("Debug", " MyGPS : getPlaceDetails status is OK");

						Log.v ("Debug", " MyGPS : Name :  " + placeDetails.getName());
						Log.v ("Debug", " MyGPS : Address: " + placeDetails.getFormattedAddress());
						Log.v ("Debug", " MyGPS : Inter Phone Number: " + placeDetails.getInternationalPhoneNumber());
						Log.v ("Debug", " MyGPS : Web Site: " + placeDetails.getWebsite());
						// call the caching method
						// call the getPopUpmethd;


						Log.v("Debug",
								" MyGPS PlacesList : Put  the PlaceDetails from Places API into CachePlaceList");

						
						cachePlaceRecord.add(placeDetails);
						cachemapIdList.put(uId, placeDetails.getId());
						
						getPopupWindow(placeDetails);

						
						Log.v("Debug",
								" MyGPS Display te CachePlacesList Details in PopWindow");


						
						return;
						
					}
					
					else {
						Log.v ("Debug", " MyGPS : getPlaceDetails status is : " + placeDetailsResult.getStatusCode());
						
						//Show Alert Unable to retrieve more information
						
						return;

						
					}
					
					

				}

			}

			private PlaceDetails getPlaceDetailsFromCache(String uId,
					List<PlaceDetails> cachePlaceList) {
				// TODO Auto-generated method stub

				// change this method ot for each()

				for (int i = 0; i < cachePlaceList.size(); i++) {

					
					if (cachePlaceList.get(i).getId().equals(uId)) {
						return cachePlaceList.get(i);
					}

				}
				return null;

			}

		});

	}

	private void getPopupWindow(PlaceDetails placeDetails) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup vGroup = (ViewGroup) mActivity
				.findViewById(R.id.pop_element_window);
		View layout = inflater.inflate(R.layout.popup, vGroup);
		// popWindow = new PopupWindow(layout, 400, 350, true);
		final PopupWindow popWindow = new PopupWindow(layout,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);

		// The backgroundDrawable is importat - to make outSideToucable work

		popWindow.setBackgroundDrawable(new BitmapDrawable(mActivity
				.getResources(), ""));

		popWindow.setOutsideTouchable(true);
		
		// Load all values to the layout
		
		TextView nameView = (TextView) layout.findViewById(R.id.poi_name);
		nameView.setText(placeDetails.getName());
	
		TextView addressView = (TextView) layout.findViewById(R.id.poi_address);
		addressView.setText(placeDetails.getFormattedAddress());
		
		TextView localPhoneView = (TextView) layout.findViewById(R.id.poi_local_phone);
		localPhoneView.setText("Phone (Local) - "+ placeDetails.getFormattedPhoneNumber());
		
		TextView interPhoneView = (TextView) layout.findViewById(R.id.poi_inter_phone);
		interPhoneView.setText("Phone (Inter) - "+ placeDetails.getInternationalPhoneNumber());
		
		TextView websiteLinkView = (TextView) layout.findViewById(R.id.poi_website);
		websiteLinkView.setText("Website - "+ placeDetails.getWebsite());
		
		
		popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

		popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				popWindow.dismiss();
				// end may TODO anything else
			}
		});

	}

}