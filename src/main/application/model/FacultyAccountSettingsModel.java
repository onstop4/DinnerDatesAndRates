package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FacultyAccountSettingsModel {
	private final User currentUser;
	private int favoriteRestaurantId;
	private String favoriteFoods;
	private String interests;
	private String availability;

	public FacultyAccountSettingsModel(User currentUser) {
		this.currentUser = currentUser;
		updateObject();
	}

	private void updateObject() {
		try (Connection conn = Database.getConnection()) {
			String statement = "select interest, favorite_restaurant, preferred_food, availability from FacultyMember where user_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new NullPointerException();
			}
			interests = rs.getString("interest");
			favoriteRestaurantId = rs.getInt("favorite_restaurant");
			favoriteFoods = rs.getString("preferred_food");
			availability = rs.getString("availability");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			if (currentUser == null) {
				System.err.println("UserModel object that passed to constructor was actually null");
			} else {
				System.err.println("Cannot find student with id " + currentUser.getId());
			}
		}
	}

	public int getFavoriteRestaurantId() {
		return favoriteRestaurantId;
	}

	public void setFavoriteRestaurantId(int favoriteRestaurantId) {
		this.favoriteRestaurantId = favoriteRestaurantId;
	}

	public String getFavoriteFoods() {
		return favoriteFoods;
	}

	public void setFavoriteFoods(String favoriteFoods) {
		this.favoriteFoods = favoriteFoods;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getFavoriteEatingTime() {
		return availability;
	}

	public void setFavoriteEatingTime(String favoriteEatingTime) {
		this.availability = favoriteEatingTime;
	}

	public ObservableList<Restaurant> getRestaurants() {
		ObservableList<Restaurant> restaurants = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select restaurant_id, name from Restaurant order by name";

			ResultSet rs = conn.createStatement().executeQuery(statement);
			while (rs.next()) {
				restaurants.add(new Restaurant(rs.getInt("restaurant_id"), rs.getString("name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	public void saveSettings() {
		try (Connection conn = Database.getConnection()) {
			String statement = "update FacultyMember set interest = ?, favorite_restaurant = ?, preferred_food = ?, availability = ? where user_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setString(1, interests);
			if (favoriteRestaurantId != 0) {
				stmt.setInt(2, favoriteRestaurantId);
			} else {
				stmt.setNull(2, Types.INTEGER);
			}
			stmt.setString(3, favoriteFoods);
			stmt.setString(4, availability);
			stmt.setInt(5, currentUser.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
