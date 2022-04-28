package main.application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Conversation {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' K:mm a");

	private final User otherUser;
	private final LocalDateTime lastMessageTime;

	public Conversation(User otherUser, LocalDateTime lastMessageTime) {
		this.otherUser = otherUser;
		this.lastMessageTime = lastMessageTime;
	}

	public User getOtherUser() {
		return otherUser;
	}

	public LocalDateTime getLastMessageTime() {
		return lastMessageTime;
	}

	public String getLastMessageTimeFormatted() {
		return lastMessageTime.format(formatter);
	}
}
