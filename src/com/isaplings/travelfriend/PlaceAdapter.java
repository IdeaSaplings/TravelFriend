package com.isaplings.travelfriend;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import com.a2plab.googleplaces.models.Place;
import com.a2plab.googleplaces.models.Place.Geometry;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
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
		RatingBar ratingTextView;

		MyViewHolder(View view) {
			nameTextView = (TextView) view.findViewById(R.id.place_name_view);
			addressTextView = (TextView) view
					.findViewById(R.id.place_address_view);
			distanceTextView = (TextView) view
					.findViewById(R.id.place_distance_view);
			ratingTextView = (RatingBar) view.findViewById(R.id.place_rating_view);


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
			
			if (poiAddress.length() <43) {
				
				poiAddress = poiAddress + "\n ";
			}
			
		}
		
		// if (poiAddress.equalsIgnoreCase(null)) poiAddress = "Rating: " +
		// getItem(position).getRating();

		//poiAddress = "Rating: " + "Rating Not available";
		holder.addressTextView.setText(poiAddress);

		// This calculation is done only for kms metrics

		Geometry geometry = (getItem(position).getGeometry());
		
		final float[] dist = new float[3];
		
		Location.distanceBetween(mLocation.getLatitude(), mLocation.getLongitude(), geometry.location.lat, geometry.location.lng, dist);
		
		
		DecimalFormat df = new DecimalFormat("###.#");

		
		holder.distanceTextView.setText(" "+ df.format(dist[0]/1000) + " km");
		
		Double poiRating;
		
		if (getItem(position).getRating() != null ){
			poiRating = getItem(position).getRating();
		} else poiRating = (Double) 0.0;
		
		BigDecimal bd = new BigDecimal(poiRating);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		
		
		holder.ratingTextView.setStepSize((float) 0.25);
		holder.ratingTextView.setRating(bd.floatValue());

		// Log.v("Debug", "MyGPS : List View getView Complete");
		
		Log.v("Debug" , "MYGPS : Place ID : " +getItem(position).getId());
		Log.v("Debug" , "MYGPS : Rating : " +getItem(position).getRating());
		return row;
	}

}
