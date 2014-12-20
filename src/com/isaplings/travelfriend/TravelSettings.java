package com.isaplings.travelfriend;

import com.isaplings.travelfriend.AlertDialogRadio.AlertPositiveListener;
import com.isaplings.travelfriend.lib.QuickFeedback;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
//import android.widget.Toast;

public class TravelSettings extends Activity implements AlertPositiveListener {

	Button radio;
	public int position = 0;
	int appRating;
	static int appRadius = 50;

	public static Context appContext;
	public static Activity appActivity;
	private ActionBar actionBar;

	SharedPreferences pref;

	int step = 1;
	int min = 50;
	int radVal;
	int storedVal;
	String storedUnit;

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		appContext = this.getApplicationContext();
		appActivity = TravelSettings.this;

		setContentView(R.layout.settings_layout);

		pref = Travel.appContext.getSharedPreferences("TravelFriendPref", 0);

		storedUnit = pref.getString("DistUnit", "km");

		if (storedUnit.endsWith("mi")) {
			position = 1;
		}

		TextView distanceUnitValue = (TextView) findViewById(R.id.distance_unit_value);

		/** Setting the selected android version in the textview */
		distanceUnitValue.setText(storedUnit);

		storedVal = pref.getInt("MaxRadius", 50);
		radVal = storedVal - min;

		TextView searchRadiusValue = (TextView) findViewById(R.id.search_radius_value);
		searchRadiusValue.setText(Integer.toString(storedVal) + " km/mi");

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.ab_title);
		actionBar.setIcon(R.drawable.ic_action_settings);

		TextView title = (TextView) findViewById(android.R.id.text1);
		title.setText("Settings");

		// rl1, rl2, rl3, rl4 are relativeLayouts

		// rl1 - Distance Unit Layout
		RelativeLayout rl1 = (RelativeLayout) findViewById(R.id.app_settings_distance_unit);
		rl1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				android.app.FragmentManager manager = getFragmentManager();

				/** Instantiating the DialogFragment class */
				AlertDialogRadio alert = new AlertDialogRadio();

				/** Creating a bundle object to store the selected item's index */
				Bundle b = new Bundle();

				/** Storing the selected item's index in the bundle object */
				b.putInt("position", position);

				/** Setting the bundle object to the dialog fragment object */
				alert.setArguments(b);

				/**
				 * Creating the dialog fragment object, which will in turn open
				 * the alert dialog window
				 */
				alert.show(manager, "alert_dialog_radio");
			}
		});

		// rl2 - Search Radius Layout
		RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.app_settings_distance_radius);
		rl2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowDialog();
			}
		});

		// rl3 - share layout
		RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.share_settings);
		rl3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT,
						"Hey!! Checkout this app !!");
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});

		// rl4 - feedback layout
		RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.feedback_settings);
		rl4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ShowDialogFeed();
			}
		});

	}

	// this method is used in ShareLocation - The message embedded in
	// shareLocation
	public void sendMessage(View view) {
		// Do something in response to button

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey!! Checkout this app !!");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}

	@Override
	public void onPositiveClick(int position) {
		// TODO Auto-generated method stub

		this.position = position;

		/** Getting the reference of the textview from the main layout */
		TextView tv = (TextView) findViewById(R.id.distance_unit_value);

		/** Setting the selected android version in the textview */
		tv.setText(DistanceUnit.code[this.position]);

		Editor editor = pref.edit();
		editor.putString("DistUnit", DistanceUnit.code[this.position]);
		editor.commit();

		//Toast.makeText(getApplication(), pref.getString("DistUnit", null),
			//	Toast.LENGTH_LONG).show();

	}

	public void ShowDialog() {

		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View Viewlayout = inflater.inflate(R.layout.activity_dialog,
				(ViewGroup) findViewById(R.id.layout_dialog));
		popDialog.setTitle("Please Select Search Radius 50-100 km/mi ");
		popDialog.setView(Viewlayout);
		// seekBar1
		SeekBar seek1 = (SeekBar) Viewlayout.findViewById(R.id.seekBar1);
		seek1.setMax(50);
		
		storedVal = pref.getInt("MaxRadius", 50);
		radVal = storedVal - min;

		seek1.setProgress(radVal);

		TextView tv1 = (TextView) Viewlayout.findViewById(R.id.distance);

		/** Setting the selected android version in the textview */
		tv1.setText(Integer.toString(storedVal) + "km/mi");

		seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Do something here with new value

				int value = min + (progress * step);

				appRadius = value;

				TextView tv1 = (TextView) Viewlayout
						.findViewById(R.id.distance);

				/** Setting the selected android version in the textview */
				tv1.setText(Integer.toString(value) + "km/mi");
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});
		// Button OK
		popDialog.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = pref.edit();
						editor.putInt("MaxRadius", appRadius);
						editor.commit();

						@SuppressWarnings("unused")
						String maxRad = Integer.toString(pref.getInt(
								"MaxRadius", 50));

						//Toast.makeText(getApplication(), maxRad,
							//	Toast.LENGTH_LONG).show();

						TextView tv = (TextView) findViewById(R.id.search_radius_value);

						/** Setting the selected android version in the textview */
						tv.setText(Integer.toString(appRadius) + " km/mi");

						dialog.dismiss();
					}

				});

		popDialog.create();
		popDialog.show();
	}

	public void ShowDialogFeed() {
		final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
		final LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View Viewlayout = inflater.inflate(R.layout.feedback_dialog,
				(ViewGroup) findViewById(R.id.layout_dialog));
		popDialog.setView(Viewlayout);
		// seekBar1
		SeekBar seek1 = (SeekBar) Viewlayout.findViewById(R.id.seekBarFeed1);
		appRating = 10;
		seek1.setMax(10);
		seek1.setProgress(appRating);
		seek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// Do something here with new value
				TextView tv1 = (TextView) Viewlayout.findViewById(R.id.rating);
				/** Setting the selected android version in the textview */
				tv1.setText(Integer.toString(progress));

				appRating = progress;
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});

		// Button Cancel
		popDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});

		// Button Send
		popDialog.setPositiveButton("Send",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						EditText feedbackInfo = (EditText) Viewlayout
								.findViewById(R.id.feedback_info);
						String feedbackMessage = feedbackInfo.getText()
								+ "\n\n" + "\n Rating : " + appRating;
						QuickFeedback.sendMessage(feedbackMessage);
						dialog.dismiss();
					}

				});

		popDialog.create();
		popDialog.show();
	}

}
