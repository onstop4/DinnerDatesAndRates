package main.application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.AccountSettingsController;
import main.application.controller.HomeController;
import main.application.model.UserModel;

public class SceneSwitcher {
	private static Stage primaryStage;
	private static FXMLLoader signInLoader;
	private static FXMLLoader homeLoader;
	private static FXMLLoader accountSettingsLoader;

	private static Scene signInScene;
	private static Scene homeScene;
	private static Scene accountSettingsScene;

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

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static FXMLLoader getSignInLoader() {
		return signInLoader;
	}

	public static FXMLLoader getHomeLoader() {
		return homeLoader;
	}

	public static FXMLLoader getAccountSettingsLoader() {
		return accountSettingsLoader;
	}

	static void setPrimaryStage(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}

	static void setSignInLoader(FXMLLoader signInLoader) {
		SceneSwitcher.signInLoader = signInLoader;
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

	public static Scene getSignInScene() {
		return signInScene;
	}

	public static Scene getHomeScene() {
		return homeScene;
	}

	public static Scene getAccountSettingsScene() {
		return accountSettingsScene;
	}
}
