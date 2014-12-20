package com.isaplings.travelfriend;

import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.isaplings.travelfriend.MyLocation.LocationResult;
import com.isaplings.travelfriend.lib.ButteryProgressBar;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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

// Last Modified by Navine on 19/Dec/2014


public class Travel extends Activity implements OnClickListener {

	
	private ActionBar actionBar;

	private static final String TAG = "Debug";
	// private Boolean flag = false;

	private Double latitude;
	private Double longitude;
	private Location mLocation;

	private Boolean locationFlag = false;

	private Menu mymenu = null;

	public static Context appContext;

	ButteryProgressBar progressBar;

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
			Intent settingsIntent = new Intent(getBaseContext(), TravelSettings.class);
        	startActivity(settingsIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void resetUpdating() {
		
		if (mymenu == null){
			return;
		}
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

	}

	@Override
	protected void onStart()
	{
		super.onStart();

		actionBar = getActionBar();
		actionBar.setTitle("Travel Friend");
		actionBar.setSubtitle("SOS Help, closer to you");
		
		actionBar.setIcon(R.drawable.location);

		// Load the AdHolder

		progressBar = new ButteryProgressBar(appContext);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				24));

		// retrieve the top view of our application
		final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
		decorView.addView(progressBar);

		ViewTreeObserver observer = progressBar.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				View contentView = decorView.findViewById(android.R.id.content);
				progressBar.setY(contentView.getY() - 10);

				ViewTreeObserver observer = progressBar.getViewTreeObserver();
				observer.removeGlobalOnLayoutListener(this);
				// observer.removeOnGlobalLayoutListener(this);
				Log.v("Debug", "MYGPS : on end of onGlobalLayout");

			}
		});

		Log.v("Debug",
				" MyGPS : Buttery Progress Bar Visiblity Check - isShown : "
						+ progressBar.isShown());

		progressBar.setVisibility(View.GONE);
		
		AdView adView = (AdView) findViewById(R.id.travel_ad_mob_view);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("TEST_DEVICE_ID")
				.addKeyword("travel")
				.build();
		adView.loadAd(adRequest);

		onRefresh();
		
		
	}
	
	@SuppressLint("InflateParams")
	private void AutoRefresh(){
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh,
				null);
		Animation rotation = AnimationUtils.loadAnimation(this,
				R.anim.rotate_refresh);
		rotation.setRepeatCount(Animation.INFINITE);
		iv.startAnimation(rotation);
		MenuItem menuItem = mymenu.findItem(R.id.action_refresh);

		menuItem.setActionView(iv);
		// End
		onRefresh();
		
	}

	//Need to remove this method
	public void onClick(View v) {
		// Closes the Activity

		finish();

	}

	public void onGetSoS(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnSOS);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetPharmacy(View v) {

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnPharmacy);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetATM(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnATM);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetRestaurant(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnRestaurant);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetRepairPal(View v) {

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnCarRepair);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetFuelStation(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnFuelPump);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetRestRoom(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnRestRoom);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetTowService(View v) {

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Tow Service Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnTow);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onGetHospital(View v) {

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Hospital Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnHospital);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void onShareLocation(View v) {
		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();
		disableRefreshButton();

		if (myLocation.getLocation(locationResult)) {

			// Hospital Icon Id is passed -which is equivalent of v.getId()

			LocationControl locationControlTask = new LocationControl(
					R.id.btnShareLoc);
			locationFlag = false;
			// Executing on parallel thread
			// Location Control Task will call the appropriate method with the
			// id
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	private void enableRefreshButton() {
		if (mymenu == null) {
			return;
		}
		MenuItem refreshButton = mymenu.findItem(R.id.action_refresh);
		refreshButton.setEnabled(true);

	}

	private void disableRefreshButton() {

		MenuItem refreshButton = mymenu.findItem(R.id.action_refresh);
		refreshButton.setEnabled(false);

	}

	private void SOSActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListSOSActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void PharmacyActivity() {
		// Create a new Intent

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListPharmacyActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void ATMActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListATMActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void RestaurantActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRestaurantActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void RepairPalActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRepairPalActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void FuelStationActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListFuelStationActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void RestRoomActivity() {

		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListRestRoomActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);

		enableRefreshButton();
		enableHomeScreenIcons();
	
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void TowServiceActivity() {
		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListTowServiceActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
	
		enableRefreshButton();
		enableHomeScreenIcons();
	
		
		startActivity(intent);

		Log.v("Debug", "MyGPS : Intent start initiated ...");

	}

	private void HospitalActivity() {
		if (mLocation == null) {
			return;
		}

		Bundle bundle = new Bundle();
		bundle.putParcelable("LOCATION", mLocation);

		Intent intent = new Intent(appContext, ListHospitalsActivity.class);

		// Location is sent as parcelable object
		intent.putExtras(bundle);
		
		enableRefreshButton();		
		enableHomeScreenIcons();
		
		startActivity(intent);
		Log.v("Debug", "MyGPS : Hospital intent - Started after icons enabled ...");

	}

	private void ShareLocationActivity() {
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
		
		enableRefreshButton();
		enableHomeScreenIcons();
	
		
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

	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {
			// Got the location!
			if (location == null) {
				Log.v(TAG, "MyGPSLocation Inside GotLocation: location is null");

				// implement - show message - #CodeReview

				mLocation = null;
				resetUpdating();
				enableHomeScreenIcons();
				enableRefreshButton();

				AlertDialog.Builder builder = new AlertDialog.Builder(
						Travel.this);
				builder.setTitle("Unable to get current location");
				builder.setMessage("Please check the settings of Location Services in the Phone Settings.");
				builder.setCancelable(true);
				builder.setNeutralButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
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

					Log.v(TAG, "MYGPSLocation : Inside onTaskComplete called ");

					// Extracted from Address Component in JSONObject
					// SubLocality - route || administrative_area_level_2 ||
					// administrative_area_level_1
					// Locality - locality || political
					// SubAdminArea - administrative_area_level_2 || country

					if (result == null) {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								Travel.this);
						builder.setTitle("Unable to get current address");
						builder.setMessage("Please check the settings of Network Services in the Phone Settings.");
						builder.setCancelable(true);
						builder.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});

						AlertDialog alert = builder.create();
						alert.show();

						// Resetting Location to Null
						mLocation = null;
						locationFlag = true;
						enableHomeScreenIcons();
						enableRefreshButton();

						return;
					}

					if ((result != null) & (result.size() > 0)) {

						String streetName = result.get(0).getSubLocality();

						String cityName = result.get(0).getLocality() + ", "
								+ result.get(0).getSubAdminArea();

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
						Log.v(TAG, "MYGPS : AddressTask - locationFlag :"
								+ locationFlag);

					}
				}
			}

			FetchMyDataTaskCompleteListener fm = new FetchMyDataTaskCompleteListener();
			GetMyAddressTask addTask = new GetMyAddressTask(Travel.this,
					appContext, fm);

			if (mLocation == null)
				return;

			if (mLocation != null) {
				// btnGetLocation.setEnabled(true);
				Log.v(TAG,
						"MyGPSLocation : GetMyAddress Task Execute for Location :"
								+ mLocation.getLatitude() + ","
								+ mLocation.getLongitude());
				// addTask.execute(mLocation);
				addTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						mLocation);
			}
			return;
		}

		@Override
		public void gotLastLocation(Location location) {

			final Location mLocation = location;
			// Show a dialog box here that no results are found

			// if there is ui issue then
			// resetUpdating();

			AlertDialog.Builder builder = new AlertDialog.Builder(Travel.this);
			builder.setTitle("Alert");
			builder.setMessage(
					"Unable to retrieve current location. Do you want to proceed with last known location ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
									gotLocation(mLocation);
									return;

								}
							})
					.setNegativeButton("No",
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

		}
	}; // LocationResult Definition Ends

	class LocationControl extends AsyncTask<Context, Void, Boolean> {

		int itemId;

		LocationControl() {
			this.itemId = 0;
		};

		LocationControl(int itemId) {
			this.itemId = itemId;
		}

		protected void onPreExecute() {
			// Empyt on PreExecute
			Log.v(TAG, " MYGPS : locaction control preExecute method  ");
			Log.v(TAG, " MYGPS : LC preExecute : locationFlag : "
					+ locationFlag);
			Log.v(TAG,
					" MYGPS : LC preExecute : ProgressBar : "
							+ progressBar.isShown());

			if (!progressBar.isShown()) {
				Log.v("Debug",
						" MYGPS : Buttery Progress Bar is to be made Visible ");

				progressBar.setVisibility(View.VISIBLE);
				Log.v("Debug",
						"MYGPS : set ButteryProgressBar Visible - isShown : "
								+ progressBar.isShown());

			}

		}

		protected Boolean doInBackground(Context... params) {
			// Wait locactionFlag to toggle to see if we can get a location from
			// either
			// network or GPS, otherwise stop

			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

			// Long t = Calendar.getInstance().getTimeInMillis();
			// while (!locationFlag
			// && Calendar.getInstance().getTimeInMillis() - t < 100000) {

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

			Log.v(TAG,
					" MYGPS : LC postExecute : ProgressBar : "
							+ progressBar.isShown());

			if (progressBar.isShown()) {

				Log.v("Debug", " MYGPS : Buttery Progress Bar is to be removed");

				progressBar.setVisibility(View.GONE);
				Log.v("Debug",
						"MYGPS : After set ButteryProgressBar set GONE - isShown : "
								+ progressBar.isShown());

			}

			// protected void onPostExecute() {
			Log.v(TAG, " MYGPS : locaction control postExecute Method  ");
			Log.v(TAG, " MYGPS : LC postExecute : locationFlag : "
					+ locationFlag);

			if (flag) {

				if (mLocation == null) {
					Log.v(TAG,
							" MYGPS : LocControl : locaction identified as null ");

				} else {
					Log.v(TAG,
							" MYGPS : LocControl : locaction was retrieved successfully  ");
					useLocation(itemId);

				}
			} else {
				Log.v(TAG,
						"MYGPS : LocControl : location retreival was unsuccessfull");

			}

		}
	}

	public void onRefresh() {

		Log.v(TAG, "MyGPSLocation : onRefresh Click");

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();

		if (myLocation.getLocation(locationResult)) {

			LocationControl locationControlTask = new LocationControl(
					R.id.action_refresh);
			locationFlag = false;
			// Executing on parallel thread
			locationControlTask.executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, this);
		}

	}

	public void useLocation(int itemId) {
		// TODO Auto-generated method stub
		switch (itemId) {
		case R.id.action_refresh:
			enableHomeScreenIcons();
			return;

		case R.id.btnShareLoc:
			ShareLocationActivity();
			return;

		case R.id.btnHospital:
			HospitalActivity();
			return;

		case R.id.btnTow:
			TowServiceActivity();
			return;

		case R.id.btnRestRoom:
			RestRoomActivity();
			return;

		case R.id.btnFuelPump:
			FuelStationActivity();
			return;

		case R.id.btnCarRepair:
			RepairPalActivity();
			return;

		case R.id.btnRestaurant:
			RestaurantActivity();
			return;

		case R.id.btnATM:
			ATMActivity();
			return;
		case R.id.btnPharmacy:
			PharmacyActivity();
			return;
		case R.id.btnSOS:
			SOSActivity();
			return;

		}
	}

	public void onTravelActivity(View v) {
		Log.v(TAG, "MyGPSLocation : onRefresh Click");

		MyLocation myLocation = new MyLocation(Travel.this, this);

		disableHomeScreenIcons();

		myLocation.getLocation(locationResult);

		LocationControl locationControlTask = new LocationControl();
		locationFlag = false;
		// Executing on parallel thread
		locationControlTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
				this);

	}

}
