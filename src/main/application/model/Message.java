package main.application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores message info.
 */
public class Message {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' K:mm a");

	private final int id;
	private final User sender;
	private final String content;
	private final LocalDateTime timeSent;

	/**
	 * Constructs new object.
	 * 
	 * @param id       id of message
	 * @param sender   sender of message
	 * @param content  content of message
	 * @param timeSent time that message was sent
	 */
	public Message(int id, User sender, String content, LocalDateTime timeSent) {
		this.id = id;
		this.sender = sender;
		this.content = content;
		this.timeSent = timeSent;
	}

	/**
	 * Returns id of message.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns sender of message.
	 * 
	 * @return
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * Returns content of message.
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Returns time message was sent.
	 * 
	 * @return
	 */
	public LocalDateTime getTimeSent() {
		return timeSent;
	}

	/**
	 * Returns time message was sent as a formatted string.
	 * 
	 * @return
	 */
	public String getTimeSentFormatted() {
		return timeSent.format(formatter);
	}
}
