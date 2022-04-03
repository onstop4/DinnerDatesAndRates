package main.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Main extends Application {
	Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		SceneSwitcher.primaryStage = primaryStage;
		SceneSwitcher.signInLoader = new FXMLLoader(getClass().getResource("loginui.fxml"));
		SceneSwitcher.homeLoader = new FXMLLoader(getClass().getResource("HomePgUI.fxml"));

		SceneSwitcher.switchToSignIn();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
//https://www.dataquest.io/blog/sql-commands/
