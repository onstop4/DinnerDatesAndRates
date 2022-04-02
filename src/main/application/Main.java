package main.application;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.controller.SignInController;

public class Main extends Application {
	Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("loginui.fxml"));
			Scene scene = new Scene((Parent) loader.load());
			SignInController controller = loader.getController();
			controller.setStage(primaryStage);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
//https://www.dataquest.io/blog/sql-commands/
