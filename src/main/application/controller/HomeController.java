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
import main.application.model.RestaurantTime;
import main.application.model.RestaurantTimeModel;
import main.application.model.UserModel;

public class HomeController {
	private EventsModel eventModel;
	private RestaurantTimeModel restaurantTimeModel;

	@FXML
	private ListView<Event> EventsListView;
	@FXML
	private ListView<RestaurantTime> RestaurantTimesListView;

	private static class EventCell extends ListCell<Event> {
		HBox hbox = new HBox();
		Text descriptionText = new Text("");
		Pane pane = new Pane();
		Button button = new Button("I will attend");
		Event lastItem;

		public EventCell(UserModel userModel) {
			super();
			hbox.getChildren().addAll(descriptionText, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					lastItem.confirmAttendance(userModel);
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
				descriptionText.setText(item.getDescription());
				if (item.willAttend()) {
					button.setDisable(true);
				}
				setGraphic(hbox);
			}
		}
	}

	private static class RestaurantTimesCell extends ListCell<RestaurantTime> {
		VBox vbox = new VBox();
		Pane pane = new Pane();
		Text nameText = new Text();
		Text openCloseText = new Text();

		public RestaurantTimesCell() {
			super();
			vbox.getChildren().addAll(nameText, openCloseText);
			HBox.setHgrow(pane, Priority.ALWAYS);
		}

		@Override
		protected void updateItem(RestaurantTime item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);
			if (empty) {
				setGraphic(vbox);
			} else {
				nameText.setText(item.getRestaurantName());
				openCloseText.setText(item.getDayHoursFormatted());
				setGraphic(vbox);
			}
		}
	}
	
	public void configure(UserModel userModel) {
		SceneSwitcher.getPrimaryStage().setTitle("Home");

		eventModel = new EventsModel(userModel);
		EventsListView.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
			@Override
			public ListCell<Event> call(ListView<Event> param) {
				return new EventCell(userModel);
			}
		});
		
		restaurantTimeModel = new RestaurantTimeModel();
		RestaurantTimesListView.setCellFactory(new Callback<ListView<RestaurantTime>, ListCell<RestaurantTime>>() {
			@Override
			public ListCell<RestaurantTime> call(ListView<RestaurantTime> param) {
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
