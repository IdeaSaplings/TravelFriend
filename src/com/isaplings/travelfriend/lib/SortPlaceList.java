package com.isaplings.travelfriend.lib;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.location.Location;

import com.a2plab.googleplaces.models.Place;

public class SortPlaceList {

	public static List<Place> sortbyDistance(List<Place> placesList,
			Location location) {

		final Location mLocation = location;

		Collections.sort(placesList, new Comparator<Place>() {

			public int compare(Place o1, Place o2) {

				// Sorts by haversine distance 'distanceBetween' property

				final float[] dist = new float[3];

				Location.distanceBetween(mLocation.getLatitude(),
						mLocation.getLongitude(),
						o1.getGeometry().location.lat,
						o1.getGeometry().location.lng, dist);

				float distBetweenPlace1 = dist[0];

				Location.distanceBetween(mLocation.getLatitude(),
						mLocation.getLongitude(),
						o2.getGeometry().location.lat,
						o2.getGeometry().location.lng, dist);

				float distBetweenPlace2 = dist[0];

				return distBetweenPlace1 > distBetweenPlace1 ? 1
						: (distBetweenPlace1 < distBetweenPlace2 ? -1 : 0);

			}

		});

		return placesList;

	}

	public static List<Place> sortbyRating(List<Place> placesList) {

		Collections.sort(placesList, new Comparator<Place>() {

			public int compare(Place o1, Place o2) {
				Double ratingPlace1;
				Double ratingPlace2;

				if (o1.getRating() == null) {
					ratingPlace1 = 0.0;
				} else
					ratingPlace1 = o1.getRating();

				if (o2.getRating() == null) {
					ratingPlace2 = 0.0;
				} else
					ratingPlace2 = o2.getRating();

				return ratingPlace1 < ratingPlace2 ? 1 : (ratingPlace1 > ratingPlace2 ? -1 : 0);

			}

		});

		return placesList;

	}
}
