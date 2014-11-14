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

	class MyViewHolder {
		TextView nameTextView;
		TextView addressTextView;

		MyViewHolder(View view) {
			nameTextView = (TextView) view.findViewById(R.id.place_name_view);
			addressTextView = (TextView) view
					.findViewById(R.id.place_address_view);

		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		 View row = convertView;
		 MyViewHolder holder=null;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			row = inflater.inflate(R.layout.places_lists_item, parent, false);
			holder = new MyViewHolder(row);
			row.setTag(holder);
		}
		else {
			holder = (MyViewHolder) row.getTag();
		}

		String poiName = new String();
		String poiAddress = new String();

		// POI Name is stored as Name in the Place method

		poiName = getItem(position).getName();

		holder.nameTextView.setText(poiName);

		poiAddress = getItem(position).getAddress() + " - Rating: "
				+ getItem(position).getRating();
		// if (poiAddress.equalsIgnoreCase(null)) poiAddress = "Rating: " +
		// getItem(position).getRating();

		poiAddress = "Rating: " + getItem(position).getRating();
		holder.addressTextView.setText(poiAddress);

		// Log.v("Debug", "MyGPS : List View getView Complete");

		return row;
	}

}
