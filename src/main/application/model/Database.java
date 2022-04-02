package main.application.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	private static final String host = "localhost";
	// Default MySQL port.
	private static final int port = 3306;

	private static final String username = "root";
	private static final String password = "12345";
	
	private static Connection conn;

	public static Connection getConnection() throws SQLException{
		try {
		if (conn == null || conn.isClosed()) {
			conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/dinnerdates", username, password);
		}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		return conn;
	}
}
