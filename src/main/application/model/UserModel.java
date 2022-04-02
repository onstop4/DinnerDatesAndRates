package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {

	private int id;
	private String username;

	private UserModel(int id, String username) {
		this.id = id;
		this.username = username;
	}

	public static UserModel get_user_model(String username, String password) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement("select User.user_id, User.username from User where lower(username) = lower(?) and password = ?");
			
		stmt.setString(1, username);
		stmt.setString(2, password);
		
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			return new UserModel(rs.getInt("user_id"), rs.getString("username"));
		} else {
			return null;
		}
	}

	public static UserModel create_user_model(String username, String password) throws SQLException {
		Connection conn = Database.getConnection();
		PreparedStatement stmt = conn.prepareStatement("insert into User (username, password) values (?, ?)");
		
		stmt.setString(1, username);
		stmt.setString(2, password);

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
}
