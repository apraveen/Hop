package com.lahacks;

import com.lahacks.hacks.Pair;
import com.lahacks.hacks.TopGroupsFinder;
import com.lahacks.hacks.users;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen on 4/4/15.
 */
@SuppressWarnings("serial")
public class FindGroupServlet extends HttpServlet implements Servlet {
	public FindGroupServlet() {

	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String userId = request.getParameter("UserId");
		String query = request.getParameter("Query");
		String lat = request.getParameter("Lat");
		String lon = request.getParameter("Lon");
		String radius = request.getParameter("Radius");
		String result = "";
		List<Integer> groupIds = TopGroupsFinder.getGroupIds(
				Double.parseDouble(lat), Double.parseDouble(lon),
				Double.parseDouble(radius), query);
		try {
			List<Pair<Integer, Double>> orderedGroups = TopGroupsFinder
					.calculateAllGroupScore(groupIds, Long.parseLong(userId));
			List<Integer> groups = new ArrayList<Integer>();
			for (Pair<Integer, Double> pair : orderedGroups) {
				groups.add(pair.getFirst());
			}
			result = users.getGroupInfo(groups);

		} catch (Exception e) {
			e.printStackTrace();
		}

		ServletOutputStream outStream = response.getOutputStream();
		outStream.write(result.getBytes());
		outStream.flush();
	}
}
