package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Assists in querying and updating account settings for faculty members.
 */
public class FacultyAccountSettingsModel {
	private final User currentUser;
	private int favoriteRestaurantId;
	private String favoriteFoods;
	private String interests;
	private String availability;

	/**
	 * Constructs new objects and initializes instance variables with values from
	 * database.
	 * 
	 * @param currentUser signed-in user
	 */
	public FacultyAccountSettingsModel(User currentUser) {
		this.currentUser = currentUser;
		updateObject();
	}

	/**
	 * Sets instance variables using values from database.
	 */
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

	/**
	 * Returns favorite restaurant id.
	 * 
	 * @return
	 */
	public int getFavoriteRestaurantId() {
		return favoriteRestaurantId;
	}

	/**
	 * Sets favorite restaurant id.
	 * 
	 * @param favoriteRestaurantId
	 */
	public void setFavoriteRestaurantId(int favoriteRestaurantId) {
		this.favoriteRestaurantId = favoriteRestaurantId;
	}

	/**
	 * Returns favorite foods.
	 * 
	 * @return
	 */
	public String getFavoriteFoods() {
		return favoriteFoods;
	}

	/**
	 * Sets favorite foods.
	 * 
	 * @param favoriteFoods
	 */
	public void setFavoriteFoods(String favoriteFoods) {
		this.favoriteFoods = favoriteFoods;
	}

	/**
	 * Returns availability.
	 * 
	 * @return
	 */
	public String getAvailability() {
		return availability;
	}

	/**
	 * Sets availability.
	 * 
	 * @param availability availability
	 */
	public void setAvailability(String availability) {
		this.availability = availability;
	}

	/**
	 * Returns interests.
	 * 
	 * @return
	 */
	public String getInterests() {
		return interests;
	}

	/**
	 * Sets interests.
	 * 
	 * @param interests
	 */
	public void setInterests(String interests) {
		this.interests = interests;
	}

	/**
	 * Returns favorite eating time.
	 * 
	 * @return
	 */
	public String getFavoriteEatingTime() {
		return availability;
	}

	/**
	 * Sets favorite eating time.
	 * 
	 * @param favoriteEatingTime
	 */
	public void setFavoriteEatingTime(String favoriteEatingTime) {
		this.availability = favoriteEatingTime;
	}

	/**
	 * Returns list of restaurants.
	 * 
	 * @return
	 */
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

	/**
	 * Saves values of instance variables to database.
	 */
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
