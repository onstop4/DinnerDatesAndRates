package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Stores event info. Can confirm that current user is attending event.
 */
public class Event {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	private final int id;
	private final String description;
	private final LocalDate date;
	private boolean willAttend;

	/**
	 * Constructs new object.
	 * 
	 * @param id          id of event
	 * @param description description of event
	 * @param date        date of event
	 * @param willAttend  whether or not signed-in user will attend
	 */
	Event(int id, String description, LocalDate date, boolean willAttend) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.willAttend = willAttend;
	}

	/**
	 * Returns id of event.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns description of event.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns date of event.
	 * 
	 * @return
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * Returns whether or not signed-in user will attend event.
	 * 
	 * @return
	 */
	public boolean willAttend() {
		return willAttend;
	}

	/**
	 * Confirms that signed-in user will attend event.
	 * 
	 * @param currentUser
	 */
	public void confirmAttendance(User currentUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into EventAttendant (attendant_id, event_id) values (?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
			willAttend = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes record that signed-in user will attend event.
	 * 
	 * @param currentUser
	 */
	public void denyAttendance(User currentUser) {
		try (Connection conn = Database.getConnection()) {
			String statement = "delete from EventAttendant where attendant_id = ? and event_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, id);

			stmt.executeUpdate();
			willAttend = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns date of event formatted as a string.
	 * 
	 * @return
	 */
	public String getDateFormatted() {
		return date.format(formatter);
	}

	/**
	 * If the event will occur today, returns "Occurs today". Otherwise, returns
	 * date of event formatted as a string.
	 * 
	 * @return
	 */
	public String getWhenWillOccurFormatted() {
		if (LocalDate.now().isEqual(date)) {
			return "Occurs today";
		}
		return "Will occur on " + getDateFormatted();
	}
}
