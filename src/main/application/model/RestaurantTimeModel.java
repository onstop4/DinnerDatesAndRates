package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RestaurantTimeModel {
	public ObservableList<Restaurant> getRestaurantTimes() {
		ObservableList<Restaurant> times = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			int dayOfWeek = DayOfWeek.from(LocalDate.now()).getValue();
			String statement = "select Restaurant.restaurant_id, Restaurant.name, RestaurantHours.opening_time, RestaurantHours.closing_time from RestaurantHours left outer join Restaurant on Restaurant.restaurant_id = RestaurantHours.restaurant_id where RestaurantHours.day_of_week = ? order by RestaurantHours.opening_time";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, dayOfWeek);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				times.add(new Restaurant(rs.getInt("Restaurant.restaurant_id"), rs.getString("Restaurant.name"),
						rs.getTime("RestaurantHours.opening_time").toLocalTime(),
						rs.getTime("RestaurantHours.closing_time").toLocalTime()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return times;
	}
}
