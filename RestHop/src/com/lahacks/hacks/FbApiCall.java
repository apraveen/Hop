package com.lahacks.hacks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.codehaus.jettison.json.JSONArray;

/**
 * Created by praveen on 4/4/15.
 */
@SuppressWarnings("unused")
public class FbApiCall {

	public static Triple<Long, Set<String>, Map<String, Integer>> triplet;

	public static void getFBlikes(String UserID, String accessToken) {

		String likes = "";
		try {

			String url = "https://graph.facebook.com/v2.3/" + UserID
					+ "/likes?pretty=0&limit=1000&access_token=" + accessToken;

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

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
			String events = "";
			events = events + response.toString();

			JsonArray ResultList = new JsonParser().parse(events.toString())
					.getAsJsonObject().getAsJsonArray("data");
			Map<String, Integer> userCategoryLikes = new HashMap<String, Integer>();
			Set<String> userLikes = new HashSet<String>();

			for (int i = 0; i < ResultList.size(); i++) {

				String res = ResultList.get(i).getAsJsonObject()
						.get("category").toString();
				if (userCategoryLikes.containsKey(res)) {
					userCategoryLikes.put(res, userCategoryLikes.get(res) + 1);
				} else {
					userCategoryLikes.put(res, 1);
				}

				res = ResultList.get(i).getAsJsonObject().get("name")
						.toString();
				userLikes.add(res);
			}
			triplet = new Triple<Long, Set<String>, Map<String, Integer>>(
					Long.parseLong(UserID), userLikes, userCategoryLikes);

		} catch (Exception E) {
			E.printStackTrace();

		}
	}

	public static Set<String> getUserLikes(String userId, String accessToken) {
		if (triplet != null && triplet.getLeft().equals(Long.parseLong(userId))) {
			return triplet.getMiddle();
		} else {
			getFBlikes(userId, accessToken);
			return triplet.getMiddle();
		}
	}

	public static Map<String, Integer> getUserCategoryLikes(String userId,
			String accessToken) {
		if (triplet != null && triplet.getLeft().equals(Long.parseLong(userId))) {
			return triplet.getRight();
		} else {
			getFBlikes(userId, accessToken);
			return triplet.getRight();
		}
	}

}
