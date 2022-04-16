package main.application.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RestaurantReviewsModel {
	private final User currentUser;

	public RestaurantReviewsModel(User currentUser) {
		this.currentUser = currentUser;
	}

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

	public ObservableList<Review> getReviewsOfMenuItem(int menuItemId) {
		ObservableList<Review> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select Review.review_id, User.full_name, Review.rating, Review.comment, Review.date_submitted from Review inner join User on Review.student_id = User.user_id where Review.menu_item_id = ? order by Review.date_submitted desc";

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

	public void submitReview(int menuItemId, int rating, String comment) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Review (student_id, menu_item_id, rating, comment, date_submitted) values (?, ?, ?, ?, ?)";

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
}