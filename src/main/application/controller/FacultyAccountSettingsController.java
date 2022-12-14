package main.application.controller;

import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.util.StringConverter;
import main.application.SceneSwitcher;
import main.application.model.FacultyAccountSettingsModel;
import main.application.model.Restaurant;
import main.application.model.User;

/**
 * Allows faculty members to change values in the database associated with their
 * account.
 */
public class FacultyAccountSettingsController extends AbstractControllerWithNav {
	private FacultyAccountSettingsModel facultyAccountSettingsModel;

	@FXML
	private ListView<Restaurant> FavoriteRestaurantListView;
	@FXML
	private TextField FavoriteFoodsField;
	@FXML
	private TextArea AvailabilityField;
	@FXML
	private TextArea InterestsField;

	/**
	 * Configures controller.
	 */
	@Override
	public void configure(User currentUser) {
		super.configure(currentUser);
		SceneSwitcher.getPrimaryStage().setTitle("Account Settings");

		facultyAccountSettingsModel = new FacultyAccountSettingsModel(currentUser);

		refresh();
	}

	/**
	 * Sets all fields to their associated values in the database.
	 */
	private void refresh() {
		FavoriteFoodsField.setText(facultyAccountSettingsModel.getFavoriteFoods());
		AvailabilityField.setText(facultyAccountSettingsModel.getFavoriteEatingTime());
		InterestsField.setText(facultyAccountSettingsModel.getInterests());

		ObservableList<Restaurant> restaurants = facultyAccountSettingsModel.getRestaurants();
		restaurants.add(0, null);

		FavoriteRestaurantListView.setItems(restaurants);
		FavoriteRestaurantListView.setCellFactory(ComboBoxListCell.forListView(new StringConverter<Restaurant>() {
			@Override
			public Restaurant fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Restaurant restaurant) {
				if (restaurant == null) {
					return "-- No favorite restaurant --";
				}
				return restaurant.getName();
			}
		}, restaurants));

		// Selects favorite restaurant recorded in database. Selects the first item ("--
		// No favorite restaurant --") if database value is null (which JDBC translates
		// to 0).
		int favoriteRestaurantId = facultyAccountSettingsModel.getFavoriteRestaurantId();
		if (favoriteRestaurantId > 0) {
			IntStream.range(1, restaurants.size()).filter(i -> restaurants.get(i).getId() == favoriteRestaurantId)
					.findFirst().ifPresent(i -> {
						FavoriteRestaurantListView.getSelectionModel().select(i);
					});
		} else {
			FavoriteRestaurantListView.getSelectionModel().select(0);
		}
	}

	/**
	 * Updates database values using values from fields.
	 */
	@FXML
	private void handleSubmitSettings() {
		Restaurant favoriteRestaurant = FavoriteRestaurantListView.getSelectionModel().getSelectedItem();
		int favoriteRestaurantId = 0;
		if (favoriteRestaurant != null) {
			favoriteRestaurantId = favoriteRestaurant.getId();
		}
		facultyAccountSettingsModel.setFavoriteRestaurantId(favoriteRestaurantId);
		facultyAccountSettingsModel.setFavoriteFoods(FavoriteFoodsField.getText());
		facultyAccountSettingsModel.setInterests(InterestsField.getText());
		facultyAccountSettingsModel.setFavoriteEatingTime(AvailabilityField.getText());

		facultyAccountSettingsModel.saveSettings();
	}
}
