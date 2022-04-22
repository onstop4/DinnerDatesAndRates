package main.application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.AccountSettingsController;
import main.application.controller.CommunityController;
import main.application.controller.FacultyAccountSettingsController;
import main.application.controller.HomeController;
import main.application.controller.RestaurantReviewsController;
import main.application.controller.SignInController;
import main.application.model.User;

public class SceneSwitcher {
	private static Stage primaryStage;
	private static FXMLLoader signInLoader;
	private static FXMLLoader homeLoader;
	private static FXMLLoader accountSettingsLoader;
	private static FXMLLoader facultyAccountSettingsLoader;
	private static FXMLLoader communityLoader;
	private static FXMLLoader restaurantReviewsLoader;

	private static Scene signInScene;
	private static Scene homeScene;
	private static Scene accountSettingsScene;
	private static Scene facultyAccountSettingsScene;
	private static Scene communityScene;
	private static Scene restaurantReviewsScene;

	public static void switchToSignIn() {
		SignInController controller = signInLoader.getController();
		controller.configure();
		primaryStage.setScene(signInScene);
		primaryStage.show();
	}

	public static void switchToHome(User currentUser) {
		HomeController controller = homeLoader.getController();
		controller.configure(currentUser);
		primaryStage.setScene(homeScene);
		primaryStage.show();
	}

	public static void switchToAccountSettings(User currentUser) {
		Scene scene = null;

		// Switches to accountSettingsScene or facultyAccountSettingsScene based on
		// whether the user is a student or a teacher, as indicated by the user's
		// UserType.
		if (currentUser.getUserType() == User.UserType.STUDENT) {
			scene = accountSettingsScene;
			AccountSettingsController controller = accountSettingsLoader.getController();
			controller.configure(currentUser);
		} else if (currentUser.getUserType() == User.UserType.FACULTY) {
			scene = facultyAccountSettingsScene;
			FacultyAccountSettingsController controller = facultyAccountSettingsLoader.getController();
			controller.configure(currentUser);
		}

		if (scene != null) {
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static void switchToCommunity(User currentUser) {
		CommunityController controller = communityLoader.getController();
		controller.configure(currentUser);
		primaryStage.setScene(communityScene);
		primaryStage.show();
	}

	public static void switchToRestaurantReviews(User currentUser) {
		RestaurantReviewsController controller = restaurantReviewsLoader.getController();
		controller.configure(currentUser);
		primaryStage.setScene(restaurantReviewsScene);
		primaryStage.show();
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
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

	static void setFacultyAccountSettingsLoader(FXMLLoader facultyAccountSettingsLoader) {
		SceneSwitcher.facultyAccountSettingsLoader = facultyAccountSettingsLoader;
		try {
			facultyAccountSettingsScene = new Scene((Parent) facultyAccountSettingsLoader.load());
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

	static void setRestaurantReviewsLoader(FXMLLoader restaurantReviewsLoader) {
		SceneSwitcher.restaurantReviewsLoader = restaurantReviewsLoader;
		try {
			restaurantReviewsScene = new Scene((Parent) restaurantReviewsLoader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
