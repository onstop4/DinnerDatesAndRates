package main.application.model;

public class Following {
	private final User from;
	private final User to;

	public Following(User from, User to) {
		this.from = from;
		this.to = to;
	}

	public User getFrom() {
		return from;
	}

	public User getTo() {
		return to;
	}
}
