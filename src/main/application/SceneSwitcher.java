package main.application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.HomeController;
import main.application.model.UserModel;

public class SceneSwitcher {
	private static Stage primaryStage;
	private static FXMLLoader signInLoader;
	private static FXMLLoader homeLoader;

	public static void switchToSignIn() {
		try {
			Scene scene = new Scene((Parent) signInLoader.load());

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void switchToHome(UserModel userModel) {
		Scene scene;
		try {
			scene = new Scene((Parent) homeLoader.load());

			HomeController controller = homeLoader.getController();
			controller.configure(userModel);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	static void setPrimaryStage(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}

	static void setSignInLoader(FXMLLoader signInLoader) {
		SceneSwitcher.signInLoader = signInLoader;
	}

	static void setHomeLoader(FXMLLoader homeLoader) {
		SceneSwitcher.homeLoader = homeLoader;
	}
}
