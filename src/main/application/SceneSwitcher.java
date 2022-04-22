package main.application;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.AbstractController;
import main.application.controller.SignInController;
import main.application.model.User;

public class SceneSwitcher {
	private static URL signInPage = SceneSwitcher.class.getResource("SignInPgUI.fxml");
	private static URL homePage = SceneSwitcher.class.getResource("HomePgUI.fxml");
	private static URL accountSettingsPage = SceneSwitcher.class.getResource("AccountSettingsPgUI.fxml");
	private static URL facultyAccountSettingsPage = SceneSwitcher.class.getResource("FacultyAccountSettingsPgUI.fxml");
	private static URL communityPage = SceneSwitcher.class.getResource("CommunityPgUI.fxml");
	private static URL restaurantReviewsPage = SceneSwitcher.class.getResource("RestaurantReviewsPgUI.fxml");

	private static Stage primaryStage;

	private static Scene getSceneForSignedInUser(FXMLLoader loader, User currentUser) {
		try {
			Scene scene = getSceneFromLoader(loader);
			AbstractController controller = loader.getController();
			controller.configure(currentUser);
			return scene;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Scene getSceneFromLoader(FXMLLoader loader) throws IOException {
		return new Scene((Parent) loader.load());
	}

	private static void switchToScene(Scene scene) {
		if (scene != null) {
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			System.err.println("Cannot switch to null scene");
		}
	}

	public static void switchToSignIn() {
		try {
			FXMLLoader loader = new FXMLLoader(signInPage);
			Scene scene = getSceneFromLoader(loader);
			SignInController controller = loader.getController();
			controller.configure();
			switchToScene(scene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void switchToHome(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(homePage), currentUser));
	}

	public static void switchToAccountSettings(User currentUser) {
		Scene scene = null;

		// Switches to accountSettingsScene or facultyAccountSettingsScene based on
		// whether the user is a student or a teacher, as indicated by the user's
		// UserType.
		if (currentUser.getUserType() == User.UserType.STUDENT) {
			scene = getSceneForSignedInUser(new FXMLLoader(accountSettingsPage), currentUser);
		} else if (currentUser.getUserType() == User.UserType.FACULTY) {
			scene = getSceneForSignedInUser(new FXMLLoader(facultyAccountSettingsPage), currentUser);
		}

		switchToScene(scene);
	}

	public static void switchToCommunity(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(communityPage), currentUser));
	}

	public static void switchToRestaurantReviews(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(restaurantReviewsPage), currentUser));
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	static void setPrimaryStage(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}
}
