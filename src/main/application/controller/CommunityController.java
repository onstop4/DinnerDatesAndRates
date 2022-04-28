package main.application.controller;

import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import main.application.SceneSwitcher;
import main.application.model.Conversation;
import main.application.model.Following;
import main.application.model.FollowingModel;
import main.application.model.Message;
import main.application.model.MessagingModel;
import main.application.model.User;

public class CommunityController extends AbstractController {
	private User currentUser;
	private FollowingModel followingModel;
	private User otherUserInConversation;
	private MessagingModel messagingModel;

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

	public void configure(User currentUser) {
		super.configure(currentUser);
		this.currentUser = currentUser;
		SceneSwitcher.getPrimaryStage().setTitle("Community");

		followingModel = new FollowingModel(currentUser);
		messagingModel = new MessagingModel(currentUser);

		SelectConversationListView.setCellFactory(new Callback<ListView<Conversation>, ListCell<Conversation>>() {
			@Override
			public ListCell<Conversation> call(ListView<Conversation> arg0) {
				return new ConversationCell();
			}
		});
		SelectConversationListView.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				Conversation selectedConversation = SelectConversationListView.getSelectionModel().getSelectedItem();
				if (selectedConversation != null) {
					otherUserInConversation = selectedConversation.getOtherUser();
					refreshMessagesListView();
				}
			}
		});
		MessagesListView.setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
			@Override
			public ListCell<Message> call(ListView<Message> arg0) {
				return new MessageCell();
			}
		});

		refresh();
	}

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
		AddFriendsListView.setCellFactory(ComboBoxListCell.forListView(new StringConverter<User>() {
			@Override
			public User fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(User otherUser) {
				return otherUser.getFullName();
			}
		}, potentialFriends));

		refreshConversationsListView();
		refreshMessagesListView();
	}

	private void refreshConversationsListView() {
		SelectConversationListView.setItems(messagingModel.getConversations());
	}

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

	@FXML
	private void handleAddFriend() {
		MultipleSelectionModel<User> selectionModel = AddFriendsListView.getSelectionModel();
		if (selectionModel.getSelectedItem().addAsFriend(currentUser)) {
			refresh();
		}
	}

	@FXML
	private void handleUnfriend() {
		Following friend = FriendsListView.getSelectionModel().getSelectedItem();
		if (friend != null) {
			followingModel.unfriend(friend.getTo());
			refresh();
		}
	}

	@FXML
	private void handleStartConversation() {
		Following friend = FriendsListView.getSelectionModel().getSelectedItem();
		if (friend != null) {
			otherUserInConversation = friend.getTo();
			refreshMessagesListView();
		}
	}

	@FXML
	private void handleSubmitMessage() {
		if (otherUserInConversation != null) {
			messagingModel.submitMessage(otherUserInConversation, MessageField.getText());
			refreshConversationsListView();
			refreshMessagesListView();
		}
	}
}
