package com.esezak.server.ConnectionManager.Responses;

import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private boolean status;
    private ArrayList<Content> movies = null;
    private ArrayList<Review> reviews = null;
    private transient JSONObject json;
    private String data;
    private Content movie = null;
    public Response(boolean status) {
        this.status = status;
    }
    public Response(boolean status, Content movie, ArrayList<Review> reviews) {
        this.status = status;
        this.movie = movie;
        this.reviews = reviews;
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
        return reviews;
    }
    public Content getMovie() {
        return movie;
    }
    public void setReviews(ArrayList<Review> reviews) {
        JSONArray reviewArray = new JSONArray();
        JSONObject row = new JSONObject();
        for (Review review : reviews) {
            row.put("username", review.getUsername());
            row.put("rating", review.getRating());
            row.put("comment", review.getComment());
            reviewArray.put(row);
        }
        this.data = reviewArray.toString();
        System.out.println(data);
    }

    public String getData() {
        return data;
    }
    public static void main(String[] args){
        ArrayList<Review> reviews = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            reviews.add(new Review("ege",10,"bruh"));
        }
        Response response = new Response(true);
        response.setReviews(reviews);
    }
}
