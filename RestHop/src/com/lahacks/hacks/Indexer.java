package com.lahacks.hacks;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {
	private IndexWriter indexWriter = null;

	/** Creates a new instance of Indexer */
	public Indexer() {
	}

	public IndexWriter getIndexWriter(boolean create) throws IOException {
		if (indexWriter == null) {

			FileUtils.deleteDirectory(new File("index-directory"));
			Path path = FileSystems.getDefault().getPath("index-directory");
			Directory indexDir = FSDirectory.open(path);
			// deleteDir("index-directory");
			// Directory indexDir = FSDirectory.open(new
			// File("index-directory"));
			IndexWriterConfig config = new IndexWriterConfig(
					new StandardAnalyzer());
			indexWriter = new IndexWriter(indexDir, config);
		}

		return indexWriter;
	}

	public void closeIndexWriter() throws IOException {
		if (indexWriter != null) {
			indexWriter.close();
		}
	}

	public void rebuildIndexes() throws Exception {

		Connection conn = null;

		// create a connection to the database to retrieve Items from MySQL

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String url = "jdbc:mysql://ec2-52-11-181-150.us-west-2.compute.amazonaws.com:3306/hopschema";
		String user = "root";
		String password = "root";

		conn = DriverManager.getConnection(url, user, password);

		IndexWriter writer = getIndexWriter(true);

		Statement stmt = conn.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT GroupID,GroupName,HashTags FROM GroupsDetails");
		String GroupID;
		String GroupName;
		String Hashtags = null;
		while (rs.next()) {
			GroupID = null;
			GroupName = null;
			Hashtags = "";
			Document doc = new Document();
			GroupID = rs.getString("GroupID");
			GroupName = rs.getString("GroupName");
			Hashtags = rs.getString("HashTags");
			doc.add(new StringField("GroupID", GroupID, Field.Store.YES));
			doc.add(new StringField("GroupName", GroupName, Field.Store.YES));
			doc.add(new TextField("Hashtags", Hashtags, Field.Store.YES));
			String fullSearchableText = GroupName + " " + Hashtags;
			doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
			writer.addDocument(doc);
		}
		closeIndexWriter();

		// close the database connection
		try {
			conn.close();
		} catch (SQLException ex) {
			System.out.println(ex);
		}
	}

	public static void main(String args[]) {
		Indexer idx = new Indexer();
		try {
			idx.rebuildIndexes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
