package com.phasefive.backend;

import java.sql.*;
import com.microsoft.sqlserver.jdbc.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


public class SQLConnect {

	public SQLConnect() {
		super();
	}

	public static ResultSet setUpConnection() {
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		// Create a variable for the connection string.

		String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
				"databaseName=insurance;integratedSecurity=true;";

		String query = "SELECT * FROM EMPLOYEES";
				//Change this
		try {
			//Establish the connection
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(connectionUrl);
			//DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			//String dbURL = "jdbc:sqlserver://localhost;databaseName=insurance;integratedSecurity=true;";
			//Connection conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected to SQL server.");
				rs = conn.createStatement().executeQuery(query);
				//delete below next
				while (rs.next()) {
					System.out.println(rs.getString(4) + " " + rs.getString(6));
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//if (rs != null) try { rs.close(); } catch(Exception e) {}
			if (conn != null) try { conn.close(); } catch(Exception e) {}
			return rs;
		}
	}

}
