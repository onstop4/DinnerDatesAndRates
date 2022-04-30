package main.application.model;

/**
 * Stores menu item info.
 */
public class MenuItem {
	private final int id;
	private final String name;
	private final String description;
	private final double price;

	/**
	 * Constructs new object.
	 * 
	 * @param id          id of menu item
	 * @param name        name of menu item
	 * @param description description of menu item
	 * @param price       price of menu item
	 */
	MenuItem(int id, String name, String description, double price) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
	}

	/**
	 * Returns id of menu item.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns name of menu item.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns description of menu item.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns price of menu item.
	 * 
	 * @return
	 */
	public double getPrice() {
		return price;
	}
}
