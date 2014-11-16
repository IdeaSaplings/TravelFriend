package com.isaplings.travelfriend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.models.Place.Geometry;
import com.a2plab.googleplaces.GooglePlaces;
import com.a2plab.googleplaces.result.PlacesResult;
import com.a2plab.googleplaces.result.Result.StatusCode;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaceAdapter extends ArrayAdapter<Place> {

	Context mContext;
	List<Place> mPlaces;
	Location mLocation;

	public PlaceAdapter(Context context, int listViewResourceId, List<Place> places, Location location) {
		super(context, listViewResourceId, places);

		mContext = context;
		mLocation = location;
		mPlaces = places;
	}

	class MyViewHolder {
		TextView nameTextView;
		TextView addressTextView;
		TextView distanceTextView;
		TextView ratingTextView;

		MyViewHolder(View view) {
			nameTextView = (TextView) view.findViewById(R.id.place_name_view);
			addressTextView = (TextView) view
					.findViewById(R.id.place_address_view);
			distanceTextView = (TextView) view
					.findViewById(R.id.place_distance_view);
			ratingTextView = (TextView) view
					.findViewById(R.id.place_rating_view);


		}

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		MyViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			row = inflater.inflate(R.layout.places_lists_item, parent, false);
			holder = new MyViewHolder(row);
			row.setTag(holder);
		} else {
			holder = (MyViewHolder) row.getTag();
		}

		String poiName = new String();
		String poiAddress = new String();

		// POI Name is stored as Name in the Place method

		poiName = getItem(position).getName();


		if ((poiName != null )){
			// Concatnate the string to length of the string to
			// display within 2 lines
			
			
			if (poiName.length() > 27 ){
				
				poiName = poiName.substring(0, 24) + "...";
			}
			
		}

		
		holder.nameTextView.setText(poiName);

		poiAddress = getItem(position).getVicinity();
		
		if ((poiAddress == null) || poiAddress.isEmpty()){
			poiAddress = getItem(position).getFormattedAddress();
		}

		
		if ((poiAddress != null )){
			// Concatnate the string to length of the string to
			// display within 2 lines
			
			
			if (poiAddress.length() > 80 ){

				poiAddress = poiAddress.substring(0, 77) + "...";
			}
			
			if (poiAddress.length() <46) {
				
				poiAddress = poiAddress + "\n ";
			}
			
		}
		
		// if (poiAddress.equalsIgnoreCase(null)) poiAddress = "Rating: " +
		// getItem(position).getRating();

		//poiAddress = "Rating: " + "Rating Not available";
		holder.addressTextView.setText(poiAddress);

		// This calculation is done only for kms metrics

		Geometry geometry = (getItem(position).getGeometry());

		// Location pLocation = ((Object) geometry).location();

		// Double dist = (getItem(position).getDistanceTo(mLocation))/1000;

		// BigDecimal bd = new BigDecimal(dist);
		// bd = bd.setScale(1, RoundingMode.HALF_UP);

		// String distanceTo = Double.toString(bd.doubleValue());
		// holder.distanceTextView.setText(distanceTo+ "\n kms");

		holder.distanceTextView.setText(" 99.9" + " km");
		holder.ratingTextView.setText(" *Rating*");

		// Log.v("Debug", "MyGPS : List View getView Complete");

		return row;
	}

}
