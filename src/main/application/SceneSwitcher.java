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

/**
 * Provides methods to access the stage or to switch to a different scene.
 * Scenes are loaded on every switch.
 */
public class SceneSwitcher {
	private static URL signInPage = SceneSwitcher.class.getResource("SignInPgUI.fxml");
	private static URL homePage = SceneSwitcher.class.getResource("HomePgUI.fxml");
	private static URL accountSettingsPage = SceneSwitcher.class.getResource("AccountSettingsPgUI.fxml");
	private static URL facultyAccountSettingsPage = SceneSwitcher.class.getResource("FacultyAccountSettingsPgUI.fxml");
	private static URL communityPage = SceneSwitcher.class.getResource("CommunityPgUI.fxml");
	private static URL restaurantReviewsPage = SceneSwitcher.class.getResource("RestaurantReviewsPgUI.fxml");
	private static URL eventCalendarPage = SceneSwitcher.class.getResource("EventCalendarPgUI.fxml");

	private static Stage primaryStage;

	/**
	 * Loads and returns scene from FXMLLoader object.
	 * 
	 * @param loader
	 * @return
	 * @throws IOException
	 */
	private static Scene getSceneFromLoader(FXMLLoader loader) throws IOException {
		return new Scene((Parent) loader.load());
	}

	/**
	 * Loads and returns scene (specifically for signed-in users) from FXMLLoader
	 * object. Configures controller in the process.
	 * 
	 * @param loader
	 * @param currentUser signed-in user
	 * @return
	 */
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

	/**
	 * Switches to scene.
	 * 
	 * @param scene
	 */
	private static void switchToScene(Scene scene) {
		if (scene != null) {
			primaryStage.setScene(scene);
			primaryStage.show();
		} else {
			System.err.println("Cannot switch to null scene");
		}
	}

	/**
	 * Switches to Sign-In page.
	 */
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

	/**
	 * Switches to Home page.
	 * 
	 * @param currentUser signed-in user
	 */
	public static void switchToHome(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(homePage), currentUser));
	}

	/**
	 * Switches to Account Settings page.
	 * 
	 * @param currentUser signed-in user
	 */
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

	/**
	 * Switches to Community page.
	 * 
	 * @param currentUser signed-in user
	 */
	public static void switchToCommunity(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(communityPage), currentUser));
	}

	/**
	 * Switches to Restaurant Reviews page.
	 * 
	 * @param currentUser signed-in user
	 */
	public static void switchToRestaurantReviews(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(restaurantReviewsPage), currentUser));
	}

	/**
	 * Switches to Event Calendar page.
	 * 
	 * @param currentUser signed-in user
	 */
	public static void switchToEventCalendar(User currentUser) {
		switchToScene(getSceneForSignedInUser(new FXMLLoader(eventCalendarPage), currentUser));
	}

	/**
	 * Returns primary stage.
	 * 
	 * @return primary stage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Sets primary stage.
	 * 
	 * @param primaryStage
	 */
	static void setPrimaryStage(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}
}
