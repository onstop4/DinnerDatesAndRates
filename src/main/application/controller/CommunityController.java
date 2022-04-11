package main.application.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.util.StringConverter;
import main.application.SceneSwitcher;
import main.application.model.Following;
import main.application.model.FollowingModel;
import main.application.model.User;

public class CommunityController extends AbstractController {
	private User currentUser;
	private FollowingModel followingModel;

	@FXML
	private ListView<Following> FriendsListView;
	@FXML
	private ListView<Following> FollowingListView;
	@FXML
	private ListView<User> AddFriendsListView;
	@FXML
	private Button AddFriendButton;

	private class FollowingConverter extends StringConverter<Following> {
		@Override
		public Following fromString(String arg0) {
			return null;
		}

		@Override
		public String toString(Following following) {
			// Returns name of friend or follower based on whether or not the person being
			// followed is the current user.
			if (following.getToID() == currentUser.getId()) {
				return following.getFromName();
			}
			return following.getToName();
		}
	}

	public void configure(User currentUser) {
		super.configure(currentUser);
		this.currentUser = currentUser;
		SceneSwitcher.getPrimaryStage().setTitle("Community");

		followingModel = new FollowingModel(currentUser);

		AddFriendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				MultipleSelectionModel<User> selectionModel = AddFriendsListView.getSelectionModel();
				if (selectionModel.getSelectedItem().addAsFriend(currentUser)) {
					refresh();
				}
			}
		});

		refresh();
	}

	private void refresh() {
		ObservableList<Following> following = followingModel.getFollowing();
		ObservableList<Following> friends = followingModel.getFriends();
		ObservableList<User> potentialFriends = followingModel.getOtherUsers();

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
	}
}
