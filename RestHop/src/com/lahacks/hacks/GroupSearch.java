package com.lahacks.hacks;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.lahacks.hacks.SearchRegion;
import com.lahacks.hacks.SearchResult;

public class GroupSearch {

	/*
	 * You will probably have to use JDBC to access MySQL data Lucene
	 * IndexSearcher class to lookup Lucene index. Read the corresponding
	 * tutorial to learn about how to use these.
	 * 
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public, so that
	 * they are not exposed to outside of this class.
	 * 
	 * Any new classes that you create should be part of edu.ucla.cs.cs144
	 * package and their source files should be placed at src/edu/ucla/cs/cs144.
	 */

	public static SearchResult[] basicSearch(String query) throws IOException,
			ParseException {

		ArrayList<SearchResult> array = new ArrayList<SearchResult>();

		IndexSearcher searcher = null;
		QueryParser parser = null;

		Path path = FileSystems.getDefault().getPath("index-directory");
		Directory indexDir = FSDirectory.open(path);

		searcher = new IndexSearcher(DirectoryReader.open(indexDir));
		// searcher = new
		// IndexSearcher(DirectoryReader.open(FSDirectory.open(new
		// File("index-directory"))));
		parser = new QueryParser("content", new StandardAnalyzer());
		Query q1 = parser.parse(query);
		TopDocs topDocs = searcher.search(q1, 1000);
		ScoreDoc[] hits = topDocs.scoreDocs;
		int num_return = hits.length;

		for (int i = 0; i < num_return; i++) {

			Document doc = searcher.doc(hits[i].doc);
			System.out.println("$" + doc.get("GroupID"));
			array.add(new SearchResult(doc.get("GroupID"), doc.get("GroupName")));
		}
		// TODO: Your code here!
		SearchResult[] sr = new SearchResult[array.size()];
		sr = array.toArray(sr);
		return sr;
	}

	public static SearchResult[] spatialSearch(String query, SearchRegion region) {
		// TODO: Your code here!

		HashMap<String, Boolean> match = new HashMap<String, Boolean>();
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		double lx = region.getLx();
		double ly = region.getLy();
		double rx = region.getRx();
		double ry = region.getRy();

		try {

			// create a connection to the database to retrieve Items from MySQL

			String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
			String user = "root";
			String password = "root";

			conn = DriverManager.getConnection(url, user, password);
			Statement st = conn.createStatement();
			ResultSet rs = st
					.executeQuery("SELECT GroupID FROM GroupsDetails WHERE MBRCONTAINS( GEOMFROMTEXT(  'POLYGON(("
							+ lx
							+ " "
							+ ly
							+ "),("
							+ lx
							+ " "
							+ ry
							+ "),("
							+ rx
							+ " "
							+ ry
							+ "),("
							+ rx
							+ " "
							+ ly
							+ "),("
							+ lx + " " + ly + ")))' ) , Geo_point ) ");
			// System.out.println("executing spatial loop");
			// int checkspa=0;
			while (rs.next()) {
				// String s=rs.getString("GroupID");
				match.put(rs.getString("GroupID"), true);
				System.out.println(rs.getString("GroupID"));
				// checkspa++;

			}
			// System.out.println("executing start basic search, check :"+checkspa);
			// Basic search

			ArrayList<SearchResult> array = new ArrayList<SearchResult>();

			IndexSearcher searcher = null;
			QueryParser parser = null;
			Path path = FileSystems.getDefault().getPath("index-directory");
			Directory indexDir = FSDirectory.open(path);

			searcher = new IndexSearcher(DirectoryReader.open(indexDir));
			// searcher = new
			// IndexSearcher(DirectoryReader.open(FSDirectory.open(new
			// File("index-directory"))));
			parser = new QueryParser("content", new StandardAnalyzer());
			Query q1 = parser.parse(query);
			TopDocs topDocs = searcher.search(q1, Integer.MAX_VALUE);
			ScoreDoc[] hits = topDocs.scoreDocs;

			for (int i = 0; i < hits.length; i++) {
				Document doc = searcher.doc(hits[i].doc);
				System.out.println("#" + doc.get("GroupID"));
				if (match.containsKey(doc.get("GroupID"))) {
					array.add(new SearchResult(doc.get("GroupID"), doc
							.get("GroupName")));

				}

			}

			// TODO: Your code here!
			SearchResult basic_search[] = new SearchResult[array.size()];
			basic_search = array.toArray(basic_search);

			int num_return;
			num_return = basic_search.length;

			SearchResult[] final_ans = new SearchResult[num_return];

			for (int i = 0; i < num_return; i++) {
				final_ans[i] = new SearchResult();
				final_ans[i].setGroupID(basic_search[i].getGroupID());
				final_ans[i].setGroupName(basic_search[i].getGroupName());

			}

			return final_ans;
		} catch (Exception e) {
			System.out.println("Error is : " + e.getMessage());
		}
		return null;
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		GroupSearch gs = new GroupSearch();
		// String query = "sports";
		// SearchResult[] basicResults = gs.basicSearch(query);
		// System.out.println("Basic Seacrh Query: " + query);
		// System.out.println("Received " + basicResults.length + " results");
		// for (SearchResult result : basicResults) {
		// System.out.println(result.getGroupID() + ": "
		// + result.getGroupName());
		// }

		Indexer ind = new Indexer();
		ind.rebuildIndexes();

		basicSearch("bar");

		SearchRegion region = new SearchRegion(34.068922, -118.448044, 100);
		SearchResult[] spatialResults = gs.spatialSearch("Bar", region);
		System.out.println("Spatial Seacrh");
		System.out.println("Received " + spatialResults.length + " results");
		for (SearchResult result : spatialResults) {
			System.out.println(result.getGroupID() + ": "
					+ result.getGroupName());
		}

		// http://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:8080/Hop/findGroup?UserId=812703455488489&Query=Bar&Lat=34.068922&Lon=-118.448044&Radius=100

	}

}
