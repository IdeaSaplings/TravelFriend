package com.isaplings.travelfriend;

import java.util.List;

import org.gmarz.googleplaces.models.Place;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ListPlacesActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_places_details);
		
		Bundle bundle = this.getIntent().getExtras();
		List<Place> placesList = bundle.getParcelableArrayList("Place");
		
		
		ListView listView = (ListView) findViewById(R.id.places_list);
		PlaceAdapter adapter = new PlaceAdapter(this, R.layout.places_lists_item, placesList);
		listView.setAdapter(adapter);
		
				
	}

}
