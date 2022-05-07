package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Assists in querying and updating events.
 */
public class EventsModel {
	private final User currentUser;

	/**
	 * Constructs new object.
	 * 
	 * @param currentUser signed-in user
	 */
	public EventsModel(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Returns list of events of a specific month.
	 * 
	 * @param yearMonth month
	 * @return
	 */
	public ObservableList<Event> getEventsOfMonth(YearMonth yearMonth) {
		ObservableList<Event> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Event.event_id, Event.description, Event.event_date, EventAttendant.attendant_id from Event left outer join EventAttendant on Event.event_id = EventAttendant.event_id and EventAttendant.attendant_id = ? where month(Event.event_date) = ? and year(Event.event_date) = ? order by Event.event_date";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, yearMonth.getMonthValue());
			stmt.setInt(3, yearMonth.getYear());

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
