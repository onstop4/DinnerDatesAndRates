package main.application.controller;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.application.SceneSwitcher;
import main.application.model.Event;
import main.application.model.EventsModel;
import main.application.model.Restaurant;
import main.application.model.RestaurantReviewsModel;
import main.application.model.User;

/**
 * Lists events and restaurant hours for today.
 */
public class HomeController extends AbstractControllerWithNav {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM");

	private User currentUser;
	private EventsModel eventModel;
	private RestaurantReviewsModel restaurantTimeModel;

	@FXML
	private Label UpcomingEventsHeadingText;
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
		Button button = new Button("Attending?");
		Event item;

		public EventCell(User currentUser) {
			super();
			hbox.getChildren().setAll(descriptionText, pane, button);
			HBox.setHgrow(pane, Priority.ALWAYS);
			button.setOnAction(arg -> {
				if (item.willAttend()) {
					item.denyAttendance(currentUser);
				} else {
					item.confirmAttendance(currentUser);
				}

				editButton();
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
				editButton();
				setGraphic(hbox);
			}
		}

		/**
		 * Edits button text based on whether or not current user is attending event.
		 */
		private void editButton() {
			if (item.willAttend()) {
				button.setText("✔");
			} else {
				button.setText("Attending?");
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
		this.currentUser = currentUser;
		super.configure(currentUser);
		SceneSwitcher.getPrimaryStage().setTitle("Home");

		eventModel = new EventsModel(currentUser);
		EventsListView.setCellFactory(arg -> new EventCell(currentUser));

		restaurantTimeModel = new RestaurantReviewsModel(currentUser);
		RestaurantTimesListView.setCellFactory(arg -> new RestaurantTimesCell());

		refresh();
	}

	/**
	 * Refreshes lists and updates heading text.
	 */
	private void refresh() {
		YearMonth yearMonth = YearMonth.now();
		UpcomingEventsHeadingText.setText("Upcoming Events in " + formatter.format(yearMonth));
		EventsListView.setItems(eventModel.getEventsThatOccurLaterInMonth(yearMonth));
		RestaurantTimesListView.setItems(restaurantTimeModel.getRestaurantTimes());
	}

	/**
	 * Switches to Event Calendar page.
	 */
	@FXML
	private void handleNavEventCalendar() {
		SceneSwitcher.switchToEventCalendar(currentUser);
	}
}
