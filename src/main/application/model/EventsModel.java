package main.application.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EventsModel {
	private UserModel userModel;

	public EventsModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public ObservableList<Event> getEvents() {
		ObservableList<Event> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Event.event_id, Event.description, Event.event_date, EventAttendant.attendant_id from Event left outer join EventAttendant on Event.event_id = EventAttendant.event_id and EventAttendant.attendant_id = ? where Event.event_date > ? order by Event.event_date";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());
			stmt.setDate(2, Date.valueOf(LocalDate.now()));

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				boolean willAttend = rs.getInt("EventAttendant.attendant_id") > 0;
				list.add(new Event(rs.getInt("Event.event_id"), rs.getString("Event.description"),
						rs.getDate("Event.event_date").toLocalDate(), willAttend));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
}
