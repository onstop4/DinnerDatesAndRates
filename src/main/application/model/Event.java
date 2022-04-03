package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Event {
	private final int id;
	private final String description;
	private final LocalDate date;
	private final boolean willAttend;

	public Event(int id, String description, LocalDate date, boolean willAttend) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.willAttend = willAttend;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean willAttend() {
		return willAttend;
	}

	public void confirmAttendance(UserModel userModel) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into EventAttendant (attendant_id, event_id) values (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
