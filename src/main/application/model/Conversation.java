package main.application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores info related to conversations that the current user has had with other
 * users.
 */
public class Conversation {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' K:mm a");

	private final User otherUser;
	private final LocalDateTime lastMessageTime;

	/**
	 * Constructs new object.
	 * 
	 * @param otherUser       user involved in conversation who is not the signed-in
	 *                        user
	 * @param lastMessageTime time of the last message that was sent or received
	 */
	public Conversation(User otherUser, LocalDateTime lastMessageTime) {
		this.otherUser = otherUser;
		this.lastMessageTime = lastMessageTime;
	}

	/**
	 * Returns other user.
	 * 
	 * @return
	 */
	public User getOtherUser() {
		return otherUser;
	}

	/**
	 * Returns time of the last message that was sent or received.
	 * 
	 * @return
	 */
	public LocalDateTime getLastMessageTime() {
		return lastMessageTime;
	}

	/**
	 * Returns a formatted string indicating the time of the last message that was
	 * sent or received.
	 * 
	 * @return
	 */
	public String getLastMessageTimeFormatted() {
		return lastMessageTime.format(formatter);
	}
}
