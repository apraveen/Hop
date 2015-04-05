package com.lahacks;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lahacks.hacks.users;

public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteUserServlet() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {

			String groupId = request.getParameter("groupId");
			String userId = request.getParameter("userId");

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();

			boolean res = users.removeUserFromGroup(groupId, userId);

			out.print(res);
			out.flush();

		} catch (Exception e) {

			System.out.println(e);

		}
	}
}
