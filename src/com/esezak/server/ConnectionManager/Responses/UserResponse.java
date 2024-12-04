package com.esezak.server.ConnectionManager.Responses;

import com.esezak.server.MovieLookup.Content.Content;

import java.util.ArrayList;

public class UserResponse extends Response{
    private ArrayList<Content> watchlist = null;
    public UserResponse(boolean status) {
        super(status);
    }
    public UserResponse(boolean status, ArrayList<Content> watchlist) {
        super(status);
        this.watchlist = watchlist;
    }
    public ArrayList<Content> getWatchlist() {
        return watchlist;
    }
}
