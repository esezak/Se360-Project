package com.esezak.server.ConnectionManager;

import com.esezak.client.ConnectionManager.Requests.Request;
import com.esezak.client.ConnectionManager.Requests.RequestType;
import com.esezak.server.ConnectionManager.Responses.Response;
import com.esezak.server.Database.Init.ConnectDB;
import com.esezak.server.Database.Management.DB;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionThread extends Thread {
    private Socket connection;
    private ObjectOutputStream sendChannel;

    private ObjectInputStream receiveChannel;
    private Request currentRequest;
    private Response currentResponse;
    private boolean loggedIn = false;
    private boolean terminate = false;
    private String username;
    private String password;
    ConnectDB connectDB = new ConnectDB();
    public ConnectionThread(Socket connection) {
        this.connection = connection;
    }

    //TODO make it handle different requests
    public void run() {
        try{
            sendChannel = new ObjectOutputStream(connection.getOutputStream());
            receiveChannel = new ObjectInputStream(connection.getInputStream());
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
            case RequestType.GET_USER_WATCHLIST -> handleWatchlistRequest(request);
            case RequestType.ADD_MOVIE_TO_WATCHLIST -> handleAddMovieToWatchlist(request);
            default -> false;
        };
    }

    //TODO make auth system connected to DB (Bora)
    private boolean handleLoginRequest(Request request) throws IOException {
        System.out.println("Client Logged in");
        JSONObject loginData = new JSONObject(request.getData());
        this.password = loginData.getString("password");
        this.username = loginData.getString("username");
        try(Connection connection = connectDB.getConnection()) {
            if(connection != null && DB.verifyPassword(connection,username,password)){
                if(!loggedIn){
                    loggedIn = true;
                    sendOkResponse();
                    System.out.println("Logged in");
                    return true;
                } else {
                    System.err.println("User already logged in");
                    sendErrorResponse();
                    return false;
                }
            } else {
                System.err.println("Invalid password or username");
                sendErrorResponse();
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        } finally {
            connectDB.closeConnection();
        }
    }

    private boolean handleLogoutRequest() throws IOException {
        if (loggedIn) {
            loggedIn = false;
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
    //TODO
    private boolean handleWatchlistRequest(Request request) throws IOException {
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
    //TODO
    private boolean handleAddMovieToWatchlist(Request request) throws IOException {
        if (!loggedIn) {
            System.err.println("User not logged in");
            sendErrorResponse();
            return false;
        }
        JSONObject requestData = new JSONObject(request.getData());
        String movieId;
        movieId = requestData.getString("movie_id");
        String query = "INSERT INTO Watchlist (username, movie_id, date_added, user_rating, status) "
                + "VALUES (?, ?, datetime('now'), NULL, 'Watching')";
        try (Connection connection = connectDB.getConnection()) {
            if (connection != null) {
                if (DB.isMovieInWatchlist(connection, username, movieId)) {
                    System.err.println("Movie is already in the watchlist");
                    sendErrorResponse();
                    return false;
                }
                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    pstmt.setString(2, movieId);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
            sendErrorResponse();
            return false;
        } finally {
            connectDB.closeConnection();
        }
        sendOkResponse();
        System.out.println("Movie added to watchlist");
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
    
}
