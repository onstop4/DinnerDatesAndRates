package main.application.model;

public class Restaurant {
	private final int id;
	private final String name;
	private final String description;

	Restaurant(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
