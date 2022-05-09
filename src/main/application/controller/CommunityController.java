package main.application.controller;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import main.application.SceneSwitcher;
import main.application.model.AccountSettingsModel;
import main.application.model.Conversation;
import main.application.model.Event;
import main.application.model.EventsModel;
import main.application.model.FacultyAccountSettingsModel;
import main.application.model.Following;
import main.application.model.FollowingModel;
import main.application.model.Message;
import main.application.model.MessagingModel;
import main.application.model.Restaurant;
import main.application.model.RestaurantReviewsModel;
import main.application.model.User;

/**
 * Allows users to add others as friends (as long as their friends are of the
 * same user type), unfriend people, chat with friends, and see who is following
 * them. In the code below, a friend is someone who the current user is
 * following, and a follower is someone following the current user. The words
 * "follower" and "following" might be used interchangeably.
 */
public class CommunityController extends AbstractControllerWithNav {
	private User currentUser;
	private FollowingModel followingModel;
	private User otherUserInConversation;
	private MessagingModel messagingModel;
	private RestaurantReviewsModel restaurantReviewsModel;
	private List<Restaurant> restaurants;

	@FXML
	private ListView<Following> FriendsListView;
	@FXML
	private ListView<Following> FollowingListView;
	@FXML
	private ListView<User> AddFriendsListView;
	@FXML
	private ListView<Conversation> SelectConversationListView;
	@FXML
	private ListView<Message> MessagesListView;
	@FXML
	private Label MessagesHeaderText;
	@FXML
	private TextField MessageField;
	@FXML
	private TextArea SelectedUserInfoTextArea;

	/**
	 * Converts Following object to String to be used in FriendsListView and
	 * FollowingListView.
	 */
	private class FollowingConverter extends StringConverter<Following> {
		@Override
		public Following fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(Following following) {
			// Returns name of friend or follower based on whether or not the person being
			// followed is the current user.
			if (following.getTo().getId() == currentUser.getId()) {
				return following.getFrom().getFullName();
			}
			return following.getTo().getFullName();
		}
	}

	/**
	 * Converts Following object to String to be used in AddFriendsListView.
	 */
	private class UserConverter extends StringConverter<User> {
		@Override
		public User fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(User otherUser) {
			return otherUser.getFullName();
		}
	}

	/**
	 * Cell in SelectConversationListView. Includes full name of user involved in
	 * conversation as well as the last time a messages was sent or received.
	 */
	private class ConversationCell extends ListCell<Conversation> {
		VBox vbox = new VBox();
		Text otherUsernameText = new Text();
		Text lastMessageTimeText = new Text();

		public ConversationCell() {
			super();
			vbox.getChildren().setAll(otherUsernameText, lastMessageTimeText);
		}

		@Override
		protected void updateItem(Conversation item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setGraphic(null);
			} else {
				otherUsernameText.setText(item.getOtherUser().getFullName());
				lastMessageTimeText.setText(item.getLastMessageTimeFormatted());
				setGraphic(vbox);
			}
		}
	}

	/**
	 * Cell in MessagesListView. Includes the sender of the message, the time that
	 * the message was sent, and the content of the message.
	 */
	private class MessageCell extends ListCell<Message> {
		VBox vbox = new VBox();
		Text senderUsernameText = new Text();
		Text timeSentText = new Text();
		Text contentText = new Text();

		public MessageCell() {
			super();
			vbox.getChildren().setAll(senderUsernameText, timeSentText, contentText);
		}

		@Override
		protected void updateItem(Message item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setGraphic(null);
			} else {
				senderUsernameText.setText(item.getSender().getFullName());
				timeSentText.setText(item.getTimeSentFormatted());
				contentText.setText(item.getContent());
				setGraphic(vbox);
			}
		}
	}

	/**
	 * Configures controller.
	 */
	@Override
	public void configure(User currentUser) {
		super.configure(currentUser);
		this.currentUser = currentUser;
		SceneSwitcher.getPrimaryStage().setTitle("Community");

		followingModel = new FollowingModel(currentUser);
		messagingModel = new MessagingModel(currentUser);
		restaurantReviewsModel = new RestaurantReviewsModel(currentUser);

		SelectConversationListView.setCellFactory(arg -> new ConversationCell());

		MessagesListView.setCellFactory(arg -> new MessageCell());

		refresh();
	}

	/**
	 * Refreshes all data. Gets lists of friends, followers, and other users. Also
	 * refreshes conversation and message lists and clears text fields.
	 */
	private void refresh() {
		otherUserInConversation = null;
		SelectConversationListView.getItems().clear();
		MessagesListView.getItems().clear();

		ObservableList<Following> following = followingModel.getFollowing();
		ObservableList<Following> friends = followingModel.getFriends();
		ObservableList<User> potentialFriends = followingModel.getOtherUsers();

		FriendsListView.getSelectionModel().clearSelection();
		FriendsListView.setItems(friends);
		FriendsListView.setCellFactory(ComboBoxListCell.forListView(new FollowingConverter(), friends));
		FollowingListView.setItems(following);
		FollowingListView.setCellFactory(ComboBoxListCell.forListView(new FollowingConverter(), following));
		AddFriendsListView.setItems(potentialFriends);
		AddFriendsListView.setCellFactory(ComboBoxListCell.forListView(new UserConverter(), potentialFriends));

		refreshConversationsListView();
		refreshMessagesListView();
		refreshRestaurantsArray();
	}

	/**
	 * Refreshes list of conversations.
	 */
	private void refreshConversationsListView() {
		SelectConversationListView.setItems(messagingModel.getConversations());
	}

	/**
	 * Refreshes list of messages to/from a specific user. If no user is currently
	 * selected, clears list of messages. Also clears text field and sets header
	 * text appropriately.
	 */
	private void refreshMessagesListView() {
		MessageField.clear();
		if (otherUserInConversation != null) {
			MessagesListView.setItems(messagingModel.getMessagesOfConversation(otherUserInConversation));
			MessagesListView.scrollTo(MessagesListView.getItems().size() - 1);
			MessagesHeaderText.setText(otherUserInConversation.getFullName());
		} else {
			MessagesListView.getItems().clear();
			MessagesHeaderText.setText("No User Selected");
		}
	}

	private void refreshRestaurantsArray() {
		restaurants = restaurantReviewsModel.getRestaurantsAsArray();
	}

	@FXML
	private void handleSelectFriend() {
		Following following = FriendsListView.getSelectionModel().getSelectedItem();
		if (following != null) {
			updateSelectedUserInfoTextArea(following.getTo());
		}
	}

	@FXML
	private void handleSelectFollowing() {
		Following following = FollowingListView.getSelectionModel().getSelectedItem();
		if (following != null) {
			updateSelectedUserInfoTextArea(following.getFrom());
		}
	}

	@FXML
	private void handleSelectOtherUser() {
		User otherUser = AddFriendsListView.getSelectionModel().getSelectedItem();
		if (otherUser != null) {
			updateSelectedUserInfoTextArea(otherUser);
		}
	}

	/**
	 * Adds selected friend.
	 */
	@FXML
	private void handleAddFriend() {
		User user = AddFriendsListView.getSelectionModel().getSelectedItem();
		if (user != null) {
			user.addAsFriend(currentUser);
			refresh();
		}
	}

	/**
	 * Unfriends selected friend.
	 */
	@FXML
	private void handleUnfriend() {
		Following friend = FriendsListView.getSelectionModel().getSelectedItem();
		if (friend != null) {
			followingModel.unfriend(friend.getTo());
			refresh();
		}
	}

	/**
	 * Either starts conversation with selected friend or
	 */
	@FXML
	private void handleStartConversation() {
		Following friend = FriendsListView.getSelectionModel().getSelectedItem();
		if (friend != null) {
			otherUserInConversation = friend.getTo();
			refreshMessagesListView();
			updateSelectedUserInfoTextArea(otherUserInConversation);
		}
	}

	/**
	 * Indicates that current user is now chatting with user associated with
	 * selected conversation. Also fetches list of messages associated with selected
	 * conversation.
	 */
	@FXML
	private void handleSelectConversation() {
		Conversation selectedConversation = SelectConversationListView.getSelectionModel().getSelectedItem();
		if (selectedConversation != null) {
			otherUserInConversation = selectedConversation.getOtherUser();
			refreshMessagesListView();
			updateSelectedUserInfoTextArea(otherUserInConversation);
		}
	}

	/**
	 * Submits new message from contents of text field.
	 */
	@FXML
	private void handleSubmitMessage() {
		if (otherUserInConversation != null) {
			messagingModel.submitMessage(otherUserInConversation, MessageField.getText());
			refreshConversationsListView();
			refreshMessagesListView();
		}
	}

	/**
	 * Updates the contents of the user info text area, which provides details on
	 * the currently selected user (whichever user will selected last in the
	 * ListViews).
	 * 
	 * @param selectedUser user selected from one of the ListViews
	 */
	private void updateSelectedUserInfoTextArea(User selectedUser) {
		StringBuilder sb = new StringBuilder(String.format("Information about %s%n%n", selectedUser.getFullName()));

		int favoriteRestaurantId;
		String favoriteFoods;
		String interests;
		String availability;

		if (selectedUser.getUserType() == User.UserType.STUDENT) {
			AccountSettingsModel settings = new AccountSettingsModel(selectedUser);
			int academicYear = settings.getAcademicYear();
			String major = settings.getMajor();
			favoriteRestaurantId = settings.getFavoriteRestaurantId();
			favoriteFoods = settings.getFavoriteFoods();
			interests = settings.getInterests();
			availability = settings.getAvailability();

			// Appends academic year and major of selected user to StringBuilder object.
			sb.append(String.format("Academic Year: %d%nMajor: %s%n", academicYear, major));
		} else if (selectedUser.getUserType() == User.UserType.FACULTY) {
			FacultyAccountSettingsModel settings = new FacultyAccountSettingsModel(selectedUser);
			favoriteRestaurantId = settings.getFavoriteRestaurantId();
			favoriteFoods = settings.getFavoriteFoods();
			interests = settings.getInterests();
			availability = settings.getAvailability();
		} else {
			return;
		}

		// Appends favorite restaurant (or no favorite restaurant) of selected user to
		// StringBuilder
		// object.
		sb.append("Favorite restaurant: ");

		if (favoriteRestaurantId != 0) {
			Restaurant favoriteRestaurant = restaurants.get(favoriteRestaurantId - 1);
			sb.append(favoriteRestaurant.getName());
		} else {
			sb.append("No favorite restaurant");
		}

		// Appends favorite food, availability, and interests of selected user to
		// StringBuilder object.
		sb.append(String.format("%nFavorite Food: %s%nAvailability: %s%nInterests: %s", favoriteFoods, availability,
				interests));

		// Determines if selected user is following current user.
		boolean isFollowing = FollowingListView.getItems().stream()
				.filter(following -> selectedUser.equals(following.getFrom())).anyMatch(following -> true);

		// Only shows events that selected user has attended or will attend if selected
		// user is following current
		// user.
		if (isFollowing) {
			// Gets this month's events.
			ObservableList<Event> events = new EventsModel(selectedUser).getEventsOfMonth(YearMonth.now());

			// Joins events dates and descriptions (that the selected user has attended or
			// will attend) into one string, with each event separated by a newline
			// character. Only includes events that selected user has attended or will
			// attend.
			String eventsJoined = events.stream().filter(Event::willAttend)
					.map(event -> event.getDateFormatted() + " - " + event.getDescription())
					.collect(Collectors.joining("\n"));

			// Only prints generated string if the selected user will attend any events this
			// month.
			if (!eventsJoined.isEmpty()) {
				// Appends the event dates and descriptions to the StringBuilder object.
				sb.append(String.format("%n%nEvents that %s has attended or will attend this month:%n%s",
						selectedUser.getFullName(), eventsJoined));
			} else {
				sb.append(String.format("%n%nThere are no events that %s has attended or will attend this month.",
						selectedUser.getFullName()));
			}
		}

		SelectedUserInfoTextArea.setText(sb.toString());
	}
}
