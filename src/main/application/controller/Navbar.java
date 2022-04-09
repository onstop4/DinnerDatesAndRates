package main.application.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import main.application.SceneSwitcher;
import main.application.model.UserModel;

class Navbar {
	/**
	 * Configures all buttons in navigation bar.
	 * 
	 * @param userModel
	 * @param navHomeButton
	 * @param navAccountSettingsButton
	 * @param navCommunityButton
	 * @param navRestaurantReviewsButton
	 */
	static void configureAllNavButtons(UserModel userModel, Button navHomeButton, Button navAccountSettingsButton,
			Button navCommunityButton, Button navRestaurantReviewsButton) {
		configureNavHomeButton(navHomeButton, userModel);
		configureNavAccountSettingsButton(navAccountSettingsButton, userModel);
		configureNavCommunityButton(navCommunityButton, userModel);
	}

	/**
	 * Configures button that will switch to the Home scene.
	 * 
	 * @param button
	 * @param userModel
	 */
	static void configureNavHomeButton(Button button, UserModel userModel) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToHome(userModel);
			}
		});
	}

	/**
	 * Configures button that will switch to the Account Settings scene.
	 * 
	 * @param button
	 * @param userModel
	 */
	static void configureNavAccountSettingsButton(Button button, UserModel userModel) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToAccountSettings(userModel);
			}
		});
	}

	/**
	 * Configures button that will switch to the Community scene.
	 * 
	 * @param button
	 * @param userModel
	 */
	static void configureNavCommunityButton(Button button, UserModel userModel) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				SceneSwitcher.switchToCommunity(userModel);
			}
		});
	}
}
