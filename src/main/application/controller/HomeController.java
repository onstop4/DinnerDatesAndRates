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

/**
 * Lists events and restaurant hours for today.
 */
public class HomeController extends AbstractController {
	private EventsModel eventModel;
	private RestaurantTimeModel restaurantTimeModel;

	@FXML
	private ListView<Event> EventsListView;
	@FXML
	private ListView<Restaurant> RestaurantTimesListView;

	/**
	 * Cell in EventsListView. Includes event description and date, as well as a
	 * button that will confirm that the user will attend the event.
	 */
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
				descriptionText.setText(item.getDescription() + "\n" + item.getWhenWillOccurFormatted());
				if (item.willAttend()) {
					button.setDisable(true);
				}
				setGraphic(hbox);
			}
		}
	}

	/**
	 * Cell in RestaurantTimesListView. Includes name of restaurant and when it
	 * opens and closes.
	 */
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
				setGraphic(null);
			} else {
				nameText.setText(item.getName());
				openCloseText.setText(item.getDayHoursFormatted());
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
		SceneSwitcher.getPrimaryStage().setTitle("Home");

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

	/**
	 * Refreshes lists.
	 */
	private void refresh() {
		EventsListView.setItems(eventModel.getEvents());
		RestaurantTimesListView.setItems(restaurantTimeModel.getRestaurantTimes());
	}
}
