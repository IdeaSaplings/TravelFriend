package com.isaplings.travelfriend;

import android.app.Activity;
import android.os.Bundle;

public class ListPlacesActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_places_details);
		
		this.setResult(RESULT_OK, getIntent());
		
	}

}
