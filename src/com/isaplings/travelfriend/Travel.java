package com.isaplings.travelfriend;

import java.util.List;

import com.isaplings.travelfriend.MyLocation.LocationResult;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
//import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

// Since this Base Class for TravelFriend
// there will be many changes to this class
// Please ensure there are no regressions to this file

// Please go through the comments to see 
// the pending changes

// Major part of the code need to be refactores
// especially the inner class and method definitions

// Last Modified by Navine on 15/Nov/2014

// Modification in progress for getHospital data 15/Nov/2014

public class Travel extends Activity implements OnClickListener {

	/*
	 * private Button btnGetLocation = null; private EditText editLocation =
	 * null; private ProgressBar pb = null;
	 */
	private ActionBar actionBar;

	private static final String TAG = "Debug";
	// private Boolean flag = false;

	private Double latitude;
	private Double longitude;
	private Location mLocation;

	private Boolean locationFlag = false;

	private Menu mymenu;

	public static Context appContext;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		mymenu = menu;
		return super.onCreateOptionsMenu(menu);

	}

	@SuppressLint("InflateParams")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_refresh:
			// adding code for refresh animation
			// can be moved to pre-execute task. Task for Navine
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh,
					null);
			Animation rotation = AnimationUtils.loadAnimation(this,
					R.anim.rotate_refresh);
			rotation.setRepeatCount(Animation.INFINITE);
			iv.startAnimation(rotation);
			item.setActionView(iv);
			// End
			onRefresh();
			return true;
		case R.id.action_settings:
			// openSettings();
			// Need to check if settings option is required
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void resetUpdating() {
		// Get our refresh item from the menu
		MenuItem m = mymenu.findItem(R.id.action_refresh);
		if (m.getActionView() != null) {
			// Remove the animation.
			m.getActionView().clearAnimation();
			m.setActionView(null);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);

		appContext = this.getApplicationContext();

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// This need to changed

		actionBar = getActionBar();
		actionBar.setTitle("Travel Friend");
		actionBar.setSubtitle("SOS Help, closer to you");

		// Load the AdHolder

	}

	public void onClick(View v) {
		// Closes the Activity
		finish();

	}

	public void onGetSoS(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListSOSActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetPharmacy(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListPharmacyActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetATM(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListATMActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetRestaurant(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRestaurantActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetRepairPal(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRepairPalActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetFuelStation(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListFuelStationActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetRestRoom(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRestRoomActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetTowService(View v) {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListTowServiceActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onGetHospital(View v) {
		// Create a new Intent

		// Try to get the latest location

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListHospitalsActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void onShareLocation(View v) {
		// Create a new Intent

		// Try to get the latest location

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ShareMyLocation.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	public void disableHomeScreenIcons() {
		// TODO Auto-generated method stub

		Log.v(TAG, "MYGPSLocation : Disabling the Layout Icons ");

		GridLayout layout = (GridLayout) findViewById(R.id.home_screen);

		for (int i = 0; i < layout.getChildCount(); i++) {
			LinearLayout linlayout = (LinearLayout) layout.getChildAt(i);

			View child = linlayout.getChildAt(i);

			for (int j = 0; j < linlayout.getChildCount(); j++) {
				child = linlayout.getChildAt(j);
				child.setEnabled(false);

			}

		}

	}

	public void enableHomeScreenIcons() {
		// TODO Auto-generated method stub

		Log.v(TAG, "MYGPSLocation : Enabling the Layout Icons ");

		GridLayout layout = (GridLayout) findViewById(R.id.home_screen);

		for (int i = 0; i < layout.getChildCount(); i++) {
			LinearLayout linlayout = (LinearLayout) layout.getChildAt(i);

			View child = linlayout.getChildAt(i);

			for (int j = 0; j < linlayout.getChildCount(); j++) {
				child = linlayout.getChildAt(j);
				child.setEnabled(true);

			}

		}

	}

	public void onRefresh() {

		Log.v(TAG, "MyGPSLocation : onClick");

		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				// Got the location!
				if (location == null) {
					Log.v(TAG,
							"MyGPSLocation Inside GotLocation: location is null");

					// implement - show message - #CodeReview

					mLocation = null;
					resetUpdating();
					enableHomeScreenIcons();

					AlertDialog.Builder builder = new AlertDialog.Builder(
							Travel.this);
					builder.setTitle("Location Services Disabled");
					builder.setMessage("Please check Location Services in the Phone Settings to get current location");
					builder.setCancelable(true);
					builder.setNeutralButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert = builder.create();
					locationFlag = true;
					alert.show();
					return;
				}

				else {

					Log.v(TAG,
							"MyGPSLocation Inside GotLocation: location is identified");

					mLocation = location;
					latitude = location.getLatitude();
					longitude = location.getLongitude();
					Log.v(TAG, "GPS Location - Latitude " + latitude);
					Log.v(TAG, "GPS Location - Longitude " + longitude);

					if ((latitude != null) && (longitude != null)) {

						Log.v(TAG,
								"MYGPSLocation : Calling Async Task to get the City Name");

						/*----------to get City-Name from coordinates ------------- */
						getAddressFromLocation(appContext);
						//

					} else {
						Log.v(TAG, "MYGPSLocation : flag is false");
						return;
					}

				}

			}

			@Override
			protected void getAddressFromLocation(Context context) {
				// This class will get the address from location

				Log.v(TAG, "MYGPSLocation : getAddressFromLocation called ");

				class FetchMyDataTaskCompleteListener implements
						AsyncTaskCompleteListener<List<Address>> {

					@Override
					public void onTaskComplete(List<Address> result) {

						// We can also execute the postExecute Method Here

						Log.v(TAG,
								"MYGPSLocation : Inside onTaskComplete called ");

						// Extracted from Address Component in JSONObject
						// SubLocality - route || administrative_area_level_2 ||
						// administrative_area_level_1
						// Locality - locality || political
						// SubAdminArea - administrative_area_level_2 || country

						if (result == null) {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									Travel.this);
							builder.setTitle("Network Services Disabled");
							builder.setMessage("Please check Network Services in the Phone Settings to get current location");
							builder.setCancelable(true);
							builder.setNeutralButton(android.R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});

							AlertDialog alert = builder.create();
							alert.show();

							// Resetting Location to Null
							mLocation = null;
							return;
						}

						if ((result != null) & (result.size() > 0)) {

							String streetName = result.get(0).getSubLocality();

							String cityName = result.get(0).getLocality()
									+ ", " + result.get(0).getSubAdminArea();

							actionBar.setTitle(streetName);
							actionBar.setSubtitle(cityName);

							Bundle extras = new Bundle();
							extras.putString("STREETNAME", streetName);
							extras.putString("CITYNAME", cityName);
							extras.putString("COUNTRYNAME", result.get(0)
									.getCountryName());
							extras.putString("COUNTRYCODE", result.get(0)
									.getCountryCode());
							extras.putString("LOCADDRESS", result.get(0)
									.getFeatureName());

							mLocation.setExtras(extras);

							locationFlag = true;
							Log.v(TAG,"MYGPS : AddressTask - locationFlag :" + locationFlag);

						}
					}
				}

				FetchMyDataTaskCompleteListener fm = new FetchMyDataTaskCompleteListener();
				GetMyAddressTask addTask = new GetMyAddressTask(Travel.this,
						appContext, fm);

				if (mLocation == null) return ;

				if (mLocation != null) {
					// btnGetLocation.setEnabled(true);
					Log.v(TAG,
							"MyGPSLocation : GetMyAddress Task Execute for Location :"
									+ mLocation.getLatitude() + ","
									+ mLocation.getLongitude());
					//addTask.execute(mLocation);
					addTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mLocation);
				}
				return;
			}

			@Override
			public void gotLastLocation(Location location) {
				
				
				final Location mLocation = location;
				// Show a dialog box here that no results are found

				// if there is ui issue then
				// resetUpdating();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Travel.this);
				builder.setTitle("Alert");
				builder.setMessage(
						"Unable to retrieve current location. Click Ok to proceed with last known location.")
						.setCancelable(false)
						.setPositiveButton("Ok",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// MyActivity.this.finish();
										// Settings to be invoked
										dialog.cancel();
										gotLocation(mLocation);
										return;

									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
										gotLocation(null);
										return;
									}
								});
				AlertDialog alert = builder.create();
				alert.show();

				// gotLocation(location);

			}
		}; // LocationResult Definition Ends

		class LocationControl extends AsyncTask<Context, Void, Boolean> {

			protected void onPreExecute() {
				// Empyt on PreExecute
				Log.v (TAG, " MYGPS : locaction control preExecute method  ");
				Log.v (TAG, " MYGPS : LC preExecute : locationFlag : " + locationFlag);

				
			}

			protected Boolean doInBackground(Context... params) {
				// Wait 10 seconds to see if we can get a location from either
				// network or GPS, otherwise stop
				
				Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

				
				//Long t = Calendar.getInstance().getTimeInMillis();
				//while (!locationFlag
					//	&& Calendar.getInstance().getTimeInMillis() - t < 100000) {
					
				while (!locationFlag) {
				try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				;
				Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

				return locationFlag;
			}

			@Override
			protected void onPostExecute(Boolean flag) {

			
			//protected void onPostExecute() {
				Log.v (TAG, " MYGPS : locaction control postExecute Method  ");
				Log.v (TAG, " MYGPS : LC postExecute : locationFlag : " + locationFlag);

				
				if (flag) {
				
					if (mLocation == null) {
						Log.v (TAG, " MYGPS : LocControl : locaction identified as null ");

					} else
					Log.v (TAG, " MYGPS : LocControl : locaction was retrieved successfully  ");
				} else 
				{
					Log.v (TAG, "MYGPS : LocControl : location retreival was unsuccessfull");
					
				}
				
				// if (currentLocation != null)
				// {
				// //useLocation();
				// return ;
				// }
				// else
				// {
				// //Couldn't find location, do something like show an alert
				// dialog
				// }

			}
		}

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();

		myLocation.getLocation(locationResult);

		LocationControl locationControlTask = new LocationControl();
		locationFlag = false;
		//locationControlTask.execute(this);
		locationControlTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
		
		
		
			}

}
