package com.isaplings.travelfriend;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CityAddressAdapter extends ArrayAdapter<Address> {

	Context mycontext;

	public CityAddressAdapter(Context context, int listViewResourceId,
			List<Address> addresses) {
		super(context, listViewResourceId, addresses);

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

		String addressline = new String();


        // address is stored as FeatureName in the Geocoder method
		
		addressline = getItem(position).getFeatureName();
		TextView rowAddress = new TextView(mycontext);
		rowAddress.setText(addressline);

		return rowAddress;
	}

}
