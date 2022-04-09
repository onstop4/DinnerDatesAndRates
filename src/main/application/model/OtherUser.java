package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OtherUser {
	private final int id;
	private final String fullName;
	private final UserModel currentUser;

	OtherUser(int id, String fullName, UserModel currentUser) {
		this.id = id;
		this.fullName = fullName;
		this.currentUser = currentUser;
	}

	public int getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public UserModel getCurrentUser() {
		return currentUser;
	}

	public boolean addFriend() {
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
