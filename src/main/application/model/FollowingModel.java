package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.application.model.User.UserType;

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
				list.add(new Following(
						new User(currentUser.getId(), null, currentUser.getFullName(), currentUser.getUserType()),
						new User(rs.getInt("Following.to_id"), null, rs.getString("User.full_name"),
								currentUser.getUserType())));
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
				list.add(new Following(
						new User(rs.getInt("Following.from_id"), null, rs.getString("User.full_name"),
								currentUser.getUserType()),
						new User(currentUser.getId(), null, currentUser.getFullName(), currentUser.getUserType())));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ObservableList<User> getOtherUsers() {
		ObservableList<User> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select User.user_id, User.full_name, User.user_type from User where User.user_id != ? and User.user_type = ? and User.user_id not in (select Following.to_id from Following where Following.from_id = ?) order by User.full_name;";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, currentUser.getUserType().ordinal());
			stmt.setInt(3, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new User(rs.getInt("User.user_id"), null, rs.getString("User.full_name"),
						UserType.values()[rs.getInt("User.user_type")]));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void unfriend(User otherUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "delete from Following where from_id = ? and to_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, otherUser.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
