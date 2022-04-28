package main.application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' K:mm a");

	private final int id;
	private final User sender;
	private final String content;
	private final LocalDateTime timeSent;

	public Message(int id, User sender, String content, LocalDateTime timeSent) {
		this.id = id;
		this.sender = sender;
		this.content = content;
		this.timeSent = timeSent;
	}

	public int getId() {
		return id;
	}

	public User getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

	public LocalDateTime getTimeSent() {
		return timeSent;
	}

	public String getTimeSentFormatted() {
		return timeSent.format(formatter);
	}
}
