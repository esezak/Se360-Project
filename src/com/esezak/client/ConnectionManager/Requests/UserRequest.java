package com.esezak.client.ConnectionManager.Requests;

import com.esezak.server.MovieLookup.Content.Review;

public class UserRequest extends Request {
    private String username;
    private String password;
    private String movieID;
    private Review review;

    /**
     * Constructor for sending simple request <br>
     * Request types: LOGIN, LOGOUT, GET_USER_WATCHLIST
     * @param username
     * @param password
     * @param requestType
     */
    public UserRequest(String username, String password, RequestType requestType) {
        super(requestType);
        this.username = username;
        this.password = password;
    }

    /**
     * Constructor for sending ADD_MOVIE_TO_WATCHLIST request
     * @param username
     * @param password
     * @param requestType
     * @param movieID
     */
    public UserRequest(String username, String password, RequestType requestType, String movieID) {
        super(requestType);
        this.username = username;
        this.password = password;
        this.movieID = movieID;
    }

    /**
     * Constructor for sending RATE_MOVIE request
     * @param username
     * @param password
     * @param requestType
     * @param movieID
     * @param review
     */
    public UserRequest(String username, String password, RequestType requestType, String movieID, Review review) {
        super(requestType);
        this.username = username;
        this.password = password;
        this.movieID = movieID;
        this.review = review;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMovieID(){
        return movieID;
    }

}
