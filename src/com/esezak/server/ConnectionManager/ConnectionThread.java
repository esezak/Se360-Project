package com.esezak.server.ConnectionManager;

import com.esezak.client.ConnectionManager.Request;
import com.esezak.client.ConnectionManager.RequestType;
import com.esezak.server.Database.DBConnection;
import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;
import com.esezak.server.MovieLookup.TVDB.TVDBSearcher;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
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
            case RequestType.ADD_MOVIE_TO_WATCHLIST -> handleAddMovieToWatchlistRequest(request);
            case RequestType.RATE_MOVIE -> handleRateMovieRequest(request);
            case RequestType.SEARCH_MOVIE -> handleSearchMovieRequest(request);
            case RequestType.GET_MOVIE_INFORMATION -> handleGetMovieInformationRequest(request);
            case RequestType.UPDATE_WATCHLIST -> handleUpdateWatchlistRequest(request);

            default -> false;
        };
    }



    private boolean handleGetMovieInformationRequest(Request request) throws IOException {
        JSONObject json = new JSONObject(request.getData());
        String movieId = json.getString("movie_id");
        String query = """
        SELECT username, comment, user_rating, review_date
        FROM Reviews
        WHERE movie_id = ?
    """;
        ArrayList<Review> reviews = new ArrayList<>();
        try {
            if (dbConnection != null) {
                try (PreparedStatement pstmt = dbConnection.getDbConnection().prepareStatement(query)) {
                    pstmt.setString(1, movieId);
                    try (ResultSet rs = pstmt.executeQuery();) {
                        while (rs.next()) {
                            String username = rs.getString("username");
                            int rating = rs.getInt("user_rating");
                            String comment = rs.getString("comment");
                            System.out.println("Added: "+username + " " + rating + " " + comment);
                            reviews.add(new Review(username, rating, comment));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        }
        currentResponse = new Response(true,reviews,false);
        sendResponse();
        return true;
    }

    private boolean handleSearchMovieRequest(Request request) throws IOException {
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
    private boolean handleGetWatchlistRequest(Request request) throws IOException {
        JSONObject requestData = new JSONObject(request.getData());
        String username = requestData.getString("username");
        if (!checkAuth(username)) {
            sendErrorResponse();
            return false;
        }
        String query = """
        SELECT m.movie_id, m.title, w.date_added, w.user_rating, w.status
        FROM Watchlist w
        JOIN Movies m ON w.movie_id = m.movie_id
        WHERE w.username = ?
    """;
        JSONArray watchlistJson = new JSONArray();
        try {
            if (dbConnection != null) {
                try (PreparedStatement pstmt = dbConnection.getDbConnection().prepareStatement(query)) {
                    pstmt.setString(1, username);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            JSONObject movieJson = new JSONObject();
                            movieJson.put("movie_id", rs.getString("movie_id"));
                            movieJson.put("title", rs.getString("title"));
                            movieJson.put("date_added", rs.getString("date_added"));
                            movieJson.put("user_rating", rs.getDouble("user_rating"));
                            movieJson.put("status", rs.getString("status"));
                            watchlistJson.put(movieJson);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        }
        currentResponse = new Response(true,watchlistJson.toString());
        sendResponse();
        System.out.println("Watchlist returned");
        return true;
    }

    private boolean handleUpdateWatchlistRequest(Request request) throws IOException {
        JSONArray requestData = new JSONArray(request.getData());
        String username = requestData.getJSONObject(0).getString("username");
        if(!checkAuth(username)){
            sendErrorResponse();
            return false;
        }
        for(int i = 1; i < requestData.length(); i++){
            String movieId = (String) requestData.getJSONObject(i).get("movie_id");
            String title = (String) requestData.getJSONObject(i).get("title");
            String dateAdded = (String) requestData.getJSONObject(i).get("date_added");
            int userRating = (Integer) requestData.getJSONObject(i).get("user_rating");
            String status = (String) requestData.getJSONObject(i).get("status");
            String query = """
                            INSERT INTO Watchlist (username, movie_id, date_added, user_rating, status)
                            VALUES (?, ?, ?, ?, ?)
                            ON CONFLICT (username, movie_id)
                            DO UPDATE SET username = excluded.username,movie_id = excluded.movie_id, date_added = excluded.date_added, user_rating = excluded.user_rating, status = excluded.status
                            """;
            try{
                if (dbConnection != null) {
                    try (PreparedStatement pstmt = dbConnection.getDbConnection().prepareStatement(query)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, movieId);
                        pstmt.setString(3, dateAdded);
                        pstmt.setInt(4, userRating);
                        pstmt.setString(5, status);
                        pstmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                System.err.println("Database Error: " + e.getMessage());
                sendErrorResponse();
                return false;
            }
            System.out.println("Updated Watchlist Element");
        }
        sendOkResponse();
        return false;

    }


    /**
     * checks if the user is logged in and if the movie request is already in the users watchlist. <br>
     * if the user is logged in and the moive is not in the watchlist add the movie to user watchlist
     * @param request
     * @return true if the movie is added successfully
     * @throws IOException
     */
    private boolean handleAddMovieToWatchlistRequest(Request request) throws IOException {
        JSONObject requestData = new JSONObject(request.getData());
        String movieId = requestData.getString("movie_id");
        String username = requestData.getString("username");
        if(!checkAuth(username)){
            sendErrorResponse();
            return false;
        }
        String query = "INSERT INTO Watchlist (username, movie_id, date_added, user_rating, status) "
                + "VALUES (?, ?, datetime('now'), NULL, 'Plan to Watch')";
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
     * @throws IOException Sends the current response directly
     * this is for sending prepared responses. <br>
     * should not be used with error/ok responses.
     */
    private void sendResponse() throws IOException {
        System.out.println("Sent response");
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
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ConnectionThread(socket).start();//Start new Thread
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
    }
}
