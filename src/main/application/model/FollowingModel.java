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
	
	public ObservableList<Following> getFollowing() {
		ObservableList<Following> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Following.from_id, Following.to_id from Following left outer join EventAttendant on Event.event_id = EventAttendant.event_id and EventAttendant.attendant_id = ? where Event.event_date > ? order by Event.event_date";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Following(rs.getInt("Following.from_id"), rs.getInt("Following.to_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
}
