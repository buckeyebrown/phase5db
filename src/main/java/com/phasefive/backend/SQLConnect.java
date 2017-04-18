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

		String query = getQuery(ObtainQueryCode.getQueryCode());
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
				System.out.println("Got the result set.");
				//delete below next
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//if (rs != null) try { rs.close(); } catch(Exception e) {}
			//if (conn != null) try { conn.close(); } catch(Exception e) {}
			return rs;
		}
	}

	private static String getQuery(int queryCode){
		String returnString = "";
		switch (queryCode){
			case 1:
				returnString = "SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"UNION\n" +
						"SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')";
				break;
			case 2:
				returnString = "SELECT fname, lname FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"INTERSECT\n" +
						"SELECT fname, lname FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins')";
				break;
			case 3:
				returnString = "SELECT Account_ID FROM ACCOUNTS\n" +
						"EXCEPT\n" +
						"(SELECT ssn FROM CLIENT WHERE ssn IN (SELECT client_ssn FROM DEPENDENTS)\n" +
						"INTERSECT\n" +
						"SELECT employee_ssn FROM CLIENT WHERE employee_ssn IN (SELECT emp_ssn FROM EMPLOYEES WHERE lname = 'higgins'))";
				break;
			case 4:
				returnString = "SELECT vehicle_model FROM VEHICLES WHERE NOT EXISTS (SELECT Report_Number FROM ACCIDENT_HISTORY)";
				break;
			case 5:
				returnString = "SELECT SUM(salary) FROM EMPLOYEES WHERE salary > 40000";
				break;
			case 6:
				returnString = "SELECT * FROM (ACCOUNTS as a INNER JOIN CAR_INSURANCE_POLICY as c ON a.Account_ID = c.accountNumber) INNER JOIN BOAT_INSURANCE_POLICY as b ON b.accountNumber = a.Account_ID\n";
				break;
		}
		return returnString;
	}

}
