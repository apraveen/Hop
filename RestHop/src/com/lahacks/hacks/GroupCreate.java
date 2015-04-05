package com.lahacks.hacks;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.*;

/**
 * 
 * @author puneet
 */
public class GroupCreate {

	public static String GroupID;
	public String GroupName;
	public String Latitude;
	public String Longitude;
	public String Geo_point;
	public String Count;
	public String HashTags;
	public String CustomerName;
	public String CustomerID;

	@SuppressWarnings("unused")
	public void insertGD(String GroupName, String Latitude, String Longitude,
			String Count, String HashTags, String FacebookId, String Place)
			throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Connection conn = null;

		// create a connection to the database to retrieve Items from MySQL
		java.sql.Statement st = null;
		ResultSet rs = null;

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		conn = DriverManager.getConnection(url, user, password);
		st = conn.createStatement();

		Statement stmt = conn.createStatement();
		PreparedStatement pstmt = conn
				.prepareStatement("INSERT INTO GroupsDetails(GroupName,Latitude,Longitude,Place,Count,HashTags,Geo_point) VALUES(?, ?, ?, ?, ?, ?, PointFromText(CONCAT('POINT(',?,' ',?,')')))");
		// pstmt.setString(1, GroupID);
		pstmt.setString(1, GroupName);
		pstmt.setString(2, Latitude);
		pstmt.setString(3, Longitude);
		pstmt.setString(4, Place);
		pstmt.setString(5, Count);
		pstmt.setString(6, HashTags);
		pstmt.setString(7, Latitude);
		pstmt.setString(8, Longitude);
		int i = pstmt.executeUpdate();

		rs = stmt.executeQuery("Select LAST_INSERT_ID() as GroupID");
		while (rs.next()) {
			GroupID = rs.getString("GroupID");
		}

	}

	@SuppressWarnings("unused")
	public void insertCIG(String FacebookID) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Connection conn = null;
		try {
			// create a connection to the database to retrieve Items from MySQL
			java.sql.Statement st = null;
			java.sql.Statement st2 = null;
			String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
			String user = "root";
			String password = "root";
			String CustomerID = null;
			ResultSet rs = null;
			conn = DriverManager.getConnection(url, user, password);
			st2 = conn.createStatement();
			// String CustName=

			rs = st2.executeQuery("Select CustomerID from CustomerDetails where FacebookID='"
					+ FacebookID + "'");
			while (rs.next()) {
				CustomerID = rs.getString("CustomerID");
			}

			Statement stmt = conn.createStatement();
			PreparedStatement pstmt = conn
					.prepareStatement("INSERT INTO CustInGroup VALUES(?, ?)");
			pstmt.setString(1, GroupID);
			pstmt.setString(2, CustomerID);
			int i = pstmt.executeUpdate();

			Indexer ind = new Indexer();
			ind.rebuildIndexes();

		} catch (Exception E) {
			System.out.println(E);
		}
	}

	public static void main(String[] args) throws SQLException {

		GroupCreate gc = new GroupCreate();
		gc.insertGD("Group", "32.567", "-117.98","1", "#tr # get", "101","LA");

	}

}
