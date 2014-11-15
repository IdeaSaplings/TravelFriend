package com.isaplings.travelfriend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.gmarz.googleplaces.models.Place;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaceAdapter extends ArrayAdapter<Place> {

	Context mContext;
	List<Place> mPlaces;
	Location mLocation;

	public PlaceAdapter(Context context, int listViewResourceId,
			List<Place> places, Location location) {
		super(context, listViewResourceId, places);

		mContext = context;
		mLocation = location;
	}

	class MyViewHolder {
		TextView nameTextView;
		TextView addressTextView;
		TextView distanceTextView;

		MyViewHolder(View view) {
			nameTextView = (TextView) view.findViewById(R.id.place_name_view);
			addressTextView = (TextView) view
					.findViewById(R.id.place_address_view);
			distanceTextView = (TextView) view.findViewById(R.id.place_distance_view);

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

		//This calculation is done only for kms metrics
		Double dist = (getItem(position).getDistanceTo(mLocation))/1000;
		
		
		BigDecimal bd = new BigDecimal(dist);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
		
	    
		String distanceTo = Double.toString(bd.doubleValue());
		holder.distanceTextView.setText(distanceTo+ "\n kms");
		
		// Log.v("Debug", "MyGPS : List View getView Complete");

		return row;
	}

}
