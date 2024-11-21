package com.esezak.server.MovieLookup.Content;

public class Content {
    private String id;
    private String title;
    private String release_date;
    private String genres;
    private String director;
    private String overview;
    private String image_url;
    private float avg_rating;

    public Content(String id, String title, String image_url, String overview, String release_date, String genres, String director) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.overview = overview;
        this.release_date = release_date;
        this.genres = genres;
        this.director = director;
        this.avg_rating = 0;
    }
    public void displayContent() {
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Genres: " + genres);
        System.out.println("Director: " + director);
        System.out.println("Overview: " + overview);
        //System.out.println("Image URL: " + image_url);
        System.out.println("Rating: " + avg_rating);
        System.out.println("--------------------------");
    }

}
