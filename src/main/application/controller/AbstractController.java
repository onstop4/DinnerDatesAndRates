package main.application.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.application.SceneSwitcher;
import main.application.model.User;

/**
 * Provides some functionality for signed-in users, such as allowing them to
 * switch to a different page.
 */
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

	/**
	 * Configures all navigation buttons to switch to different pages when clicked.
	 * 
	 * @param currentUser signed-in user
	 */
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