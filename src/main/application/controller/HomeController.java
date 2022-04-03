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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.application.model.Event;
import main.application.model.EventsModel;
import main.application.model.UserModel;

public class HomeController {
	private EventsModel eventModel;

	@FXML
	private ListView<Event> EventsListView;

	private static class XCell extends ListCell<Event> {
		HBox hbox = new HBox();
		Text descriptionText = new Text("(empty)");
		Pane pane = new Pane();
		Button button = new Button("I will attend");
		Event lastItem;

		public XCell(UserModel userModel) {
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
				lastItem = null;
				setGraphic(null);
			} else {
				lastItem = item;
				descriptionText.setText(item.getDescription());
				if (item.willAttend()) {
					button.setDisable(true);
				}
				setGraphic(hbox);
			}
		}
	}

	public void configure(Stage stage, UserModel userModel) {
		stage.setTitle("Home");

		eventModel = new EventsModel(userModel);
		EventsListView.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
			@Override
			public ListCell<Event> call(ListView<Event> param) {
				return new XCell(userModel);
			}
		});

		refresh();
	}

	public void refresh() {
		EventsListView.setItems(eventModel.getEvents());
	}
}