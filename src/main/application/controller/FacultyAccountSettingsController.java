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

public class FacultyAccountSettingsController extends AbstractController {
	private FacultyAccountSettingsModel facultyAccountSettingsModel;

	@FXML
	private ListView<Restaurant> FavoriteRestaurantListView;
	@FXML
	private TextField FavoriteFoodsField;
	@FXML
	private TextArea AvailabilityField;
	@FXML
	private TextArea InterestsField;

	public void configure(User currentUser) {
		super.configure(currentUser);
		SceneSwitcher.getPrimaryStage().setTitle("Account Settings");

		facultyAccountSettingsModel = new FacultyAccountSettingsModel(currentUser);

		refresh();
	}

	private void refresh() {
		FavoriteFoodsField.setText(facultyAccountSettingsModel.getFavoriteFoods());
		AvailabilityField.setText(facultyAccountSettingsModel.getFavoriteEatingTime());
		InterestsField.setText(facultyAccountSettingsModel.getInterests());

		ObservableList<Restaurant> restaurants = facultyAccountSettingsModel.getRestaurants();

		FavoriteRestaurantListView.setItems(restaurants);
		FavoriteRestaurantListView.setCellFactory(ComboBoxListCell.forListView(new StringConverter<Restaurant>() {
			@Override
			public Restaurant fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(Restaurant restaurant) {
				return restaurant.getName();
			}
		}, restaurants));

		// Selects favorite restaurant recorded in database.
		int favoriteRestaurantId = facultyAccountSettingsModel.getFavoriteRestaurantId();
		if (favoriteRestaurantId > 0) {
			IntStream.range(0, restaurants.size()).filter(i -> restaurants.get(i).getId() == favoriteRestaurantId)
					.findFirst().ifPresent(i -> {
						FavoriteRestaurantListView.getSelectionModel().select(i);
					});
		}
	}

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
