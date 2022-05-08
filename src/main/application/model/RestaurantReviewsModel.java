package main.application.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Assists in querying and updating restaurants, menu items, and reviews.
 */
public class RestaurantReviewsModel {
	private final User currentUser;

	/**
	 * Constructs new object.
	 * 
	 * @param currentUser signed-in user
	 */
	public RestaurantReviewsModel(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Returns list of restaurants sorted by name.
	 * 
	 * @return
	 */
	public ObservableList<Restaurant> getRestaurants() {
		ObservableList<Restaurant> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select restaurant_id, name, description from Restaurant order by name";

			ResultSet rs = conn.createStatement().executeQuery(statement);
			while (rs.next()) {
				list.add(new Restaurant(rs.getInt("restaurant_id"), rs.getString("name"), rs.getString("description")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Returns array of restaurants sorted by id.
	 * 
	 * @return
	 */
	public List<Restaurant> getRestaurantsAsArray() {
		List<Restaurant> list = new ArrayList<>();

		try (Connection conn = Database.getConnection()) {
			String statement = "select restaurant_id, name, description from Restaurant order by restaurant_id";

			ResultSet rs = conn.createStatement().executeQuery(statement);
			while (rs.next()) {
				list.add(new Restaurant(rs.getInt("restaurant_id"), rs.getString("name"), rs.getString("description")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Returns list of menu items of restaurant.
	 * 
	 * @param restaurantId
	 * @return
	 */
	public ObservableList<MenuItem> getMenuOfRestaurant(int restaurantId) {
		ObservableList<MenuItem> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select menu_item_id, name, description, price from MenuItem where restaurant_id = ? order by name";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, restaurantId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new MenuItem(rs.getInt("menu_item_id"), rs.getString("name"), rs.getString("description"),
						rs.getDouble("price")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Returns list of reviews of menu item.
	 * 
	 * @param menuItemId
	 * @return
	 */
	public ObservableList<Review> getReviewsOfMenuItem(int menuItemId) {
		ObservableList<Review> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Review.review_id, User.full_name, Review.rating, Review.comment, Review.date_submitted from Review inner join User on Review.user_id = User.user_id where Review.menu_item_id = ? order by Review.date_submitted, Review.review_id desc";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, menuItemId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				list.add(new Review(rs.getInt("Review.review_id"), rs.getString("User.full_name"),
						rs.getInt("Review.rating"), rs.getString("Review.comment"),
						rs.getDate("Review.date_submitted").toLocalDate()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Adds new review of menu item to database.
	 * 
	 * @param menuItemId
	 * @param rating
	 * @param comment
	 */
	public void submitReview(int menuItemId, int rating, String comment) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Review (user_id, menu_item_id, rating, comment, date_submitted) values (?, ?, ?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, menuItemId);
			stmt.setInt(3, rating);
			stmt.setString(4, comment);
			stmt.setDate(5, Date.valueOf(LocalDate.now()));

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns list of times when restaurants open and close. Only restaurants that
	 * are open today are included.
	 * 
	 * @return
	 */
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
