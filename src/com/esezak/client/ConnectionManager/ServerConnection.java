package com.esezak.client.ConnectionManager;
import com.esezak.client.ConnectionManager.Requests.Request;
import com.esezak.client.ConnectionManager.Requests.UserRequest;
import com.esezak.server.ConnectionManager.Responses.UserResponse;
import com.esezak.server.MovieLookup.Content.Content;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerConnection {
    private String host = "localhost";
    private int port = 12345;
    Socket connection = null;
    ArrayList<Content> userWatchlist;
    Request currentRequest;
    ObjectOutputStream sendChannel;
    ObjectInputStream recieveChannel;
    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }
    private boolean setConnection(){
        try{
            connection = new Socket(this.host,this.port);
            sendChannel = new ObjectOutputStream(connection.getOutputStream());
            recieveChannel = new ObjectInputStream(connection.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Could not connect to server");
            return false;
        }
    }

    /**
     * @param request recieves any type of request and handels requests based on type
     * @return returns true if request is successful
     */
    public boolean sendRequest(Request request){
        currentRequest = request;
        return switch (currentRequest.getRequestType()) {
            case LOGIN, LOGOUT, GET_USER_WATCHLIST -> sendSimpleRequest();
            case ADD_MOVIE_TO_WATCHLIST -> sendAddToWatchListRequest();
            default -> false;
        };
    }

    /**
     * @return true if login attempt is successful else false
     */
    private boolean sendSimpleRequest(){
        UserRequest request = (UserRequest) currentRequest;
        UserResponse response;
        try{
            //send request to server
            sendChannel.writeObject(request);

            //receive request from server
            response = (UserResponse) recieveChannel.readObject();
            if(!response.getStatus()){
                System.out.println("Request failed");
                return false;
            }
            if(response.getWatchlist()!=null){
                userWatchlist = response.getWatchlist();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Could not send login request");
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Received class not found");
            return false;
        }
    }

    private boolean sendAddToWatchListRequest(){
        UserRequest request = (UserRequest) currentRequest;
        UserResponse response;
        try{
            //Send request to server
            sendChannel.writeObject(request);
            //receive request from server
            response = (UserResponse) recieveChannel.readObject();
            if(!response.getStatus()){
                System.out.println("Request failed");
                return false;
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Could not send add to watchlist request");
            return false;
        }
    }
}
