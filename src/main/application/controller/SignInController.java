package main.application.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
	private VBox yearBox;
	@FXML
	private TextField yearField;
	@FXML
	private Button switchModeButton;

	public void configure() {
		stage = SceneSwitcher.getPrimaryStage();
		stage.setTitle("Sign in");

		usernameField.setText("");
		passwordField.setText("");
		fullNameField.setText("");
		yearField.setText("");

		isRegistering = false;
		controlRegisteringStatus(isRegistering);
	}

	private void controlRegisteringStatus(boolean isRegistering) {
		fullNameBox.setVisible(isRegistering);
		fullNameBox.setManaged(isRegistering);

		yearBox.setVisible(isRegistering);
		yearBox.setManaged(isRegistering);

		if (isRegistering) {
			stage.setTitle("Register");
			switchModeButton.setText("Sign into an existing account");
		} else {
			stage.setTitle("Sign in");
			switchModeButton.setText("Create a new account");
		}
	}

	@FXML
	private void handleSubmit(ActionEvent event) {
		Alert a = new Alert(AlertType.ERROR);

		String username = usernameField.getText().strip();
		String password = passwordField.getText();
		String fullName = fullNameField.getText().strip();

		if (username.isBlank() || password.isBlank() || (isRegistering && fullName.isBlank())) {
			a.setHeaderText("Bad Input");
			a.setContentText("Fields must not be left blank.");
			a.show();
			return;
		}

		try {
			User currentUser;

			if (isRegistering) {
				currentUser = User.create_student(username, password, fullName, Integer.parseInt(yearField.getText()));
			} else {
				currentUser = User.get_user(username, password);
				if (currentUser == null) {
					a.setHeaderText("Bad Credentials");
					a.setContentText("Incorrect username or password.");
					a.show();
					return;
				}
			}

			if (currentUser != null) {
				switchScene(currentUser);
			}
		} catch (NumberFormatException e) {
			a.setHeaderText("Bad Input");
			a.setContentText("Please enter a valid integer for the \"Year\" field.");
			a.show();
		} catch (SQLIntegrityConstraintViolationException e) {
			a.setHeaderText("Username Taken");
			a.setContentText("Error creating account. Username might be taken.");
			a.show();
		} catch (SQLException e) {
			e.printStackTrace();
			a.setHeaderText("Database Communication Error");
			a.setContentText("Error communicating with database.");
			a.show();
		}
	}

	@FXML
	private void handleSwitchMode(ActionEvent event) {
		if (isRegistering) {
			isRegistering = false;
		} else {
			isRegistering = true;
		}

		controlRegisteringStatus(isRegistering);
	}

	private void switchScene(User currentUser) {
		SceneSwitcher.switchToHome(currentUser);
	}
}
