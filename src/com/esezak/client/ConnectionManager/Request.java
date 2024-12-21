package com.esezak.client.ConnectionManager;

import com.esezak.server.MovieLookup.Content.Review;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class Request implements Serializable {
    private RequestType requestType;
    private transient JSONObject json;
    private String data;
    public void login(String username, String Password){
        json = new JSONObject();
        requestType = RequestType.LOGIN;
        json.put("username", username);
        json.put("password", Password);
        data = json.toString();
    }
    public void logout(){
        requestType = RequestType.LOGOUT;
        json = null;
        data = null;
    }
    public void disconnect(){
        requestType = RequestType.DISCONNECT;
        json = null;
        data = null;
    }
    public void get_user_watchlist(String username){
        requestType = RequestType.GET_USER_WATCHLIST;
        json = new JSONObject();
        json.put("username", username);
        data = json.toString();
    }
    public void add_movie_to_watchlist(String movie_id, String username){
        json = new JSONObject();
        requestType = RequestType.ADD_MOVIE_TO_WATCHLIST;
        json.put("username", username);
        json.put("movie_id", movie_id);
        data = json.toString();
    }
    public void rate_movie(String movie_id, Review review){
        json = new JSONObject();
        requestType = RequestType.RATE_MOVIE;
        json.put("movie_id", movie_id);
        json.put("username", review.getUsername());
        json.put("rating", review.getRating());
        json.put("comment", review.getComment());
        data = json.toString();
    }
    public void search_movie(String movie_name){
        json = new JSONObject();
        requestType = RequestType.SEARCH_MOVIE;
        json.put("movie_name", movie_name);
        data = json.toString();
    }
    public void get_movie_information(String movie_id){
        json = new JSONObject();
        requestType = RequestType.GET_MOVIE_INFORMATION;
        json.put("movie_id", movie_id);
        data = json.toString();
    }
    public void update_watchlist(String movie_data){
        requestType = RequestType.UPDATE_WATCHLIST;
        data = movie_data;
        System.out.println(data);
    }
    public RequestType getRequestType() {
        return requestType;
    }
    public String getData() {
        return data;
    }
}
