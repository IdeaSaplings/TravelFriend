package com.isaplings.travelfriend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;



public class ListSOSActivity extends Activity {
	
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

	
	//places.add(new PlaceRecord("",""));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sos_places);
        
        Log.v("Debug" , " MYGPS : List Places Activity loaded");
        
        getActionBar().setTitle("Outer Ring Road");
		getActionBar().setSubtitle("Bellandur, Bangalore");
 
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
 
        // preparing list data
        prepareListData();
 
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
 
        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.expandGroup(0);
 
        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {
 
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
            	return false;
            }
        });
 
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {
 
            @Override
            public void onGroupExpand(int groupPosition) {
            	int len = listAdapter.getGroupCount();

            	for (int i = 0; i < len; i++) {
            	    if (i != groupPosition) {
            	        expListView.collapseGroup(i);
            	    }
            	}
               // Toast.makeText(getApplicationContext(),
               //         listDataHeader.get(groupPosition) + " Expanded",
                //        Toast.LENGTH_SHORT).show();
            }
        });
 
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
 
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(),
                    //    listDataHeader.get(groupPosition) + " Collapsed",
                    //    Toast.LENGTH_SHORT).show();
 
            }
        });
 
        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {
 
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
	}
        
        /*
         * Preparing the list data
         */
        private void prepareListData() {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();
     
            // Adding header data
            listDataHeader.add("Buddies");
            listDataHeader.add("Police");
            listDataHeader.add("Ambulance");
            listDataHeader.add("Toll Booth");
            listDataHeader.add("Helpline");
     
            // Adding child data
            List<String> buddies = new ArrayList<String>();
            buddies.add("Elango +91 9880649789");
            buddies.add("Navine +91 80 42120570");
            buddies.add("Senthilnathan +91 9686033557");
            buddies.add("Ramesh +91 9916922424");
            
     
            List<String> police = new ArrayList<String>();
            police.add("100");
            police.add("102");
            police.add("104");
            
            List<String> tollfree = new ArrayList<String>();
            tollfree.add("08025746");
            tollfree.add("04412345");
            tollfree.add("02289745");
     
            List<String> ambulance = new ArrayList<String>();
            ambulance.add("1088");
            ambulance.add("108");
                    
            List<String> helpline = new ArrayList<String>();
            helpline.add("18004087346");
            helpline.add("18008364565");
     
            listDataChild.put(listDataHeader.get(0), buddies); // Header, Child data
            listDataChild.put(listDataHeader.get(1), police);
            listDataChild.put(listDataHeader.get(2), ambulance);
            listDataChild.put(listDataHeader.get(3), tollfree);
            listDataChild.put(listDataHeader.get(4), helpline);
        }

}

