package main.application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		SceneSwitcher.setPrimaryStage(primaryStage);
		SceneSwitcher.switchToSignIn();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
//https://www.dataquest.io/blog/sql-commands/
