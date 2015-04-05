package com.lahacks;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lahacks.hacks.GroupCreate;

@SuppressWarnings("serial")
public class GroupCreateServlet extends HttpServlet implements Servlet {

	public GroupCreateServlet() {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			String GroupName = (String) request.getParameter("GroupName");
			String Latitude = (String) request.getParameter("Latitude");
			String Longitude = (String) request.getParameter("Longitude");
			String Place = (String) request.getParameter("PlaceName");
			String Count = (String) request.getParameter("Count");
			String HashTags = (String) request.getParameter("HashTags");
			String FacebookId = (String) request.getParameter("FacebookId");
			GroupCreate gC = new GroupCreate();
			gC.insertGD(GroupName, Latitude, Longitude, Count, HashTags,
					FacebookId, Place);
			gC.insertCIG(FacebookId);
			request.setAttribute("success", 1);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
