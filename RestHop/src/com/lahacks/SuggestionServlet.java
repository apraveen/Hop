package com.lahacks;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.lahacks.hacks.events;

public class SuggestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SuggestionServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			String query = request.getParameter("query");
			String lat = request.getParameter("lat");
			String lng = request.getParameter("lng");
			String range = request.getParameter("range");

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();

			JSONObject obj = events.getEvents(query, lat, lng, range);

			out.print(obj);
			out.flush();

		} catch (Exception e) {

			System.out.println(e);

		}
	}
}
