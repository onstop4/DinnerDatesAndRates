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
import main.application.model.AccountSettingsModel;
import main.application.model.Restaurant;
import main.application.model.User;

/**
 * Allows students to change values in the database associated with their
 * account.
 */
public class AccountSettingsController extends AbstractController {
	private AccountSettingsModel accountSettingsModel;

	@FXML
	private TextField AcademicYearField;
	@FXML
	private TextField MajorField;
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

		accountSettingsModel = new AccountSettingsModel(currentUser);

		refresh();
	}

	/**
	 * Sets all fields to their associated values in the database.
	 */
	private void refresh() {
		AcademicYearField.setText(String.valueOf(accountSettingsModel.getAcademicYear()));
		MajorField.setText(accountSettingsModel.getMajor());
		FavoriteFoodsField.setText(accountSettingsModel.getFavoriteFoods());
		AvailabilityField.setText(accountSettingsModel.getFavoriteEatingTime());
		InterestsField.setText(accountSettingsModel.getInterests());

		ObservableList<Restaurant> restaurants = accountSettingsModel.getRestaurants();

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
		int favoriteRestaurantId = accountSettingsModel.getFavoriteRestaurantId();
		if (favoriteRestaurantId > 0) {
			IntStream.range(0, restaurants.size()).filter(i -> restaurants.get(i).getId() == favoriteRestaurantId)
					.findFirst().ifPresent(i -> {
						FavoriteRestaurantListView.getSelectionModel().select(i);
					});
		}
	}

	/**
	 * Updates database values using values from fields.
	 */
	@FXML
	private void handleSubmitSettings() {
		accountSettingsModel.setAcademicYear(Integer.parseInt(AcademicYearField.getText()));
		accountSettingsModel.setMajor(MajorField.getText());
		Restaurant favoriteRestaurant = FavoriteRestaurantListView.getSelectionModel().getSelectedItem();
		int favoriteRestaurantId = 0;
		if (favoriteRestaurant != null) {
			favoriteRestaurantId = favoriteRestaurant.getId();
		}
		accountSettingsModel.setFavoriteRestaurantId(favoriteRestaurantId);
		accountSettingsModel.setFavoriteFoods(FavoriteFoodsField.getText());
		accountSettingsModel.setInterests(InterestsField.getText());
		accountSettingsModel.setFavoriteEatingTime(AvailabilityField.getText());

		accountSettingsModel.saveSettings();
	}
}
