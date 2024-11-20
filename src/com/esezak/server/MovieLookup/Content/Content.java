package com.esezak.server.MovieLookup.Content;

public class Content {
    private String id;
    private String title;
    private String image_url;
    private String overview;
    private String release_date;
    public Content(String id, String title, String image_url, String overview, String release_date) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.overview = overview;
        this.release_date = release_date;
    }

}
