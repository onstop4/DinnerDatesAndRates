package main.application;

import java.io.IOException;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import main.application.model.Database;

public class Main extends Application {
	private static final String propertiesFilename = "settings.conf";

	@Override
	public void start(Stage primaryStage) {
		SceneSwitcher.setPrimaryStage(primaryStage);
		SceneSwitcher.setSignInLoader(new FXMLLoader(getClass().getResource("SignInPgUI.fxml")));
		SceneSwitcher.setHomeLoader(new FXMLLoader(getClass().getResource("HomePgUI.fxml")));
		SceneSwitcher.setAccountSettingsLoader(new FXMLLoader(getClass().getResource("AccountSettingsPgUI.fxml")));
		SceneSwitcher.setCommunityLoader(new FXMLLoader(getClass().getResource("CommunityPgUI.fxml")));
		SceneSwitcher.setRestaurantReviewsLoader(new FXMLLoader(getClass().getResource("RestaurantReviewsPgUI.fxml")));

		SceneSwitcher.switchToSignIn();
	}

	public static void main(String[] args) {
		Properties prop = new Properties();

		try {
			prop.load(Main.class.getClassLoader().getResourceAsStream(propertiesFilename));
			Database.setPassword(prop.getProperty("db_password"));
		} catch (IOException | NullPointerException e) {
			System.err.println("Cannot load properties file " + propertiesFilename);
		}

		launch(args);
	}

}
//https://www.dataquest.io/blog/sql-commands/
