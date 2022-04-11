package main.application.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.application.SceneSwitcher;
import main.application.model.Event;
import main.application.model.EventsModel;
import main.application.model.Restaurant;
import main.application.model.RestaurantTimeModel;
import main.application.model.User;

public class HomeController {
	private EventsModel eventModel;
	private RestaurantTimeModel restaurantTimeModel;

	@FXML
	private Button NavHomeButton;
	@FXML
	private Button NavAccountSettingsButton;
	@FXML
	private Button NavCommunityButton;
	@FXML
	private Button NavRestaurantReviewsButton;

	@FXML
	private ListView<Event> EventsListView;
	@FXML
	private ListView<Restaurant> RestaurantTimesListView;

	private static class EventCell extends ListCell<Event> {
		HBox hbox = new HBox();
		Text descriptionText = new Text("");
		Pane pane = new Pane();
		Button button = new Button("I will attend");
		Event item;

		public EventCell(User currentUser) {
			super();
			hbox.getChildren().setAll(descriptionText, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					item.confirmAttendance(currentUser);
					button.setDisable(true);
				}
			});
		}

		@Override
		protected void updateItem(Event item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(null);
			} else {
				this.item = item;
				descriptionText.setText(item.getDescription());
				if (item.willAttend()) {
					button.setDisable(true);
				}
				setGraphic(hbox);
			}
		}
	}

	private static class RestaurantTimesCell extends ListCell<Restaurant> {
		VBox vbox = new VBox();
		Pane pane = new Pane();
		Text nameText = new Text();
		Text openCloseText = new Text();

		public RestaurantTimesCell() {
			super();
			vbox.getChildren().setAll(nameText, openCloseText);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(Restaurant item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(vbox);
			} else {
				nameText.setText(item.getName());
				openCloseText.setText(item.getDayHoursFormatted());
				setGraphic(vbox);
			}
		}
	}

	public void configure(User currentUser) {
		SceneSwitcher.getPrimaryStage().setTitle("Home");
		Navbar.configureAllNavButtons(currentUser, NavHomeButton, NavAccountSettingsButton, NavCommunityButton,
				NavRestaurantReviewsButton);

		eventModel = new EventsModel(currentUser);
		EventsListView.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
			@Override
			public ListCell<Event> call(ListView<Event> param) {
				return new EventCell(currentUser);
			}
		});

		restaurantTimeModel = new RestaurantTimeModel();
		RestaurantTimesListView.setCellFactory(new Callback<ListView<Restaurant>, ListCell<Restaurant>>() {
			@Override
			public ListCell<Restaurant> call(ListView<Restaurant> param) {
				return new RestaurantTimesCell();
			}
		});

		refresh();
	}

	public void refresh() {
		EventsListView.setItems(eventModel.getEvents());
		RestaurantTimesListView.setItems(restaurantTimeModel.getRestaurantTimes());
	}
}
