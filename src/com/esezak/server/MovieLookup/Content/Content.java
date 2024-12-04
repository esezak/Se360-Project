package com.esezak.server.MovieLookup.Content;

import java.io.Serializable;
import java.util.ArrayList;

public class Content implements Serializable {
    private final String id;
    private final String title;
    private final String release_date;
    private final String genres;
    private final String director;
    private final String overview;
    private final String image_url;
    private float avg_rating;
    private int total_rating;
    private int nuber_of_reviews;

    public Content(String id, String title, String image_url, String overview, String release_date, String genres, String director) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.overview = overview;
        this.release_date = release_date;
        this.genres = genres;
        this.director = director;
        this.avg_rating = 0;
        this.nuber_of_reviews = 0;
        this.total_rating = 0;
    }
    public void displayContent() {
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Genres: " + genres);
        System.out.println("Director: " + director);
        System.out.println("Overview: " + overview);
        //System.out.println("Image URL: " + image_url);
        System.out.println("Rating: " + avg_rating);
        System.out.println("ReleaseDate: " + release_date);
        System.out.println("Image: " + image_url);
        System.out.println("--------------------------");
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getRelease_date() {
        return release_date;
    }
    public String getGenres() {
        return genres;
    }
    public String getDirector() {
        return director;
    }
    public String getOverview() {
        return overview;
    }
    public String getImage_url() {
        return image_url;
    }
    public float getAvg_rating() {
        return avg_rating;
    }
    private void calcAverageRating() {
        this.avg_rating += (float)this.total_rating/(float)this.nuber_of_reviews;
    }
    public void addRating(int newRating) {
        this.total_rating += newRating;
        this.nuber_of_reviews++;
        calcAverageRating();
    }
    public void updateRating(int oldRating, int newRating) {
        this.total_rating -= oldRating;
        this.total_rating += newRating;
        calcAverageRating();
    }
    public void deleteRating(int rating) {
        this.total_rating -= rating;
        this.nuber_of_reviews--;
        calcAverageRating();
    }

}
