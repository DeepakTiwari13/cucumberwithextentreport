package com.citrus.util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.cucumber.listener.Reporter;

public class oracledbUtil {

	public HashMap<String, String> dbvalue = new HashMap<String, String>();
	public static Connection connection = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;
	Fileutil fu = new Fileutil();

	public Connection makeconnectionwithDB() {

		connection = null;
		System.out.println("Selected endpoint " + fu.prop.getProperty("test_end_point_api"));
		System.out.println("-------- Making connection with Oracle--------" + fu.prop.getProperty("test_db_url"));
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Issue in jdbc driver " + e.fillInStackTrace());
			return null;
		}
		System.out.println("Oracle JDBC Driver Registered ");
		try {
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@//" + fu.prop.getProperty("test_db_url") + ":"
							+ fu.prop.getProperty("test_db_port") + "/" + fu.prop.getProperty("test_db_service"),
					fu.prop.getProperty("test_db_user"), fu.prop.getProperty("test_db_password"));
		} catch (SQLException e) {
			System.out.println("Connection Failed! report " + e.fillInStackTrace());
			return null;
		}
		if (connection != null) {
			System.out.println("Connection established ");
		}
		return connection;
	}

	public void closeconnectionwithDB() {
		if (connection != null) {
			try {
				connection.close();
				Reporter.addStepLog("Connection closed with database ");
			} catch (SQLException e) {
				Reporter.addStepLog("Failed to close connection " + e.fillInStackTrace());
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
				Reporter.addStepLog("Statement closed with database ");
			} catch (SQLException e) {
				Reporter.addStepLog("Failed to close connection with stmt " + e.fillInStackTrace());
			}
		}
		if (rs != null) {
			try {
				rs.close();
				Reporter.addStepLog("Result set closed with database ");
			} catch (SQLException e) {
				Reporter.addStepLog("Failed to close connection with rs " + e.fillInStackTrace());
			}
		}
	}

	public HashMap<String, String> getdatafromDB(String tid) throws SQLException {
		
		boolean isresultsetavailable=true;

		String query = "select COLUMN1,COLUMN2 from tablename where condition = '"
				+ tid + "'";
		Reporter.addStepLog("Executing query :" + query);

		if (connection != null) {
			Reporter.addStepLog("Using existing connection ");
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);

			for (int i = 0; i < 60; i++) {
				rs = stmt.executeQuery(query);
				while (rs.next()) {
										
					String data1 = rs.getString("COLUMN1");
					Reporter.addStepLog("COLUMN1 " + data1);
					dbvalue.put("COLUMN1", data1);
					String data2 = rs.getString("COLUMN2");
					Reporter.addStepLog("COLUMN2 " + data2);
					dbvalue.put("COLUMN2", data2);
										
					Reporter.addStepLog("Hashmap size in dbutil " + dbvalue.size());
					if (!rs.next()) {
						isresultsetavailable = false;
					}
					
				}
				try {
					Thread.sleep(1000);
					Reporter.addStepLog("Added delay in result set fetching ");
				} catch (InterruptedException e) {
					Reporter.addStepLog(" Exception in sleep thread " + e.fillInStackTrace());
				}
				if(isresultsetavailable==false) {
					break;
				}
			}

		} else {
			Reporter.addStepLog("Failed to retrieve data ");
			return null;
		}
		return dbvalue;
	}
}
