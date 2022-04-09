package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
	private int id;
	private String username;
	private String fullName;

	public User(int id, String username, String fullName) {
		this.id = id;
		this.username = username;
		this.fullName = fullName;
	}

	public static User get_user(String username, String password) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement(
				"select User.user_id, User.username, User.full_name from User where lower(username) = lower(?) and password = ?");

		stmt.setString(1, username);
		stmt.setString(2, password);

		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("full_name"));
		} else {
			return null;
		}
	}

	public static User create_user(String username, String password, String fullName) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn
				.prepareStatement("insert into User (username, password, full_name) values (?, ?, ?)");

		stmt.setString(1, username);
		stmt.setString(2, password);
		stmt.setString(3, fullName);

		stmt.executeUpdate();

		return get_user(username, password);
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

	public boolean addAsFriend(User currentUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Following (from_id, to_id) values (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
