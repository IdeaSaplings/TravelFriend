package com.isaplings.travelfriend;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.ActionBar;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ListHospitalsActivity extends Activity {

	// private static final String TAG = "Debug";
	private ActionBar actionBar;
	public GetMyPOITask taskRef;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onCreate(Bundle savedInstanceState) {

		// Log.v("Debug", "MyGPS : Hospital Intent Start");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_places_details);

		// AdHolder update

		AdView adView = (AdView) findViewById(R.id.ad_mob_view);
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("TEST_DEVICE_ID").addKeyword("health").build();
		adView.loadAd(adRequest);

		Bundle bundle = this.getIntent().getExtras();

		Location mLocation = bundle.getParcelable("LOCATION");

		String streetName = mLocation.getExtras().getString("STREETNAME");
		String cityName = mLocation.getExtras().getString("CITYNAME");


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

		actionBar.setIcon(R.drawable.hospital);

		List<String> types = new ArrayList<String>();
		types.add("hospital");
		types.add("dentist");
		types.add("doctor");

		String keyword = null;

		taskRef = ListPOIPlacesActivity.getPOIListTask(
				ListHospitalsActivity.this, this, mLocation, types, keyword);


	}

	@Override
	protected void onStop() {

		super.onStop();
		taskRef.cancel(true);

	}

}
