package com.lahacks;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lahacks.hacks.users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by praveen on 4/4/15.
 */
@SuppressWarnings("serial")
public class NewUserCreationServlet extends HttpServlet implements Servlet {
	public NewUserCreationServlet() {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String facebookUserId = request.getParameter("UserId");
		String authCode = request.getParameter("AuthCode");
		String name = getFBName(facebookUserId, authCode);

		users.addUser(facebookUserId, authCode, name);

		ServletOutputStream outStream = response.getOutputStream();
		outStream.write("Success".getBytes());
		outStream.flush();
	}

	public static String getFBName(String UserID, String accessToken) {
		try {
			String url = "https://graph.facebook.com/v2.3/" + UserID
					+ "?access_token=" + accessToken;

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
			JsonObject object = new JsonParser().parse(response.toString())
					.getAsJsonObject();
			String name = "";
			if (object.has("firt_name")) {
				name = object.get("firt_name").toString();
			} else {
				name = "John Doe";
			}
			return name;
		} catch (Exception e) {
			e.printStackTrace();
			return "John Doe";
		}
	}
}
