package com.lahacks;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lahacks.hacks.users;

import java.io.IOException;

/**
 * Created by praveen on 4/4/15.
 */
@SuppressWarnings("serial")
public class UpdateLocation extends HttpServlet implements Servlet {
	public UpdateLocation() {

	}

	@SuppressWarnings("unused")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("UserId");
		String lat = request.getParameter("Lat");
		String lon = request.getParameter("Long");
		String group = users.getUserGroupId(userId);
		// if (group != "") {
		// if (checkIfUserOutsideGroup(userId, lat, lon)) {
		// users.removeUserFromGroup(group, userId);
		// }
		// }
		ServletOutputStream outStream = response.getOutputStream();
		outStream.write("Success".getBytes());
		outStream.flush();
	}
}
