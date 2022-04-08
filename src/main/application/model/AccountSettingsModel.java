package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AccountSettingsModel {
	private final UserModel userModel;
	private int academicYear;
	private String major;
	private int favoriteRestaurantId;
	private String favoriteFoods;
	private String interests;
	private String availability;

	public AccountSettingsModel(UserModel userModel) {
		this.userModel = userModel;
		updateObject();
	}

	private void updateObject() {
		try (Connection conn = Database.getConnection()) {
			String statement = "select year, major, interest, favorite_restaurant, preferred_food, availability from Student where user_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, userModel.getId());

			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				throw new NullPointerException();
			}
			academicYear = rs.getInt("year");
			major = rs.getString("major");
			interests = rs.getString("interest");
			favoriteRestaurantId = rs.getInt("favorite_restaurant");
			favoriteFoods = rs.getString("preferred_food");
			availability = rs.getString("availability");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			if (userModel == null) {
				System.err.println("UserModel object that passed to constructor was actually null");
			} else {
				System.err.println("Cannot find student with id " + userModel.getId());
			}
		}
	}

	public int getAcademicYear() {
		return academicYear;
	}

	public void setAcademicYear(int academicYear) {
		this.academicYear = academicYear;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
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
				restaurants.add(new Restaurant(rs.getInt("restaurant_id"), rs.getString("name"), null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	public void saveSettings() {
		try (Connection conn = Database.getConnection()) {
			String statement = "update Student set year = ?, major = ?, interest = ?, favorite_restaurant = ?, preferred_food = ?, availability = ? where user_id = ?";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, academicYear);
			stmt.setString(2, major);
			stmt.setString(3, interests);
			stmt.setInt(4, favoriteRestaurantId);
			stmt.setString(5, favoriteFoods);
			stmt.setString(6, availability);
			stmt.setInt(7, userModel.getId());

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
