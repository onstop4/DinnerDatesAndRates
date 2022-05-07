package main.application.controller;

import main.application.model.User;

/**
 * Abstract class for pages for signed-in users.
 */
public abstract class AbstractController {
	/**
	 * Configures controller.
	 * 
	 * @param currentUser signed-in user
	 */
	public abstract void configure(User currentUser);
}