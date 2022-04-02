package main.application.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.application.model.UserModel;

public class SignInController {
	private Stage stage;
	private boolean isRegistering;

	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Button switchModeButton;
	@FXML
	private Text errorText;

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setTitle("Sign in");
	}

	@FXML
	private void handleSubmit(ActionEvent event) {
		errorText.setText("");
		String username = usernameField.getText().strip();
		String password = passwordField.getText();

		if (username.isBlank() || password.isBlank()) {
			errorText.setText("Fields must not be blank.");
			return;
		}
		
		try {
			UserModel userModel;

			if (isRegistering) {
				userModel = UserModel.create_user_model(username, password);
			} else {
				userModel = UserModel.get_user_model(username, password);
				if (userModel == null) {
					errorText.setText("Incorrect username or password.");
					return;
				}
			}

			switchScene(userModel);
		} catch (java.sql.SQLIntegrityConstraintViolationException e) {
			errorText.setText("Error creating account. Username might be taken.");
		} catch (SQLException e) {
			e.printStackTrace();
			errorText.setText("Error communicating with database.");
		}
	}

	@FXML
	private void handleSwitchMode(ActionEvent event) {
		errorText.setText("");

		if (isRegistering) {
			isRegistering = false;
			stage.setTitle("Sign in");
			switchModeButton.setText("Sign into an existing account");
		} else {
			isRegistering = true;
			stage.setTitle("Register");
			switchModeButton.setText("Create a new account");
		}
	}

	private void switchScene(UserModel userModel) {
		// Will be used to switch to a scene for users who just signed in.
	}
}
