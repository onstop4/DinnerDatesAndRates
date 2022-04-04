package main.application.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RestaurantTime {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("K:mm a");
	
	private final String restaurantName;
	private final LocalTime timeOpen;
	private final LocalTime timeClosed;
	
	RestaurantTime(String restaurantName, LocalTime timeOpen, LocalTime timeClosed) {
		this.restaurantName = restaurantName;
		this.timeOpen = timeOpen;
		this.timeClosed = timeClosed;
	}
	
	public String getRestaurantName() {
		return restaurantName;
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
