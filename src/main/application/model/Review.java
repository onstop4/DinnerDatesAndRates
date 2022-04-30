package main.application.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Stores review info.
 */
public class Review {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	private final int id;
	private final String author;
	private final int rating;
	private final String comment;
	private final LocalDate dateSubmitted;

	/**
	 * Constructs new object.
	 * 
	 * @param id            id of review
	 * @param author        author of review
	 * @param rating        rating of review
	 * @param comment       comment of review
	 * @param dateSubmitted date review was submitted
	 */
	Review(int id, String author, int rating, String comment, LocalDate dateSubmitted) {
		this.id = id;
		this.author = author;
		this.rating = rating;
		this.comment = comment;
		this.dateSubmitted = dateSubmitted;
	}

	/**
	 * Returns id of review.
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns author of review.
	 * 
	 * @return
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Returns rating of review.
	 * 
	 * @return
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * Returns comment of review.
	 * 
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Returns date when review was submitted.
	 * 
	 * @return
	 */
	public LocalDate getDateSubmitted() {
		return dateSubmitted;
	}

	/**
	 * Returns date when review was submitted formatted as a string.
	 * 
	 * @return
	 */
	public String getDateSubmittedFormatted() {
		return dateSubmitted.format(formatter);
	}
}
