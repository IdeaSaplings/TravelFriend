package com.isaplings.travelfriend;

import java.util.List;

import org.gmarz.googleplaces.models.Place;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaceAdapter extends ArrayAdapter<Place> {

	Context mycontext;

	public PlaceAdapter(Context context, int listViewResourceId,
			List<Place> places) {
		super(context, listViewResourceId, places);

		mycontext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		/*
		 * View row = convertView;
		 * 
		 * if(row == null){ LayoutInflater inflater = getLayoutInflater(); row =
		 * inflater.inflate(R.layout.view_list, parent, false); }
		 */

		String poiName = new String();


        // address is stored as FeatureName in the Geocoder method
		
		poiName = getItem(position).getName();
		TextView rowAddress = new TextView(mycontext);
		rowAddress.setText(poiName);

		return rowAddress;
	}

}
