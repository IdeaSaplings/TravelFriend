package com.isaplings.travelfriend;

import java.util.List;

import org.gmarz.googleplaces.models.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaceAdapter extends ArrayAdapter<Place> {

	Context mContext;
	List<Place> mPlaces;

	public PlaceAdapter(Context context, int listViewResourceId,
			List<Place> places) {
		super(context, listViewResourceId, places);

		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		// View row = view;

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			view = inflater.inflate(R.layout.places_lists_item, parent, false);
		}

		String poiName = new String();
		String poiAddress = new String();

		// POI Name is stored as Name in the Place method

		poiName = getItem(position).getName();
		TextView nameTextView = (TextView) view
				.findViewById(R.id.place_name_view);
		nameTextView.setText(poiName);

		poiAddress = getItem(position).getAddress() + " - Rating: " + getItem(position).getRating();
		//if (poiAddress.equalsIgnoreCase(null)) poiAddress = "Rating: " + getItem(position).getRating();
		TextView addressTextView = (TextView) view
				.findViewById(R.id.place_address_view);
		addressTextView.setText(poiAddress);

		//Log.v("Debug", "MyGPS : List View getView Complete");

		
		return view;
	}

}
