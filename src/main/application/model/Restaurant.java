package main.application.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores restaurant info.
 */
public class Restaurant {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("K:mm a");

	private final int id;
	private final String name;
	private final String description;
	private final LocalTime timeOpen;
	private final LocalTime timeClosed;

	/**
	 * Constructs new object.
	 * 
	 * @param id          id of restaurant
	 * @param name        name of restaurant
	 * @param description description of restaurant
	 * @param timeOpen    time restaurant opens
	 * @param timeClosed  time restaurant closes
	 */
	Restaurant(int id, String name, String description, LocalTime timeOpen, LocalTime timeClosed) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.timeOpen = timeOpen;
		this.timeClosed = timeClosed;
	}

	/**
	 * Constructs new object with some default values.
	 * 
	 * @param id   id of restaurant
	 * @param name name of restaurant
	 */
	Restaurant(int id, String name) {
		this.id = id;
		this.name = name;
		this.description = null;
		this.timeOpen = null;
		this.timeClosed = null;
	}

	/**
	 * Constructs new object with some default values.
	 * 
	 * @param id          id of restaurant
	 * @param name        name of restaurant
	 * @param description description of restaurant
	 */
	Restaurant(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.timeOpen = null;
		this.timeClosed = null;
	}

	/**
	 * Constructs new object with some default values.
	 * 
	 * @param id         id of restaurant
	 * @param name       name of restaurant
	 * @param timeOpen   time restaurant opens
	 * @param timeClosed time restaurant closes
	 */
	Restaurant(int id, String name, LocalTime timeOpen, LocalTime timeClosed) {
		this.id = id;
		this.name = name;
		this.description = null;
		this.timeOpen = timeOpen;
		this.timeClosed = timeClosed;
	}

	/**
	 * Returns id of restaurant.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns name of restaurant.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns description of restaurant.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns time when restaurant opens.
	 * 
	 * @return
	 */
	public LocalTime getTimeOpen() {
		return timeOpen;
	}

	/**
	 * Returns time when restaurant closes.
	 * 
	 * @return
	 */
	public LocalTime getTimeClosed() {
		return timeClosed;
	}

	/**
	 * Returns times when restaurant opens and closes formatted as a string.
	 * 
	 * @return
	 */
	public String getDayHoursFormatted() {
		return "Open from " + timeOpen.format(formatter) + " to " + timeClosed.format(formatter);
	}
}
