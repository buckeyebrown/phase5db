package com.phasefive.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SQLConnect {

	public SQLConnect() {
		super();
	}

	public ResultSet setUpConnection() {
		String query = "SELECT * FROM EMPLOYEES";
				//Change this
		ResultSet results;
		try {
			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			String dbURL = "jdbc:sqlserver://localhost;databaseName=insurance;integratedSecurity=true;";
			Connection conn = DriverManager.getConnection(dbURL);
			if (conn != null) {
				System.out.println("Connected to SQL server.");
				results = conn.createStatement().executeQuery(query);
				//delete below next
				while (results.next()) {
					System.out.println(rs.getString(4) + " " + rs.getString(6));
				}
			}
		}
		catch (catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
				return results;
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

}
