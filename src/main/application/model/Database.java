package main.application.model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import main.application.Main;

/**
 * Creates or loads a database connection.
 */
public class Database {
	private static final String propertiesFilename = "settings.conf";
	private static final String defaultPassword = "12345";

	private static final String host = "localhost";
	// Default MySQL port.
	private static final int port = 3306;

	private static final String username = "root";
	private static String password;

	private static Connection conn;

	static {
		// Loads password from properties file. If unable to load password, uses default
		// password instead.
		try {
			Properties prop = new Properties();
			prop.load(Main.class.getClassLoader().getResourceAsStream(propertiesFilename));
			password = prop.getProperty("db_password");
		} catch (IOException | NullPointerException e) {
			System.err.println("Couldn't load properties file " + propertiesFilename);
			password = defaultPassword;
		}
	}

	/**
	 * Returns Connection object. Creates new database connected if one does not
	 * exist or the existing one was closed.
	 * 
	 * @return
	 * @throws SQLException
	 */
	static Connection getConnection() throws SQLException {
		try {
			if (conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/dinnerdates", username,
						password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		return conn;
	}
}
