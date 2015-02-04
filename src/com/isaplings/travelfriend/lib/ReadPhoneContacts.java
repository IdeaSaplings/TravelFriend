package com.isaplings.travelfriend.lib;

import java.util.ArrayList;
import java.util.List;

import com.isaplings.travelfriend.Travel;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ReadPhoneContacts {

	@SuppressWarnings("null")
	public static List<String> getStarredContacts() {

		String phoneNumber = new String();

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		// StringBuffer output = new StringBuffer();
		List<String> buddyList = new ArrayList<String>();

		// use the context here
		ContentResolver contentResolver = Travel.appContext
				.getContentResolver();
		Cursor cursor = contentResolver.query(CONTENT_URI, null, "starred=?",
				new String[] { "1" }, null);
		// Loop for every contact in the phone
		// Log.v("debug", "hello");
		// Log.v("debug", "dummy" + cursor.getCount());
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				// Log.v("debug", "hai");
				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));
				if (hasPhoneNumber > 0) {
					// output.append("\n First Name:" + name);

					// Query and loop for every phone number of the contact
					// Log.v("debug", "before phonecursor");
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						if (phoneNumber != null || !phoneNumber.isEmpty()) {
							// output.append("\n Phone number:" + phoneNumber);
							buddyList.add(name + "\n" + phoneNumber);
							break;
						}
					}
					phoneCursor.close();
					// Query and loop for every email of the contact
					// Log.v("debug", "after phone cursor");

				}
				// output.append("\n");
			}
			// outputText.setText(output);
		}
		if (cursor != null)
			cursor.close();
		return buddyList;
	}

}
