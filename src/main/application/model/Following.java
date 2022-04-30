package main.application.model;

/**
 * Stores info on followings from one user to another.
 */
public class Following {
	private final User from;
	private final User to;

	/**
	 * Constructs new object.
	 * 
	 * @param from who is following
	 * @param to   the user being followed
	 */
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
