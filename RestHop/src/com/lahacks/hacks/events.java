package com.lahacks.hacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

class event {

	String name, lat, lng, venue;

}

public class events {

	public static JSONObject getEvents(String query, String lat, String lng,
			String range) {
		String events = "";

		JSONObject jsonOBJ = new JSONObject();
		try {
			String url = "https://seatgeek-seatgeekcom.p.mashape.com/events?sort=score.desc&q="
					+ query
					+ "&lat="
					+ lat
					+ "&lon="
					+ lng
					+ "&range="
					+ range
					+ "mi";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("X-Mashape-Key",
					"HEK4zY75DPmshWUi8Q2LG66eyycjp1bXc8jjsnNE37wfs5G1ec");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			events = events + response.toString();

			JsonArray ResultList = new JsonParser().parse(events.toString())
					.getAsJsonObject().getAsJsonArray("events");

			ArrayList<event> resultList = new ArrayList<event>();

			JSONArray list = new JSONArray();

			for (int i = 0; i < ResultList.size(); i++) {

				event evt = new event();
				evt.name = ResultList.get(i).getAsJsonObject().get("title")
						.toString();

				evt.venue = ResultList.get(i).getAsJsonObject()
						.getAsJsonObject("venue").get("name").toString();

				evt.lat = ResultList.get(i).getAsJsonObject()
						.getAsJsonObject("venue").getAsJsonObject("location")
						.get("lat").getAsDouble()
						+ "";
				evt.lng = ResultList.get(i).getAsJsonObject()
						.getAsJsonObject("venue").getAsJsonObject("location")
						.get("lon").getAsDouble()
						+ "";

				resultList.add(evt);

				JSONObject event = new JSONObject();
				event.put("name", evt.name);
				event.put("venue", evt.venue);
				event.put("lat", evt.lat);
				event.put("lng", evt.lng);
				list.put(event);
			}
			jsonOBJ.put("events", list);
			System.out.println("Done");
		} catch (Exception E) {

		}
		return jsonOBJ;
	}

	public static JSONObject getPopularEvents(String lat, String lng,
			String range) {
		String events = "";

		JSONObject jsonOBJ = new JSONObject();
		try {
			String url = "https://seatgeek-seatgeekcom.p.mashape.com/events?sort=score.desc&lat="
					+ lat + "&lon=" + lng + "&range=" + range + "mi";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("X-Mashape-Key",
					"HEK4zY75DPmshWUi8Q2LG66eyycjp1bXc8jjsnNE37wfs5G1ec");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			events = events + response.toString();

			JsonArray ResultList = new JsonParser().parse(events.toString())
					.getAsJsonObject().getAsJsonArray("events");

			ArrayList<event> resultList = new ArrayList<event>();

			JSONArray list = new JSONArray();

			for (int i = 0; i < ResultList.size(); i++) {

				event evt = new event();
				evt.name = ResultList.get(i).getAsJsonObject().get("title")
						.toString();

				evt.lat = ResultList.get(i).getAsJsonObject()
						.getAsJsonObject("venue").getAsJsonObject("location")
						.get("lat").getAsDouble()
						+ "";
				evt.lng = ResultList.get(i).getAsJsonObject()
						.getAsJsonObject("venue").getAsJsonObject("location")
						.get("lon").getAsDouble()
						+ "";

				resultList.add(evt);

				JSONObject event = new JSONObject();
				event.put("name", evt.name);
				event.put("lat", evt.lat);
				event.put("lng", evt.lng);
				list.put(event);
			}
			jsonOBJ.put("events", list);
			System.out.println("Done");
		} catch (Exception E) {

		}
		return jsonOBJ;
	}

	public static String getFBlikes(String UserID, String accessToken) {

		String likes = "";
		try {
			String url = "https://graph.facebook.com/v2.3/" + UserID
					+ "/likes?pretty=0&limit=1000&access_token=" + accessToken;

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			likes = likes + response.toString();
		} catch (Exception E) {

		}
		return likes;

	}

	public static void main(String[] args) throws IOException {

		getEvents("baseball", "34.068921", "-118.445181", "100");
		// getPopularEvents("34.068921", "-118.445181", "1");
	}
}
