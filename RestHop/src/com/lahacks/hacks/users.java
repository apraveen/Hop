package com.lahacks.hacks;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class users {

	public static Logger logger = Logger.getLogger(users.class.getName());

	public static boolean removeUserFromGroup(String groupId, String userId) {
		// call db

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("Select CustomerID from CustomerDetails where FacebookID='"
					+ userId + "'");
			String custId = "";
			if (rs.next()) {
				custId = rs.getString(1);
			}
			String query = " delete from CustInGroup where GroupID = ? and CustomerID=?";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, groupId);
			preparedStmt.setString(2, custId);

			// execute the preparedstatement
			preparedStmt.execute();

			PreparedStatement preparedStmt1 = con
					.prepareStatement("update GroupsDetails set Count=Count - 1 where GroupID=?");
			preparedStmt1.setString(1, groupId);

			// execute the preparedstatement
			preparedStmt1.execute();

		} catch (SQLException ex) {

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}

		return true;
	}

	public static String getAccessToken(String userId) {
		// call db

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String authToken = "";
		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();

			rs = st.executeQuery("select AuthToken from CustomerDetails where FacebookID="
					+ userId);

			if (rs.next()) {
				authToken = rs.getString(1);
			}

		} catch (SQLException ex) {

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}

		return authToken;
	}

	@SuppressWarnings("unused")
	public static JsonObject addUserToGroup(String groupId, String userId) {
		// call db
		JsonObject job = new JsonObject();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();

			rs = st.executeQuery("SELECT CustomerName,CustomerID FROM CustomerDetails WHERE FacebookID="
					+ userId);
			String name = "";
			String id = "";
			if (rs.next()) {
				name = rs.getString(1);
				id = rs.getString(2);
			}
			String query = " insert into CustInGroup values (?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, groupId);
			preparedStmt.setString(2, id);

			// execute the preparedstatement
			preparedStmt.execute();

			PreparedStatement preparedStmt1 = con
					.prepareStatement("update GroupsDetails set Count=Count + 1 where GroupID=?");
			preparedStmt1.setString(1, groupId);

			// execute the preparedstatement
			preparedStmt1.execute();

		} catch (SQLException ex) {
			System.out.println(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}

		return job;
	}

	public static boolean addUser(String userId, String authCode,
			String userName) {
		// call db

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		logger.log(Level.INFO, "User Id:" + userId);
		logger.log(Level.INFO, "Auth code:" + authCode);
		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";
		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("Select count(*) as ct from CustomerDetails where FacebookID='"
					+ userId + "'");
			logger.log(Level.INFO, "After query");

			if (rs.next()) {
				if (rs.getInt(1) > 0) {
					return true;
				}
			}

			String query = ("Insert into CustomerDetails(CustomerName,CustomerLoginName,AuthToken,FacebookID) values (?,?,?,?)");

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, userName);
			preparedStmt.setString(2, userName);
			preparedStmt.setString(3, authCode);
			preparedStmt.setString(4, userId);

			// execute the preparedstatement
			preparedStmt.execute();
			logger.log(Level.INFO, "Query ran");

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		logger.log(Level.INFO, "Finished query");
		return true;
	}

	public static String getUserGroupId(String userId) {
		// call db

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;
		String result = "";

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("Select GroupID from CustInGroup where CustomerId='"
					+ userId + "'");
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}

		return result;
	}

	public static String getGroupInfo(List<Integer> groups) {
		if (groups.size() == 0) {
			return "";
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;
		String result = "";
		JSONObject jsonOBJ = new JSONObject();

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			StringBuilder builder = new StringBuilder("(");
			for (Integer groupId : groups) {
				builder.append(groupId + ",");
			}
			builder.setLength(builder.length() - 1);
			builder.append(")");
			rs = st.executeQuery("Select GroupName,Latitude, Longitude, HashTags,GroupID, Place from GroupsDetails where GroupID in "
					+ builder.toString()
					+ " Order by FIELD (GroupID,"
					+ builder.toString().substring(1, builder.length()));
			JSONArray list = new JSONArray();
			while (rs.next()) {
				String name = rs.getString(1);
				String lat = rs.getString(2);
				String lon = rs.getString(3);
				String hashtags = rs.getString(4);
				String groupId = rs.getString(5);
				String place = rs.getString(6);
				JSONObject groupObject = new JSONObject();
				groupObject.put("name", name);
				groupObject.put("lat", lat);
				groupObject.put("lng", lon);
				groupObject.put("tags", hashtags);
				groupObject.put("groupId", groupId);
				groupObject.put("place", place);
				list.put(groupObject);
			}
			jsonOBJ.put("groups", list);
			result = jsonOBJ.toString();
		} catch (SQLException ex) {
			System.out.println(ex);
		} catch (org.json.JSONException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}

		return result;
	}

	public static List<Long> getUsersOfGroups(int groupId) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection con = null;
		java.sql.Statement st = null;
		ResultSet rs = null;
		List<Long> result = new ArrayList<Long>();
		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			StringBuilder builder = new StringBuilder("(");
			builder.append(")");
			rs = st.executeQuery("Select FacebookID from CustomerDetails where CustomerID in (Select CustomerID from CustInGroup where GroupID = '"
					+ groupId + "')");
			while (rs.next()) {
				result.add(rs.getLong(1));
			}
		} catch (SQLException ex) {
			System.out.println(ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {

			}
		}
		return result;
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		list.add(14);
		list.add(15);
		list.add(16);
		System.out.println(addUser("1234", "abcd", "Facebook Name"));

	}

}
