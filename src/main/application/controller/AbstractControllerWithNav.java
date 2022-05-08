package main.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.application.SceneSwitcher;
import main.application.model.User;

/**
 * Abstract class that provides some functionality for signed-in users, such as
 * allowing them to switch to a different page.
 */
public abstract class AbstractControllerWithNav extends AbstractController {
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
	 */
	@Override
	public void configure(User currentUser) {
		NavHomeButton.setOnAction(arg -> SceneSwitcher.switchToHome(currentUser));

		NavAccountSettingsButton.setOnAction(arg -> SceneSwitcher.switchToAccountSettings(currentUser));

		NavCommunityButton.setOnAction(arg -> SceneSwitcher.switchToCommunity(currentUser));

		NavRestaurantReviewsButton.setOnAction(arg -> SceneSwitcher.switchToRestaurantReviews(currentUser));

		NavSignOutButton.setOnAction(arg -> SceneSwitcher.switchToSignIn());
	}
}