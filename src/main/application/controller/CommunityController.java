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
import main.application.model.OtherUser;
import main.application.model.UserModel;

public class CommunityController {
	private UserModel userModel;
	private FollowingModel followingModel;

	@FXML
	private Button NavHomeButton;
	@FXML
	private Button NavAccountSettingsButton;
	@FXML
	private Button NavCommunityButton;
	@FXML
	private Button NavRestaurantReviewsButton;

	@FXML
	private ListView<Following> FriendsListView;
	@FXML
	private ListView<Following> FollowingListView;
	@FXML
	private ListView<OtherUser> AddFriendsListView;
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
			if (following.getToID() == userModel.getId()) {
				return following.getFromName();
			}
			return following.getToName();
		}
	}

	public void configure(UserModel userModel) {
		this.userModel = userModel;
		SceneSwitcher.getPrimaryStage().setTitle("Community");
		Navbar.configureAllNavButtons(userModel, NavHomeButton, NavAccountSettingsButton, NavCommunityButton,
				NavRestaurantReviewsButton);

		followingModel = new FollowingModel(userModel);

		AddFriendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				MultipleSelectionModel<OtherUser> selectionModel = AddFriendsListView.getSelectionModel();
				if (selectionModel.getSelectedItem().addFriend()) {
					refresh();
				}
			}
		});

		refresh();
	}

	private void refresh() {
		ObservableList<Following> following = followingModel.getFollowing();
		ObservableList<Following> friends = followingModel.getFriends();
		ObservableList<OtherUser> potentialFriends = followingModel.getOtherUsers();

		FriendsListView.setItems(friends);
		FriendsListView.setCellFactory(ComboBoxListCell.forListView(new FollowingConverter(), friends));
		FollowingListView.setItems(following);
		FollowingListView.setCellFactory(ComboBoxListCell.forListView(new FollowingConverter(), following));
		AddFriendsListView.setItems(potentialFriends);
		AddFriendsListView.setCellFactory(ComboBoxListCell.forListView(new StringConverter<OtherUser>() {
			@Override
			public OtherUser fromString(String arg0) {
				return null;
			}

			@Override
			public String toString(OtherUser otherUser) {
				return otherUser.getFullName();
			}
		}, potentialFriends));
	}
}
