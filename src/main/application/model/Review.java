package main.application.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Review {
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	private final int id;
	private final String author;
	private final int rating;
	private final String comment;
	private final LocalDate dateSubmitted;

	Review(int id, String author, int rating, String comment, LocalDate dateSubmitted) {
		this.id = id;
		this.author = author;
		this.rating = rating;
		this.comment = comment;
		this.dateSubmitted = dateSubmitted;
	}

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public int getRating() {
		return rating;
	}

	public String getComment() {
		return comment;
	}

	public LocalDate getDateSubmitted() {
		return dateSubmitted;
	}

	public String getDateSubmittedFormatted() {
		return dateSubmitted.format(formatter);
	}
}
