package com.isaplings.travelfriend;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.models.PlaceDetails;
import com.a2plab.googleplaces.result.PlaceDetailsResult;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;
import com.isaplings.travelfriend.lib.ButteryProgressBar;
import com.isaplings.travelfriend.lib.POITextSearchTask;
import com.isaplings.travelfriend.model.EmergencyRecord;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListSOSActivity extends Activity {

	private static final String TAG = "Debug";

	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<String>> listDataChild;
	private ActionBar actionBar;

	List<String> ambulance = new ArrayList<String>();
	List<String> police = new ArrayList<String>();

	String countryName;
	String countryCode;

	ButteryProgressBar progressBar;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_sos_places);

		Bundle bundle = this.getIntent().getExtras();

		Location mLocation = bundle.getParcelable("LOCATION");

		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");

		Log.v(TAG, "MyGPS : Street Name : " + streetName);
		Log.v(TAG, "MyGPS : CityName : " + cityName);

		countryName = mLocation.getExtras().getString("COUNTRYNAME");
		countryCode = mLocation.getExtras().getString("COUNTRYCODE");

		Log.v(TAG, "MyGPS : Country Name : " + countryName);
		Log.v(TAG, "MyGPS : Counrty Code : " + countryCode);

		Log.v("Debug", " MYGPS : List Places Activity loaded");

		// Code for setting action bar icon and title as custom view
		// Fixing bug to resolve, only icon click on action bar should take back
		// to Home

		String abTitle = streetName + "\n" + cityName;
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.ab_title);

		TextView title = (TextView) findViewById(android.R.id.text1);
		title.setText(abTitle);

		actionBar.setIcon(R.drawable.sos);

		// get the listview
		expListView = (ExpandableListView) findViewById(R.id.lvExp);

		// preparing list data
		prepareListData();

		listAdapter = new ExpandableListAdapter(this, listDataHeader,
				listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
		expListView.expandGroup(2);

		// Static Implementation is complete

		// Dynamic Implementation starts

		// Get Ambulance Data

		TextSearchTaskListener ambListner = new TextSearchTaskListener(
				"ambulance");

		List<String> sTypes = new ArrayList<String>();
		sTypes.add("hospital");
		sTypes.add("health");
		sTypes.add("doctor");

		String sText = "ambulance";

		POITextSearchTask getAmbulance = new POITextSearchTask(
				ListSOSActivity.this, ambListner, sTypes, sText);

		// getAmbulance.execute(mLocation);

		getAmbulance.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				mLocation);

		sTypes = new ArrayList<String>();
		sTypes.add("police");

		sText = "Police Station";

		TextSearchTaskListener polListener = new TextSearchTaskListener(
				"police");

		POITextSearchTask getPoliceStation = new POITextSearchTask(
				ListSOSActivity.this, polListener, sTypes, sText);

		// getPoliceStation.execute(mLocation);
		getPoliceStation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				mLocation);

		Log.v("Debug", "MYGPS : Initialising Buttery Progress Bar");

		progressBar = new ButteryProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				24));

		// retrieve the top view of our application
		final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
		decorView.addView(progressBar);

		ViewTreeObserver observer = progressBar.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				View contentView = decorView.findViewById(android.R.id.content);
				progressBar.setY(contentView.getY() - 10);

				ViewTreeObserver observer = progressBar.getViewTreeObserver();
				observer.removeGlobalOnLayoutListener(this);
				//observer.removeOnGlobalLayoutListener(this);
				Log.v("Debug", "MYGPS : on end of onGlobalLayout");

			}
		});

		// Listener method implementation

		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
				int len = listAdapter.getGroupCount();

				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						expListView.collapseGroup(i);
					}
				}
				// Toast.makeText(getApplicationContext(),
				// listDataHeader.get(groupPosition) + " Expanded",
				// Toast.LENGTH_SHORT).show();
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
				// Toast.makeText(getApplicationContext(),
				// listDataHeader.get(groupPosition) + " Collapsed",
				// Toast.LENGTH_SHORT).show();

			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(
						getApplicationContext(),
						listDataHeader.get(groupPosition)
								+ " : "
								+ listDataChild.get(
										listDataHeader.get(groupPosition)).get(
										childPosition), Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		});
	}

	class TextSearchTaskListener implements
			AsyncTaskCompleteListener<PlacesResult> {

		String listType;

		public TextSearchTaskListener(String listType) {
			super();
			this.listType = listType;
		}

		@Override
		public void onTaskComplete(PlacesResult placesResult) {
			// TODO Auto-generated method stub

			Log.v(TAG, "MyGPS : Get Ambulance Task Completed");

			// No error message - if unable to get data
			if ((placesResult == null)
					|| (placesResult.getResults().size() <= 0)) {
				Log.v(TAG, "MyGPS : Places result is null or empty");
				
				if (progressBar.isShown()) {
					Log.v("Debug",
							"Buttery Progress Bar is Visible - isShown ");

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							progressBar.setVisibility(View.GONE);
							Log.v("Debug",
									"MYGPS : Inside Handler :  set ButteryProgressBar InVisible ");

						}
					}, 0);

				}

				
				return;
			}

			@SuppressWarnings("unchecked")
			List<Place> placesList = (List<Place>) placesResult.getResults();

			// placesList can be filter based on Types
			// But Types is not implemented in Place (model)

			// Setting a limit for the iteration
			int iterLimit = 4;
			if (placesList.size() < 4) {
				iterLimit = placesList.size();
			}

			for (int i = 0; i < iterLimit; i++) {

				Log.v(TAG, "MyGPS : Places Name : "
						+ placesList.get(i).getName());
				Log.v(TAG, "MyGPS : Places Id : "
						+ placesList.get(i).getPlaceId());
				// ambulance.add(placesList.get(i).getName());
				getContactNumber(placesList.get(i).getPlaceId(), listType);

			}

			// listAdapter.notifyDataSetChanged();

		}

	}

	public String getContactNumber(String placeId, String list) {

		final String listType = list;

		class GetPlaceDetailsTask extends
				AsyncTask<String, String, PlaceDetailsResult> {

			@Override
			protected PlaceDetailsResult doInBackground(String... params) {
				// TODO Auto-generated method stub
				String placeId = params[0];

				GooglePlaces gp = new GooglePlaces(
						"AIzaSyAPL4gar2x7nQKc9p-bRhDa4RCgSL1qTRA");
				PlaceDetailsResult placeDetailsResult = new PlaceDetailsResult();
				try {
					placeDetailsResult = (PlaceDetailsResult) gp
							.getPlaceDetails(placeId);
					if (placeDetailsResult.getStatusCode() != StatusCode.OK) {
						return null;
					}

				} catch (IOException e) {

					e.printStackTrace();
					return null;
				}
				Log.v("Debug",
						" MyGPS : Successfully executed getPlaceDetails ");

				return placeDetailsResult;
			}

			protected void onPreExecute() {

				// Butter Progress Bar

				// Log.v("Debug", "My GetApplication Context " +
				// getApplicationContext().toString() );
				// Log.v("Debug", "ListSOSActivity Context " +
				// ListSOSActivity.this.toString() );
				// Log.v("Debug", "GetBase Context " +
				// getBaseContext().toString() );

				if (!progressBar.isShown()){
					Log.v ("Debug", "MyGPS : InPreExecute - Setting Progress Bar to visible");
					
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							progressBar.setVisibility(View.VISIBLE);
							Log.v("Debug",
									"MYGPS : Inside Handler :  set ButteryProgressBar Visible ");

						}
					}, 0);

					
				}
				
				
			}

			protected void onPostExecute(PlaceDetailsResult placeDetailsResult) {

				Log.v("Debug", " MyGPS : getPlaceDetails in PostExecute Method");

				if (placeDetailsResult == null) {
					return;
				}

				PlaceDetails placeDetails = placeDetailsResult.result;

				if (placeDetailsResult.getStatusCode() == StatusCode.OK) {

					Log.v("Debug", " MyGPS : getPlaceDetails status is OK");

					Log.v("Debug", " MyGPS : Name :  " + placeDetails.getName());
					Log.v("Debug", " MyGPS : Inter Phone Number: "
							+ placeDetails.getInternationalPhoneNumber());
					Log.v("Debug",
							" MyGPS : PhoneNumber: "
									+ placeDetails.getFormattedPhoneNumber());

					// Append the name and phonenumber to list
					String phoneNumber = new String();
					if ((placeDetails.getInternationalPhoneNumber() == null)
							|| (placeDetails.getInternationalPhoneNumber()
									.isEmpty())) {
						phoneNumber = placeDetails
								.getInternationalPhoneNumber();

					} else {
						Log.v("Debug",
								" MyGPS PlacesList : Internation phone number is null  "
										+ placeDetails
												.getFormattedPhoneNumber());
						phoneNumber = placeDetails.getFormattedPhoneNumber();
					}

					if (phoneNumber != null && !phoneNumber.isEmpty()) {

						if (listType.equals("ambulance")) {
							ambulance.add(placeDetails.getName() + "\n"
									+ phoneNumber);
							listAdapter.notifyDataSetChanged();

							if (progressBar.isShown()) {
								Log.v("Debug",
										"Buttery Progress Bar is Visible - isShown ");

								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										progressBar.setVisibility(View.GONE);
										Log.v("Debug",
												"MYGPS : Inside Handler :  set ButteryProgressBar InVisible ");

									}
								}, 0);

							}

							Log.v("Debug",
									" MyGPS PlacesList : Updated Exp List " + placeDetails.getName());

						}

						if (listType.equals("police")) {
							police.add(placeDetails.getName() + "\n"
									+ phoneNumber);
							listAdapter.notifyDataSetChanged();

							if (progressBar.isShown()) {

								Log.v("Debug",
										"Buttery Progress Bar is Visible - isShown ");

								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										progressBar.setVisibility(View.GONE);
										Log.v("Debug",
												"MYGPS : Inside Handler :  set ButteryProgressBar InVisible ");

									}
								}, 0);

							}

							Log.v("Debug",
									" MyGPS PlacesList : Updated Expandable List"
											+ placeDetails.getName());

						}

					}

					Log.v("Debug",
							" MyGPS PlacesList : Put  the PlaceDetails from Places API into CachePlaceList");

					return;

				}

				else {
					Log.v("Debug", " MyGPS : getPlaceDetails status is : "
							+ placeDetailsResult.getStatusCode());

					// Show Alert Unable to retrieve more information

					return;

				}

			}
		}

		GetPlaceDetailsTask placeDetails = new GetPlaceDetailsTask();
		placeDetails.execute(placeId);

		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Preparing the list data
	 */
	private void prepareListData() {
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding header data
		listDataHeader.add("Buddies");
		listDataHeader.add("Police");
		listDataHeader.add("Ambulance");
		listDataHeader.add("Toll Booth");
		listDataHeader.add("Helpline");

		// Adding child data
		List<String> buddies = new ArrayList<String>();
		buddies.add("Elango +91 9880649789");
		buddies.add("Navine +91 80 42120570");
		buddies.add("Senthilnathan +91 9686033557");
		buddies.add("Ramesh +91 9916922424");

		// List<String> police = new ArrayList<String>();
		// police.add("100");
		// police.add("102");
		// police.add("104");

		List<String> tollfree = new ArrayList<String>();
		tollfree.add("08025746");
		tollfree.add("04412345");
		tollfree.add("02289745");

		// List<String> ambulance = new ArrayList<String>();
		// ambulance.add("1088");
		// ambulance.add("108");
		// ambulance.add("Immediate Assistants - +61 2 9460 2851");
		// ambulance.add("Paddington Ambulance Station - +61 2 9320 7796");

		List<String> helpline = new ArrayList<String>();
		// helpline.add("18004087346");
		// helpline.add("18008364565");

		// Get the Data from Static File

		EmergencyRecord emergencyRec = new EmergencyRecord();
		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = this.getAssets();
			InputStream inputStream = assetManager
					.open("emergency_numbers.json");

			int x;
			while ((x = inputStream.read()) != -1) {
				stringBuilder.append((char) x);

			}
			String data = stringBuilder.toString();

			JSONObject jsonObj = new JSONObject(data);
			JSONArray jsonArr = jsonObj.getJSONArray("results");

			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject item = jsonArr.getJSONObject(i);

				if (item.getString("Country_Code")
						.equalsIgnoreCase(countryCode)
						|| item.getString("Country").equalsIgnoreCase(
								countryName)) {
					JSONObject emer = item.getJSONObject("Emergency");
					emergencyRec.setPolice(emer.getString("Police"));
					emergencyRec.setAmbulance(emer.getString("Ambulance"));
					emergencyRec.setFire(emer.getString("Fire"));

				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ADD THE reCORDS NOW

		police.add("Emergency Contact \n" + emergencyRec.getPolice());
		ambulance.add("Emergency Contact \n" + emergencyRec.getAmbulance());

		listDataChild.put(listDataHeader.get(0), buddies); // Header, Child data
		listDataChild.put(listDataHeader.get(1), police);
		listDataChild.put(listDataHeader.get(2), ambulance);
		listDataChild.put(listDataHeader.get(3), tollfree);
		listDataChild.put(listDataHeader.get(4), helpline);
	}

}
