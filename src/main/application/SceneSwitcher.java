package main.application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.AccountSettingsController;
import main.application.controller.CommunityController;
import main.application.controller.HomeController;
import main.application.model.UserModel;

public class SceneSwitcher {
	private static Stage primaryStage;
	private static FXMLLoader homeLoader;
	private static FXMLLoader accountSettingsLoader;
	private static FXMLLoader communityLoader;

	private static Scene signInScene;
	private static Scene homeScene;
	private static Scene accountSettingsScene;
	private static Scene communityScene;

	public static void switchToSignIn() {
		primaryStage.setScene(signInScene);
		primaryStage.show();
	}

	public static void switchToHome(UserModel userModel) {
		HomeController controller = homeLoader.getController();
		controller.configure(userModel);
		primaryStage.setScene(homeScene);
		primaryStage.show();
	}

	public static void switchToAccountSettings(UserModel userModel) {
		AccountSettingsController controller = accountSettingsLoader.getController();
		controller.configure(userModel);
		primaryStage.setScene(accountSettingsScene);
		primaryStage.show();
	}

	public static void switchToCommunity(UserModel userModel) {
		CommunityController controller = communityLoader.getController();
		controller.configure(userModel);
		primaryStage.setScene(communityScene);
		primaryStage.show();
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	static void setPrimaryStage(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}

	static void setSignInLoader(FXMLLoader signInLoader) {
		try {
			signInScene = new Scene((Parent) signInLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void setHomeLoader(FXMLLoader homeLoader) {
		SceneSwitcher.homeLoader = homeLoader;
		try {
			homeScene = new Scene((Parent) homeLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void setAccountSettingsLoader(FXMLLoader accountSettingsLoader) {
		SceneSwitcher.accountSettingsLoader = accountSettingsLoader;
		try {
			accountSettingsScene = new Scene((Parent) accountSettingsLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void setCommunityLoader(FXMLLoader communityLoader) {
		SceneSwitcher.communityLoader = communityLoader;
		try {
			communityScene = new Scene((Parent) communityLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
