package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FollowingModel {
	private User currentUser;

	public FollowingModel(User currentUser) {
		this.currentUser = currentUser;
	}

	public ObservableList<Following> getFriends() {
		ObservableList<Following> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Following.to_id, User.full_name from Following inner join User on Following.to_id = User.user_id where Following.from_id = ? order by User.full_name";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Following(currentUser.getId(), currentUser.getFullName(), rs.getInt("Following.to_id"),
						rs.getString("User.full_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ObservableList<Following> getFollowing() {
		ObservableList<Following> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Following.from_id, User.full_name from Following inner join User on Following.from_id = User.user_id where Following.to_id = ? order by User.full_name";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Following(rs.getInt("Following.from_id"), rs.getString("User.full_name"),
						currentUser.getId(), currentUser.getFullName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ObservableList<User> getOtherUsers() {
		ObservableList<User> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select User.user_id, User.full_name from User inner join Student on User.user_id = Student.user_id where User.user_id not in (select to_id from Following where from_id = ?) and User.user_id != ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new User(rs.getInt("User.user_id"), rs.getString("User.username"),
						rs.getString("User.full_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
