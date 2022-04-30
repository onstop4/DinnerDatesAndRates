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

/**
 * Allows user to sign-in or register for a new account.
 */
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

	/**
	 * Sets up radio buttons for selecting either "Student" or "Faculty" when
	 * registering.
	 */
	public void initialize() {
		UserTypeStudentRadioButton.setUserData("Student");
		UserTypeFacultyRadioButton.setUserData("Faculty");

		UserTypeToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
			isRegisteringFaculty = isRegistering && "Faculty".equals(newToggle.getUserData());
			controlUserTypeStatus();
		});
	}

	/**
	 * Configures controller. Clears all fields and sets up the UI so that user is
	 * signing in by default.
	 */
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
		controlRegisteringStatus();
		controlUserTypeStatus();
	}

	/**
	 * Changes UI elements depending on whether the user is currently signing-in or
	 * registering.
	 */
	private void controlRegisteringStatus() {
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

	/**
	 * Changes UI elements depending on whether the user is currently registering as
	 * a student or as a faculty member.
	 */
	private void controlUserTypeStatus() {
		YearBox.setVisible(!isRegisteringFaculty);
		YearBox.setManaged(!isRegisteringFaculty);
	}

	/**
	 * Signs in using credentials or registers new user (using credentials) and then
	 * signs in. Displays error message if any field is left blank, if user
	 * credentials are bad, if username already exists, or if the "Year" field is
	 * not an integer.
	 * 
	 * @param event
	 */
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

	/**
	 * Alters UI from sign-in mode to registration mode (or vice-versa).
	 */
	@FXML
	private void handleSwitchMode() {
		isRegistering = !isRegistering;
		controlRegisteringStatus();
	}

	/**
	 * Switches to Home page.
	 * 
	 * @param currentUser signed-in user
	 */
	private void switchScene(User currentUser) {
		SceneSwitcher.switchToHome(currentUser);
	}
}
