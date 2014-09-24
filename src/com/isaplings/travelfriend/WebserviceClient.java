package com.isaplings.travelfriend;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class WebserviceClient {

	public static String download(String url)  {
		HttpGet httpGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();

		HttpResponse httpResponse;
		StringBuilder stringBuilder = new StringBuilder();

		try {
			Log.v("My GPS Location",
					"MyGPSLocation - Before making http request");
			httpResponse = httpClient.execute(httpGet);
			Log.v("My GPS Location",
					"MyGPSLocation - After making http request");

			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream inputStream = httpEntity.getContent();

			int x;
			while ((x = inputStream.read()) != -1) {
				stringBuilder.append((char) x);

			}

			Log.v("My GPS Location", "Returns a string data (to json object)");

			return stringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
