package com.esezak.server.ConnectionManager.Responses;

import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private boolean status;
    private ArrayList<Content> watchlist = null;
    private ArrayList<Review> reviews = null;
    private Content movie = null;
    public Response(boolean status) {
        this.status = status;
    }
    public Response(boolean status, Content movie, ArrayList<Review> reviews) {
        this.status = status;
        this.movie = movie;
        this.reviews = reviews;
    }
    public Response(boolean status, ArrayList<Content> watchlist) {
        this.status = status;
        this.watchlist = watchlist;
    }
    public boolean getStatus() {
        return status;
    }

    public ArrayList<Content> getWatchlist() {
        return watchlist;
    }
    public ArrayList<Review> getReviews() {
        return reviews;
    }
    public Content getMovie() {
        return movie;
    }
}
