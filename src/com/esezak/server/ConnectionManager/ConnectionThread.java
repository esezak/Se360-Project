package com.esezak.server.ConnectionManager;

import com.esezak.client.ConnectionManager.Requests.Request;
import com.esezak.client.ConnectionManager.Requests.RequestType;
import com.esezak.server.ConnectionManager.Responses.Response;
import com.esezak.server.Database.Management.DBConnection;
import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;
import com.esezak.server.MovieLookup.TVDB.TVDBSearcher;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.ArrayList;


public class ConnectionThread extends Thread {
    private Socket connection;
    private ObjectOutputStream sendChannel;
    private DBConnection dbConnection;
    private ObjectInputStream receiveChannel;
    private Request currentRequest;
    private Response currentResponse;
    private boolean loggedIn = false;
    private boolean terminate = false;
    private String username;
    private String password;
    public ConnectionThread(Socket connection) {
        this.connection = connection;
    }

    //TODO make it handle different requests
    public void run() {
        try{
            sendChannel = new ObjectOutputStream(connection.getOutputStream());
            receiveChannel = new ObjectInputStream(connection.getInputStream());
            dbConnection = new DBConnection();
            while (!terminate) {
                currentRequest = (Request) receiveChannel.readObject();
                handleRequest(currentRequest);
                if(currentResponse.getStatus()){
                    System.out.println("Request handled");
                }else{
                    System.err.println("Request not handled");
                }
            }
            } catch (IOException e) {
                e.printStackTrace();
                terminate = true;
            } catch (ClassNotFoundException e) {
                System.err.println("Could not recieve request");
                e.printStackTrace();
                terminate = true;
            }
    }

    private boolean handleRequest(Request request) throws IOException {
        return switch (request.getRequestType()) {
            case RequestType.LOGIN -> handleLoginRequest(request);
            case RequestType.LOGOUT -> handleLogoutRequest();
            case RequestType.DISCONNECT -> handleDisconnectRequest();
            case RequestType.GET_USER_WATCHLIST -> handleGetWatchlistRequest(request);
            case RequestType.ADD_MOVIE_TO_WATCHLIST -> handleAddMovieToWatchlist(request);
            case RequestType.RATE_MOVIE -> handleRateMovieRequest(request);
            case RequestType.SEARCH_MOVIE -> handleSearchMovie(request);
            //case RequestType.GET_MOVIE_INFORMATION ->

            default -> false;
        };
    }

    private boolean handleSearchMovie(Request request) throws IOException {
        JSONObject requestData = new JSONObject(request.getData());
        String movieName = requestData.getString("movie_name");
        try{
            ArrayList<Content> movies = TVDBSearcher.queryFromTVDB(movieName);
            currentResponse = new Response(true,movies);
            sendChannel.writeObject(currentResponse);
            return true;
        } catch (IOException e) {
            System.err.println("Could not search movie " + movieName);
            sendErrorResponse();
            return false;
        }
    }

    private boolean handleLoginRequest(Request request) throws IOException {
        JSONObject loginData = new JSONObject(request.getData());   // parses json data
        this.password = loginData.getString("password");     // password field
        this.username = loginData.getString("username");     // username field
        try{
            if (connection != null && dbConnection.verifyPassword(username, password)) {
                if (!loggedIn) {
                    loggedIn = true;
                    sendOkResponse();
                    System.out.println(username +" logged in");
                    return true;
                } else {
                    System.err.println(username +" already logged in!");
                    sendErrorResponse();
                    return false;
                }
            } else {
                System.err.println("Invalid username or password for: "+username);
                sendErrorResponse();
                return false;
            }
        } catch (Exception e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        }
    }

    private boolean handleLogoutRequest() throws IOException {
        if (loggedIn) {
            loggedIn = false;
            this.password = null;
            this.username = null;
            System.out.println("Client Logged out");
            sendOkResponse();
            return true;
        }else{
            System.err.println("Client not logged in");
            sendErrorResponse();
            return false;
        }
    }
    private boolean handleDisconnectRequest() throws IOException {
        System.out.println("Client Disconnected");
        terminate = true;
        sendOkResponse();
        connection.close();
        return true;
    }

    /**
     * @param request
     * @return sets the current response status true if succesfull and adds Arraylist of movies in <br>
     * Response.movies
     * @throws IOException
     */
    //TODO Boraaaa user watchlisti döndüren kod
    private boolean handleGetWatchlistRequest(Request request) throws IOException {
        currentRequest = request;
        JSONObject requestData = new JSONObject(request.getData());
        String username = requestData.getString("username");
        if(!checkAuth(username)){
            sendErrorResponse();
            return false;
        }
        System.err.println("Not yet implemented");
        return false;
    }

    /**
     * checks if the user is logged in and if the movie request is already in the users watchlist. <br>
     * if the user is logged in and the moive is not in the watchlist add the movie to user watchlist
     * @param request
     * @return true if the movie is added successfully
     * @throws IOException
     */
    private boolean handleAddMovieToWatchlist(Request request) throws IOException {
        JSONObject requestData = new JSONObject(request.getData());
        String movieId = requestData.getString("movie_id");
        String username = requestData.getString("username");
        if(!checkAuth(username)){
            sendErrorResponse();
            return false;
        }
        String query = "INSERT INTO Watchlist (username, movie_id, date_added, user_rating, status) "
                + "VALUES (?, ?, datetime('now'), NULL, 'Watching')";
        try{
            if (dbConnection != null) {
                if (dbConnection.isMovieInWatchlist(username, movieId)) {
                    System.err.println("Movie is already in the watchlist");
                    sendErrorResponse();
                    return false;
                }
                try (PreparedStatement pstmt = dbConnection.getDbConnection().prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, movieId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        }
        sendOkResponse();
        System.out.println("Movie added to watchlist");
        return true;
    }

    /**
     * @param request
     * @return true if rate movie request is successfully completed else false
     * @throws IOException
     */
    private boolean handleRateMovieRequest(Request request) throws IOException {
        JSONObject reviewData = new JSONObject(request.getData());
        Review review = Review.parseJson(reviewData);//Automatically parses json to review object
        String username = reviewData.getString("username");
        String movieId = reviewData.getString("movie_id");

        if(!checkAuth(username)){
            sendErrorResponse();
            return false;
        }
        String comment = review.getComment();
        int rating = review.getRating();
        if (rating < 0 || rating > 10) {
            System.err.println("Invalid rating value for: "+rating);
            sendErrorResponse();
            return false;
        }
        String query = """
        INSERT INTO Reviews (username, movie_id, comment, user_rating, review_date)
        VALUES (?, ?, ?, ?, datetime('now'))
        ON CONFLICT(username, movie_id)
        DO UPDATE SET 
            comment = excluded.comment, 
            user_rating = excluded.user_rating, 
            review_date = excluded.review_date
    """;
        try {
            if (dbConnection != null) {
                try (PreparedStatement pstmt = dbConnection.getDbConnection().prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, movieId);
                    pstmt.setString(3, comment);
                    pstmt.setDouble(4, rating);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        }
        sendOkResponse();
        System.out.println("Movie rated");
        return true;
    }

    /**
     * Sends ok response if everything is in order
     * @throws IOException
     */
    private void sendOkResponse() throws IOException {
        System.out.println("Sent ok response");
        currentResponse = new Response(true);
        sendChannel.writeObject(currentResponse);
    }

    /**
     * Sends error response if something is not right
     * @throws IOException
     */
    private void sendErrorResponse() throws IOException {
        System.err.println("Sent error response");
        currentResponse = new Response(false);
        sendChannel.writeObject(currentResponse);
    }

    /**
     * @param username
     * @return false if logged-in username == request maker username and user is logged in else true
     * @throws IOException
     */
    private boolean checkAuth(String username) throws IOException {
        if (!loggedIn) {
            System.err.println("Not logged in");
            sendErrorResponse();
            return false;
        }
        if(!username.equals(this.username)){
            System.err.println("User logged in as: "+this.username+" but tried to add movie to watchlist as: "+username);
            return false;
        }
        return true;
    }
}
