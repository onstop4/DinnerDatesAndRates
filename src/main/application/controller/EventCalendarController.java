package main.application.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.application.SceneSwitcher;
import main.application.model.Event;
import main.application.model.EventsModel;
import main.application.model.User;

/**
 * Calendar that displays events and allows current user to confirm or deny
 * their attendance for events that will occur today or in the future.
 */
public class EventCalendarController extends AbstractController {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
	private static final Text[] headings = { new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
			new Text("Wednesday"), new Text("Thursday"), new Text("Friday"), new Text("Saturday") };

	private User currentUser;
	private LocalDate todaysDate;
	private EventsModel eventsModel;
	private List<Event> events;
	private YearMonth yearMonth;

	@FXML
	private GridPane CalendarGridPane;
	@FXML
	private Text CurrentMonthText;

	/**
	 * Configures controller.
	 */
	@Override
	public void configure(User currentUser) {
		this.currentUser = currentUser;
		eventsModel = new EventsModel(currentUser);
		yearMonth = YearMonth.now();

		refresh();
	}

	/**
	 * Refreshes calendar and updates current month text.
	 */
	private void refresh() {
		todaysDate = LocalDate.now();

		CurrentMonthText.setText(formatter.format(yearMonth));
		events = eventsModel.getEventsOfMonth(yearMonth);
		refreshCalendarGridPane();
	}

	/**
	 * Clears and refreshes calendar.
	 */
	private void refreshCalendarGridPane() {
		clearCalendarGridPane();

		LocalDate currentDate = yearMonth.atDay(1);
		LocalDate firstDayOfNextMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth()).plusDays(1);
		int numberOfWeeks = yearMonth.atEndOfMonth().get(WeekFields.ISO.weekOfMonth());

		// ISO-8601 requires at least 4 days of the week to be present before counting
		// the first week of the month, affecting the output of
		// WeekFields.ISO.weekOfMonth(). The code below increments numberOfWeeks if the
		// actual first week of the month has less than 4 days to get around this.
		if (currentDate.getDayOfWeek().getValue() > 4) {
			numberOfWeeks++;
		}

		// Adds first week to grid. Adds offset in case first day of month isn't Sunday.
		// Since getValue() will return 7 for Sunday, sets weekday to 0 for Sunday.
		int weekday = currentDate.getDayOfWeek().getValue() % 7;
		currentDate = addToCalendarGridPaneWithOffset(currentDate, 1, 7 - weekday, weekday);

		// Adds following weeks to grid. Makes sure to not go past end of month.
		for (int week = 2; week <= numberOfWeeks; week++) {
			int daysUntilEndOfWeek = (int) ChronoUnit.DAYS.between(currentDate, firstDayOfNextMonth);
			if (daysUntilEndOfWeek > 7) {
				daysUntilEndOfWeek = 7;
			}

			currentDate = addToCalendarGridPane(currentDate, week, daysUntilEndOfWeek);
		}
	}

	/**
	 * Clears all nodes in calendar and then adds the headings ("Sunday", "Monday",
	 * etc.).
	 */
	private void clearCalendarGridPane() {
		CalendarGridPane.getChildren().clear();
		CalendarGridPane.addRow(0, headings);
		for (Text heading : headings) {
			GridPane.setHalignment(heading, HPos.CENTER);
		}
	}

	/**
	 * Adds a week to the calendar.
	 * 
	 * @param startOfWeek the first day of the week
	 * @param currentWeek current week of the month
	 * @param daysInWeek  days in week
	 * @param offset      days before week begins
	 * @return first day of next week
	 */
	private LocalDate addToCalendarGridPane(LocalDate startOfWeek, int currentWeek, int daysInWeek) {
		return addToCalendarGridPaneWithOffset(startOfWeek, currentWeek, daysInWeek, 0);
	}

	/**
	 * Adds a week to the calendar. Starts adding days after an offset for the
	 * months that don't start on a Sunday.
	 * 
	 * @param startOfWeek the first day of the week
	 * @param currentWeek current week of the month
	 * @param daysInWeek  days in week
	 * @return first day of next week
	 */
	private LocalDate addToCalendarGridPaneWithOffset(LocalDate startOfWeek, int currentWeek, int daysInWeek,
			int offset) {
		Node[] weekNodes = new Node[offset + daysInWeek];
		LocalDate current = startOfWeek;

		for (int i = 0; i < offset; i++) {
			weekNodes[i] = new Text("");
		}

		for (int day = offset; day < daysInWeek + offset; day++) {
			weekNodes[day] = generateDate(current);
			current = current.plusDays(1);
		}

		CalendarGridPane.addRow(currentWeek, weekNodes);
		return current;
	}

	/**
	 * Returns a node containing the information about the specified date. This info
	 * includes the day of the month as well as buttons representing events. When a
	 * button is clicked, an alert is displayed. If the event has occurred in the
	 * past, then the alert will only describe the event. Otherwise, the alert will
	 * describe the event and prompt the user to confirm or deny their attendance. A
	 * check mark symbol on the button denotes whether or not the current user has
	 * indicated that they will attend the event.
	 * 
	 * @param date
	 * @return node with info about the specified date
	 */
	private Node generateDate(LocalDate date) {
		Text number = new Text(String.valueOf(date.getDayOfMonth()));
		VBox vbox = new VBox(number);
		vbox.setAlignment(Pos.TOP_CENTER);
		List<Node> vboxChildren = vbox.getChildren();

		events.stream().filter(event -> date.isEqual(event.getDate())).forEachOrdered(event -> {
			Button button = new Button();

			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					// Only lets current user confirm or deny attendance if event will occur today
					// or in the future.
					if (todaysDate.isAfter(event.getDate())) {
						displayAlertToDescribeEventOnly(event);
					} else {
						displayAlertToConfirmOrDenyAttendance(event, button);
					}
				}
			});

			editButton(event, button);
			vboxChildren.add(button);
		});

		return vbox;
	}

	/**
	 * Displays alert that describes event and allows current user to confirm or
	 * deny attendance.
	 * 
	 * @param event
	 * @param button
	 */
	private void displayAlertToConfirmOrDenyAttendance(Event event, Button button) {
		ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(AlertType.CONFIRMATION, event.getDescription(), yes, no);
		alert.setTitle("Confirm Attendance");
		if (event.willAttend()) {
			alert.setHeaderText("You have indicated that you will attend the following event on "
					+ event.getDateFormatted() + ". Will you still attend?");
		} else {
			alert.setHeaderText("Will you attend the following event on " + event.getDateFormatted() + "?");
		}

		Optional<ButtonType> result = alert.showAndWait();

		if (yes.equals(result.orElse(no))) {
			// Will only confirm attendance if "Yes" is clicked and event.willAttend()
			// returns false. This will prevent an error when trying to insert a row into
			// the EventAttendant table if a row indicating that the user will attend
			// already exists.
			if (!event.willAttend()) {
				event.confirmAttendance(currentUser);
			}
		} else {
			event.denyAttendance(currentUser);
		}

		editButton(event, button);
	}

	/**
	 * Displays alert that describes event.
	 * 
	 * @param event
	 */
	private void displayAlertToDescribeEventOnly(Event event) {
		Alert alert = new Alert(AlertType.INFORMATION, event.getDescription());
		alert.setTitle("Event Description");
		alert.setHeaderText("The following event occurred on " + event.getDateFormatted() + ".");
		alert.show();
	}

	/**
	 * Edits button text to indicate whether or not current user will attend event.
	 * 
	 * @param event
	 * @param button
	 */
	private void editButton(Event event, Button button) {
		if (event.willAttend()) {
			button.setText("✔ " + event.getDescription());
		} else {
			button.setText(event.getDescription());
		}
	}

	/**
	 * Advances a number of months based on the argument passed. If argument is
	 * negative, will go backward to previous months.
	 * 
	 * @param months
	 */
	private void viewMonth(int months) {
		yearMonth = yearMonth.plusMonths(months);
		refresh();
	}

	/**
	 * Go backward one month.
	 */
	@FXML
	private void handleViewPreviousMonth() {
		viewMonth(-1);
	}

	/**
	 * Go forward one month.
	 */
	@FXML
	private void handleViewNextMonth() {
		viewMonth(1);
	}

	/**
	 * Navigates to Home page.
	 */
	@FXML
	private void handleNavHome() {
		SceneSwitcher.switchToHome(currentUser);
	}

	/**
	 * Navigates to Sign-In page.
	 */
	@FXML
	private void handleNavSignOut() {
		SceneSwitcher.switchToSignIn();
	}
}
