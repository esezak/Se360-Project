package com.esezak.server.MovieLookup.Content;

import java.io.Serializable;

public class Review implements Serializable {
    private String author;
    private int rating;// out of 0 - 10
    private String comment;
    public Review(String author, int rating, String comment) {
        this.author = author;
        this.rating = rating;
        this.comment = comment;
    }
    public Review(String author, int rating) {
        this.author = author;
        this.rating = rating;
        this.comment = "This user has not commented";
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
