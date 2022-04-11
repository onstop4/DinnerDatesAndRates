package main.application.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Restaurant {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("K:mm a");

	private final int id;
	private final String name;
	private final String description;
	private final LocalTime timeOpen;
	private final LocalTime timeClosed;

	Restaurant(int id, String name, String description, LocalTime timeOpen, LocalTime timeClosed) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.timeOpen = timeOpen;
		this.timeClosed = timeClosed;
	}

	Restaurant(int id, String name) {
		this.id = id;
		this.name = name;
		this.description = null;
		this.timeOpen = null;
		this.timeClosed = null;
	}

	Restaurant(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.timeOpen = null;
		this.timeClosed = null;
	}

	Restaurant(int id, String name, LocalTime timeOpen, LocalTime timeClosed) {
		this.id = id;
		this.name = name;
		this.description = null;
		this.timeOpen = timeOpen;
		this.timeClosed = timeClosed;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public LocalTime getTimeOpen() {
		return timeOpen;
	}

	public LocalTime getTimeClosed() {
		return timeClosed;
	}

	public String getDayHoursFormatted() {
		return "From " + timeOpen.format(formatter) + " to " + timeClosed.format(formatter);
	}
}
