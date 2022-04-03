package main.application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.HomeController;
import main.application.controller.SignInController;
import main.application.model.UserModel;

public class SceneSwitcher {
	static Stage primaryStage;
	static FXMLLoader signInLoader;
	static FXMLLoader homeLoader;

	public static void switchToSignIn() {
		try {
			Scene scene = new Scene((Parent) signInLoader.load());
			SignInController controller = signInLoader.getController();
			controller.setStage(primaryStage);

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
			controller.configure(primaryStage, userModel);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
