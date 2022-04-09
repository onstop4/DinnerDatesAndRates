package main.application.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.application.SceneSwitcher;
import main.application.model.User;

class Navbar {
	/**
	 * Configures all buttons in navigation bar.
	 * 
	 * @param currentUser
	 * @param navHomeButton
	 * @param navAccountSettingsButton
	 * @param navCommunityButton
	 * @param navRestaurantReviewsButton
	 */
	static void configureAllNavButtons(User currentUser, Button navHomeButton, Button navAccountSettingsButton,
			Button navCommunityButton, Button navRestaurantReviewsButton) {
		configureNavHomeButton(navHomeButton, currentUser);
		configureNavAccountSettingsButton(navAccountSettingsButton, currentUser);
		configureNavCommunityButton(navCommunityButton, currentUser);
	}

	/**
	 * Configures button that will switch to the Home scene.
	 * 
	 * @param button
	 * @param currentUser
	 */
	static void configureNavHomeButton(Button button, User currentUser) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToHome(currentUser);
			}
		});
	}

	/**
	 * Configures button that will switch to the Account Settings scene.
	 * 
	 * @param button
	 * @param currentUser
	 */
	static void configureNavAccountSettingsButton(Button button, User currentUser) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToAccountSettings(currentUser);
			}
		});
	}

	/**
	 * Configures button that will switch to the Community scene.
	 * 
	 * @param button
	 * @param currentUser
	 */
	static void configureNavCommunityButton(Button button, User currentUser) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToCommunity(currentUser);
			}
		});
	}
}
