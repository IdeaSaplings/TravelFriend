package com.isaplings.travelfriend;

import java.util.List;

import org.gmarz.googleplaces.models.Place;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ListPlacesActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {

		Log.v("Debug", "MyGPS : New Intent Start");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_places_details);

		// if you want to lock screen for always Portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Bundle bundle = this.getIntent().getExtras();
		List<Place> placesList = bundle.getParcelableArrayList("Place");

		ListView listView = (ListView) findViewById(R.id.places_list);
		PlaceAdapter adapter = new PlaceAdapter(this,
				R.layout.places_lists_item, placesList);
		listView.setAdapter(adapter);

		Log.v("Debug", "MyGPS : New Intent Complete");

	}

}
