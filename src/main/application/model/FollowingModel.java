package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FollowingModel {
	private UserModel userModel;

	public FollowingModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public ObservableList<Following> getFriends() {
		ObservableList<Following> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Following.to_id, User.full_name from Following inner join User on Following.to_id = User.user_id where Following.from_id = ? order by User.full_name";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Following(userModel.getId(), userModel.getFullName(), rs.getInt("Following.to_id"),
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
			stmt.setInt(1, userModel.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Following(rs.getInt("Following.from_id"), rs.getString("User.full_name"),
						userModel.getId(), userModel.getFullName()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public ObservableList<OtherUser> getOtherUsers() {
		ObservableList<OtherUser> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select User.user_id, User.full_name from User inner join Student on User.user_id = Student.user_id where User.user_id not in (select to_id from Following where from_id = ?) and User.user_id != ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());
			stmt.setInt(2, userModel.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new OtherUser(rs.getInt("User.user_id"), rs.getString("User.full_name"), userModel));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
