package main.application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Assists in querying and creating new messages.
 */
public class MessagingModel {
	private User currentUser;

	/**
	 * Constructs new object.
	 * 
	 * @param currentUser signed-in user
	 */
	public MessagingModel(User currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Returns list of conversations.
	 * 
	 * @return
	 */
	public ObservableList<Conversation> getConversations() {
		ObservableList<Conversation> list = FXCollections.observableArrayList();
		Set<Integer> foundUserIds = new HashSet<>();
		int currentUserId = currentUser.getId();

		try (Connection conn = Database.getConnection()) {
			// Selects the user id and full name of the most recent message of the
			// conversations that the current user has had with friends.
			String statement = "select sender_id, UserSender.full_name as sender_name, recipient_id, UserRecipient.full_name as recipient_name, time_sent from Message inner join User as UserSender on sender_id=UserSender.user_id inner join User as UserRecipient on recipient_id=UserRecipient.user_id where (message_id in (select max(message_id) from Message where sender_id = ? group by recipient_id) or message_id in (select max(message_id) from Message where recipient_id = ? group by sender_id)) and (sender_id in (select to_id from Following where from_id = ?) or recipient_id in (select to_id from Following where from_id = ?)) order by message_id desc";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUserId);
			stmt.setInt(2, currentUserId);
			stmt.setInt(3, currentUserId);
			stmt.setInt(4, currentUserId);

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				// Since the most recent messages in some conversations might be the current
				// user's own messages to a friend, this ensures that the user id and full name
				// of that friend is used instead the user's own id and full name.
				int foundUserId = rs.getInt("sender_id");
				String foundUserFullName = rs.getString("sender_name");
				if (foundUserId == currentUserId) {
					foundUserId = rs.getInt("recipient_id");
					foundUserFullName = rs.getString("recipient_name");
				}

				// Prevents duplicates.
				if (foundUserIds.add(foundUserId)) {
					list.add(new Conversation(new User(foundUserId, null, foundUserFullName, currentUser.getUserType()),
							rs.getTimestamp("time_sent").toLocalDateTime()));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Returns list of messages in conversation with other user.
	 * 
	 * @param otherUser
	 * @return
	 */
	public ObservableList<Message> getMessagesOfConversation(User otherUser) {
		ObservableList<Message> list = FXCollections.observableArrayList();

		try (Connection conn = Database.getConnection()) {
			String statement = "select message_id, sender_id, content, time_sent from Message where (sender_id = ? and recipient_id = ?) or (sender_id = ? and recipient_id = ?) order by message_id";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, otherUser.getId());
			stmt.setInt(3, otherUser.getId());
			stmt.setInt(4, currentUser.getId());

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				int senderId = rs.getInt("sender_id");
				User sender = null;
				if (senderId == currentUser.getId()) {
					sender = currentUser;
				} else if (senderId == otherUser.getId()) {
					sender = otherUser;
				}

				list.add(new Message(rs.getInt("message_id"), sender, rs.getString("content"),
						rs.getTimestamp("time_sent").toLocalDateTime()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * Adds new message from current user to target user to database.
	 * 
	 * @param targetUser
	 * @param message
	 */
	public void submitMessage(User targetUser, String message) {
		try (Connection conn = Database.getConnection()) {
			String statement = "insert into Message (sender_id, recipient_id, content, time_sent) values (?, ?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(statement);
			stmt.setInt(1, currentUser.getId());
			stmt.setInt(2, targetUser.getId());
			stmt.setString(3, message);
			stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
