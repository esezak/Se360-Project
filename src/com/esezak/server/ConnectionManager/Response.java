package com.esezak.server.ConnectionManager;

import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Response implements Serializable {
    private boolean status;
    private ArrayList<Content> movies = null;
    private transient JSONObject json;
    private transient JSONArray jsonArray;
    private String data;
    private transient Content movie = null;
    public Response(boolean status) {
        this.status = status;
    }
    public Response(boolean status, Content movie, ArrayList<Review> reviews) {
        this.status = status;
        this.movie = movie;
        setReviews(reviews,true);
    }
    public Response(boolean status, ArrayList<Review> reviews, boolean withMovies) {
        this.status = status;
        setReviews(reviews,withMovies);
    }
    public Response(boolean status, String data) {
        this.status = status;
        this.data = data;
    }
    public Response(boolean status, ArrayList<Content> movies) {
        this.status = status;
        this.movies = movies;
    }
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Content> getMovies() {
        return movies;
    }
    public ArrayList<Review> getReviews() {
        return Review.fromJsonArray(data);

    }
    public Content getMovie() {
        return movie;
    }
    private void setMovie(Content movie) {
        jsonArray = new JSONArray();
        jsonArray.put(movie.toJsonString());
    }

    public void setReviews(ArrayList<Review> reviews,boolean withMovie) {
        if(!withMovie){
            jsonArray = new JSONArray();
        }
        JSONObject row;
        for (Review review : reviews) {
            row = new JSONObject();
            row.put("username", review.getUsername());
            row.put("rating", review.getRating());
            row.put("comment", review.getComment());
            jsonArray.put(row);
        }
        this.data = jsonArray.toString();
        System.out.println(data);
    }

    public void tempReturnWatchlist(ArrayList<Content> movies) {//Should be converted to string
        JSONObject row = null;
        JSONArray movieArray = new JSONArray();
        for (Content movie : movies) {
            row = new JSONObject();
            row.put("movie_id", movie.getId());
            row.put("title", movie.getTitle());
            //---------------------------------Replace with info from db//TODO
            row.put("date_added", LocalDateTime.now().toString());
            row.put("rating", 5);
            row.put("status","watched");
            //------------------------------------------------
            movieArray.put(row);
            System.out.println("Row added: " + row.toString());
        }
        System.out.println("final array: " + movieArray.toString());
        this.data = movieArray.toString();
    }
    public JSONArray getWatchlist() {
        return new JSONArray(data);
    }

    public String getData() {
        return data;
    }
}
