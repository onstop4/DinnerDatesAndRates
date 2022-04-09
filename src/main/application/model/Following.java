package main.application.model;

public class Following {
	private final int fromID;
	private final String fromName;
	private final int toID;
	private final String toName;

	public Following(int fromID, String fromName, int toID, String toName) {
		this.fromID = fromID;
		this.fromName = fromName;
		this.toID = toID;
		this.toName = toName;
	}

	public int getFromID() {
		return fromID;
	}

	public String getFromName() {
		return fromName;
	}

	public int getToID() {
		return toID;
	}

	public String getToName() {
		return toName;
	}
}
