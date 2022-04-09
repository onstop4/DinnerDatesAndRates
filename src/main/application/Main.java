package main.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		SceneSwitcher.setPrimaryStage(primaryStage);
		SceneSwitcher.setSignInLoader(new FXMLLoader(getClass().getResource("loginui.fxml")));
		SceneSwitcher.setHomeLoader(new FXMLLoader(getClass().getResource("HomePgUI.fxml")));
		SceneSwitcher.setAccountSettingsLoader(new FXMLLoader(getClass().getResource("AccountSettingsUI.fxml")));
		SceneSwitcher.setCommunityLoader(new FXMLLoader(getClass().getResource("CommunityUI.fxml")));

		SceneSwitcher.switchToSignIn();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
//https://www.dataquest.io/blog/sql-commands/
