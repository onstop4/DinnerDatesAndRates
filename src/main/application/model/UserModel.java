package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
	private int id;
	private String username;
	private String fullName;

	private UserModel(int id, String username, String fullName) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
	}

	public static UserModel get_user_model(String username, String password) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(
				"select User.user_id, User.username, User.full_name from User where lower(username) = lower(?) and password = ?");

		stmt.setString(1, username);
		stmt.setString(2, password);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return new UserModel(rs.getInt("user_id"), rs.getString("username"), rs.getString("full_name"));
		} else {
			return null;
		}
	}

	public static UserModel create_user_model(String username, String password, String fullName) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn
				.prepareStatement("insert into User (username, password, full_name) values (?, ?, ?)");

		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.setString(3, fullName);

		stmt.executeUpdate();

		return get_user_model(username, password);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
}
