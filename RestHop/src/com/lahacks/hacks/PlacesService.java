package com.lahacks.hacks;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @author prithviraj
 */

class Place {

	String reference, name, icon, formatted_address, formatted_phone_number,
			lat, lng;

}

public class PlacesService {

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

	private static final String TYPE_SEARCH = "/search";

	private static final String OUT_JSON = "/json";

	// KEY!
	private static final String API_KEY = "AIzaSyAr7jH6rsYSFW6Sk_8GB_zu-qwK_qXEQjA";

	public static ArrayList<Place> search(String keyword, double lat,
			double lng, int radius) {
		ArrayList<Place> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE);
			sb.append(TYPE_SEARCH);
			sb.append(OUT_JSON);
			sb.append("?sensor=false");
			sb.append("&key=" + API_KEY);
			sb.append("&keyword=" + URLEncoder.encode(keyword, "utf8"));
			sb.append("&location=" + String.valueOf(lat) + ","
					+ String.valueOf(lng));
			sb.append("&radius=" + String.valueOf(radius));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			return resultList;
		} catch (IOException e) {
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		JsonArray ResultList = new JsonParser().parse(jsonResults.toString())
				.getAsJsonObject().getAsJsonArray("results");

		// Extract the Place descriptions from the results

		resultList = new ArrayList<Place>();

		for (int i = 0; i < ResultList.size(); i++) {
			Place place = new Place();
			place.name = ResultList.get(i).getAsJsonObject().get("name")
					.toString();
			place.lat = ResultList.get(i).getAsJsonObject()
					.getAsJsonObject("geometry").getAsJsonObject("location")
					.get("lat").toString();
			place.lng = ResultList.get(i).getAsJsonObject()
					.getAsJsonObject("geometry").getAsJsonObject("location")
					.get("lng").toString();

			resultList.add(place);
		}

		return resultList;
	}

	public static void main(String[] args) {

		ArrayList<Place> places = PlacesService.search("bar", 34.068921,
				-118.445181, 50000);
		for (int i = 0; i < places.size(); i++) {
			System.out.println(places.get(i).name);
			System.out.println(places.get(i).lat);
			System.out.println(places.get(i).lng);
		}
	}
}