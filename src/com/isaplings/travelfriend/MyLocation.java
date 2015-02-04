package com.isaplings.travelfriend;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

// This Class needs refactoring once the logging procedure is finalized

// This class has an abstract class defined - LocationResult that needs
// to be implemented. LocationResult has to abstract methods
// gotLocation and getAddressFromLocation

// MyLocation method is used to get the Current Location 
// using LocationManager and implementing LocationListener

// Source of MyLocation :

// Last Modified by Navine on 30/Sep/2014
// Review Pending

public class MyLocation {
	Timer timer;
	LocationManager locationManager;
	LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	Activity mActivity;
	Context mContext;

	MyLocation(Activity activity, Context context) {
		mActivity = activity;
		mContext = context;

	}

	public boolean getLocation(LocationResult result) {
		// Log.v("MY GPS", "My GPSLocation : inside GetLocation");

		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		if (locationManager == null)
			locationManager = (LocationManager) mContext
					.getSystemService(Context.LOCATION_SERVICE);

		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {

			// Log.v("My GPS", "MyGPSLocation : GPS is throwing exception");

		}
		try {
			network_enabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
			// Log.v("My GPS", "MyGPSLocation : Network is throwing exception");

		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {

			// Log.v("My GPS",
			// "MyGPSLocation : Both GPS & Network is disabled");
			// Fix the bug here - Navine
			locationResult.gotLocation(null);
			return false;
		}
		if (gps_enabled) {
			// Log.v("My GPS", "MyGPSLocation : GPS is enabled");
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		}
		if (network_enabled) {
			// Log.v("My GPS", "MyGPSLocation : Network is enabled <<");

			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}
		timer = new Timer();
		timer.schedule(new GetLastLocation(mActivity), 30000);
		return true;
	}

	// only onLocationChanged is implemented
	// check if other methods are also to be implemented

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Log.v("My GPS",
			// "MyGPSLocation : GPS is enabled - Inside GPS Location Listener");
			timer.cancel();
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerNetwork);
			locationResult.gotLocation(location);

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	// only onLocationChanged is implemented
	// check if other methods are also to be implemented

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			// Log.v("My GPS",
			// "MyGPSLocation : Network is enabled - Inside Network Location Listener");
			timer.cancel();
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerGps);
			locationResult.gotLocation(location);

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {

		// Declaring the Activity as final to be referenced by anonymous thread
		final Activity appActivity;

		GetLastLocation(Activity activity) {
			appActivity = activity;

		}

		// This thread is run when the TimerTask is activated
		// Using runOnUiThread to avoid exception by the Async Task
		// run on main UI-Thread

		@Override
		public void run() {
			// Log.v("My GPS",
			// "MyGPSLocation : TimerTask - Inside Last Location Listener");
			locationManager.removeUpdates(locationListenerGps);
			locationManager.removeUpdates(locationListenerNetwork);

			appActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					// This block need to run in the UI Thread

					Location net_loc = null, gps_loc = null;
					if (gps_enabled)
						gps_loc = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (network_enabled)
						net_loc = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					// if there are both values use the latest one
					if (gps_loc != null && net_loc != null) {
						if (gps_loc.getTime() > net_loc.getTime())
							locationResult.gotLastLocation(gps_loc);
						else
							locationResult.gotLastLocation(net_loc);
						return;
					}

					if (gps_loc != null) {
						// Log.v("My GPS",
						// "MyGPSLocation : Last Known Location from GPS");

						locationResult.gotLastLocation(gps_loc);

						return;
					}
					if (net_loc != null) {
						// Log.v("My GPS",
						// "MyGPSLocation : Last Known Location from Net");

						locationResult.gotLastLocation(net_loc);
						return;
					}
					// Log.v("My GPS",
					// "MyGPSLocation : Location is NULL - All Methods failed");

					locationResult.gotLocation(null);

				}
			});

		}

	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);

		public abstract void gotLastLocation(Location location);

		// The below inner class is defined
		protected abstract void getAddressFromLocation(Context context);
	}
}