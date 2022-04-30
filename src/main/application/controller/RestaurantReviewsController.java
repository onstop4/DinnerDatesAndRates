package main.application.controller;

import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.application.SceneSwitcher;
import main.application.model.MenuItem;
import main.application.model.Restaurant;
import main.application.model.RestaurantReviewsModel;
import main.application.model.Review;
import main.application.model.User;

/**
 * Allows user to read and write reviews for the menu items of restaurants.
 */
public class RestaurantReviewsController extends AbstractController {
	private static final int MAX_RATING = 5;

	private RestaurantReviewsModel restaurantReviewModel;

	@FXML
	private ListView<Restaurant> RestaurantsListView;
	@FXML
	private ListView<MenuItem> MenuListView;
	@FXML
	private ListView<Review> ReviewsListView;

	@FXML
	private TextField RatingField;
	@FXML
	private TextField CommentField;

	private static class RestaurantCell extends ListCell<Restaurant> {
		VBox vbox = new VBox();
		Pane pane = new Pane();
		Text nameText = new Text();
		Text descriptionText = new Text();

		public RestaurantCell() {
			super();
			vbox.getChildren().setAll(nameText, descriptionText);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(Restaurant item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(null);
			} else {
				nameText.setText(item.getName());
				descriptionText.setText(item.getDescription());
				setGraphic(vbox);
			}
		}
	}

	private static class MenuCell extends ListCell<MenuItem> {
		private static final DecimalFormat priceFormat = new DecimalFormat("#.00");

		VBox vbox = new VBox();
		Pane pane = new Pane();
		Text nameText = new Text();
		Text priceText = new Text();
		Text descriptionText = new Text();

		public MenuCell() {
			super();
			vbox.getChildren().setAll(nameText, priceText, descriptionText);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(MenuItem item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(null);
			} else {
				nameText.setText(item.getName());
				priceText.setText(priceFormat.format(item.getPrice()));
				descriptionText.setText(item.getDescription());
				setGraphic(vbox);
			}
		}
	}

	private static class ReviewCell extends ListCell<Review> {
		VBox vbox = new VBox();
		Pane pane = new Pane();
		Text authorText = new Text();
		Text dateSubmittedText = new Text();
		Text ratingText = new Text();
		Text commentText = new Text();

		public ReviewCell() {
			super();
			vbox.getChildren().setAll(authorText, dateSubmittedText, ratingText, commentText);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(Review item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(null);
			} else {
				authorText.setText(item.getAuthor());
				dateSubmittedText.setText(item.getDateSubmittedFormatted());
				ratingText.setText(item.getRating() + " out of " + MAX_RATING);
				commentText.setText(item.getComment());
				setGraphic(vbox);
			}
		}
	}

	/**
	 * Configures controller.
	 */
	@Override
	public void configure(User currentUser) {
		super.configure(currentUser);
		SceneSwitcher.getPrimaryStage().setTitle("Restaurant Reviews");

		restaurantReviewModel = new RestaurantReviewsModel(currentUser);

		RestaurantsListView.setCellFactory(new Callback<ListView<Restaurant>, ListCell<Restaurant>>() {
			@Override
			public ListCell<Restaurant> call(ListView<Restaurant> arg0) {
				return new RestaurantCell();
			}
		});

		MenuListView.setCellFactory(new Callback<ListView<MenuItem>, ListCell<MenuItem>>() {
			@Override
			public ListCell<MenuItem> call(ListView<MenuItem> arg0) {
				return new MenuCell();
			}
		});

		ReviewsListView.setCellFactory(new Callback<ListView<Review>, ListCell<Review>>() {
			@Override
			public ListCell<Review> call(ListView<Review> arg0) {
				return new ReviewCell();
			}
		});

		refresh();
	}

	/**
	 * Refreshes list of restaurants, list of menu items, and list of reviews.
	 */
	private void refresh() {
		refreshRestaurantsListView();
		refreshMenuListView();
		refreshReviewsListView();
	}

	/**
	 * Refreshes list of restaurants.
	 */
	private void refreshRestaurantsListView() {
		RestaurantsListView.setItems(restaurantReviewModel.getRestaurants());
	}

	/**
	 * Gets list of menu items of selected restaurant.
	 */
	private void refreshMenuListView() {
		Restaurant selectedRestaurant = RestaurantsListView.getSelectionModel().getSelectedItem();
		if (selectedRestaurant != null) {
			MenuListView.setItems(restaurantReviewModel.getMenuOfRestaurant(selectedRestaurant.getId()));
		}
	}

	/**
	 * Gets list of menu items of selected menu item.
	 */
	private void refreshReviewsListView() {
		MenuItem selectedMenuItem = MenuListView.getSelectionModel().getSelectedItem();
		if (selectedMenuItem != null) {
			ReviewsListView.setItems(restaurantReviewModel.getReviewsOfMenuItem(selectedMenuItem.getId()));
		}
	}

	/**
	 * Refreshes list of menu items.
	 */
	@FXML
	private void handleSelectRestaurant() {
		refreshMenuListView();
	}

	/**
	 * Refreshes list of reviews.
	 */
	@FXML
	private void handleSelectMenuItem() {
		refreshReviewsListView();
	}

	/**
	 * Submits new review if rating is set properly. If rating is not an integer
	 * between 1 and MAX_RATING, an error message will be shown.
	 */
	@FXML
	private void handleSubmitReview() {
		MenuItem menuItem = MenuListView.getSelectionModel().getSelectedItem();

		if (menuItem != null) {
			Alert a = new Alert(AlertType.ERROR);
			int rating;
			String comment = CommentField.getText();
			try {
				rating = Integer.parseInt(RatingField.getText().strip());
			} catch (NumberFormatException e) {
				rating = 0;
			}

			if (rating < 1 || rating > MAX_RATING) {
				a.setHeaderText("Rating must be an integer between 1 and 5.");
				a.show();
			} else {
				restaurantReviewModel.submitReview(menuItem.getId(), rating, comment);
				refreshReviewsListView();
			}
		}
	}
}
