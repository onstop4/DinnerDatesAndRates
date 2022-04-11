package main.application.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.application.SceneSwitcher;
import main.application.model.User;

public abstract class AbstractController {
	@FXML
	private Button NavHomeButton;
	@FXML
	private Button NavAccountSettingsButton;
	@FXML
	private Button NavCommunityButton;
	@FXML
	private Button NavRestaurantReviewsButton;
	@FXML
	private Button NavSignOutButton;

	public void configure(User currentUser) {
		NavHomeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToHome(currentUser);
			}
		});

		NavAccountSettingsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToAccountSettings(currentUser);
			}
		});

		NavCommunityButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToCommunity(currentUser);
			}
		});

		NavRestaurantReviewsButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToRestaurantReviews(currentUser);
			}
		});

		NavSignOutButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToSignIn();
			}
		});
	}
}