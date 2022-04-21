package main.application.controller;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.application.SceneSwitcher;
import main.application.model.User;

public class SignInController {
	private Stage stage;
	private boolean isRegistering;
	private boolean isRegisteringFaculty;

	@FXML
	private TextField UsernameField;
	@FXML
	private TextField PasswordField;
	@FXML
	private VBox RegistrationBox;
	@FXML
	private VBox FullNameBox;
	@FXML
	private TextField FullNameField;
	@FXML
	private VBox YearBox;
	@FXML
	private TextField YearField;
	@FXML
	private Button SwitchModeButton;
	@FXML
	private VBox UserTypeBox;
	@FXML
	private RadioButton UserTypeStudentRadioButton;
	@FXML
	private RadioButton UserTypeFacultyRadioButton;
	@FXML
	private ToggleGroup UserTypeToggleGroup;

	public void initialize() {
		UserTypeStudentRadioButton.setUserData("Student");
		UserTypeFacultyRadioButton.setUserData("Faculty");

		UserTypeToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			isRegisteringFaculty = isRegistering && "Faculty".equals(newToggle.getUserData());
			controlUserTypeStatus(isRegisteringFaculty);
		});
	}

	public void configure() {
		stage = SceneSwitcher.getPrimaryStage();
		stage.setTitle("Sign in");

		UsernameField.setText("");
		PasswordField.setText("");
		FullNameField.setText("");
		YearField.setText("");

		UserTypeStudentRadioButton.setSelected(true);

		isRegistering = false;
		isRegisteringFaculty = false;
		controlRegisteringStatus(isRegistering);
		controlUserTypeStatus(isRegisteringFaculty);
	}

	private void controlRegisteringStatus(boolean isRegistering) {
		RegistrationBox.setVisible(isRegistering);
		RegistrationBox.setManaged(isRegistering);

		if (isRegistering) {
			stage.setTitle("Register");
			SwitchModeButton.setText("Sign into an existing account");
		} else {
			stage.setTitle("Sign in");
			SwitchModeButton.setText("Create a new account");
		}
	}

	private void controlUserTypeStatus(boolean isRegisteringFaculty) {
		YearBox.setVisible(!isRegisteringFaculty);
		YearBox.setManaged(!isRegisteringFaculty);
	}

	@FXML
	private void handleSubmit(ActionEvent event) {
		Alert a = new Alert(AlertType.ERROR);

		String username = UsernameField.getText().strip();
		String password = PasswordField.getText();
		String fullName = FullNameField.getText().strip();

		if (username.isBlank() || password.isBlank() || (isRegistering && fullName.isBlank())) {
			a.setHeaderText("Bad Input");
			a.setContentText("Fields must not be left blank.");
			a.show();
			return;
		}

		try {
			User currentUser;

			if (isRegistering) {
				if (isRegisteringFaculty) {
					currentUser = User.create_faculty(username, password, fullName);
				} else {
					currentUser = User.create_student(username, password, fullName,
							Integer.parseInt(YearField.getText()));
				}
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
			} else {
				System.err.println("Couldn't sign in or register for unknown reason.");
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
		isRegistering = !isRegistering;

		controlRegisteringStatus(isRegistering);
	}

	private void switchScene(User currentUser) {
		SceneSwitcher.switchToHome(currentUser);
	}
}
