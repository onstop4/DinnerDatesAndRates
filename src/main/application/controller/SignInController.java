package main.application.controller;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.application.SceneSwitcher;
import main.application.model.User;

public class SignInController {
	private Stage stage;
	private boolean isRegistering;

	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private VBox fullNameBox;
	@FXML
	private TextField fullNameField;
	@FXML
	private Button switchModeButton;
	@FXML
	private Text errorText;

	public void initialize() {
		stage = SceneSwitcher.getPrimaryStage();
		stage.setTitle("Sign in");
		controlFullNameBox(false);
	}

	private void controlFullNameBox(boolean state) {
		fullNameBox.setVisible(state);
		fullNameBox.setManaged(state);
	}

	@FXML
	private void handleSubmit(ActionEvent event) {
		errorText.setText("");
		String username = usernameField.getText().strip();
		String password = passwordField.getText();
		String fullName = fullNameField.getText().strip();

		if (username.isBlank() || password.isBlank() || (isRegistering && fullName.isBlank())) {
			errorText.setText("Fields must not be left blank.");
			return;
		}

		try {
			User currentUser;

			if (isRegistering) {
				currentUser = User.create_user(username, password, fullName);
			} else {
				currentUser = User.get_user(username, password);
				if (currentUser == null) {
					errorText.setText("Incorrect username or password.");
					return;
				}
			}

			if (currentUser != null) {
				switchScene(currentUser);
			}
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
			controlFullNameBox(false);
			switchModeButton.setText("Create a new account");
		} else {
			isRegistering = true;
			stage.setTitle("Register");
			controlFullNameBox(true);
			switchModeButton.setText("Sign into an existing account");
		}
	}

	private void switchScene(User currentUser) {
		SceneSwitcher.switchToHome(currentUser);
	}
}
