package com.esezak.client.ConnectionManager;
import com.esezak.client.ConnectionManager.Requests.Request;
import com.esezak.server.ConnectionManager.Responses.Response;
import com.esezak.server.MovieLookup.Content.Content;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerConnection {
    private String host;
    private int port;
    private Socket connection = null;
    public ArrayList<Content> userWatchlist;
    public Request currentRequest;
    private ObjectOutputStream sendChannel;
    private ObjectInputStream receiveChannel;
    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
        currentRequest = new Request();
    }
    public boolean setConnection(){
        try{
            connection = new Socket(this.host,this.port);
            sendChannel = new ObjectOutputStream(connection.getOutputStream());
            receiveChannel = new ObjectInputStream(connection.getInputStream());

            return true;
        } catch (IOException e) {

            return false;
        }
    }


    /**
     * Request types: LOGIN, LOGOUT, DISCONNECT, GET_USER_WATCHLIST, ADD_MOVIE_TO_WATCHLIST
     * @return true if request is successful
     */
    private boolean sendSimpleRequest(Request request){
        currentRequest = request;
        Response response;
        try{
            //send request to server
            sendChannel.writeObject(currentRequest);

            //receive request from server
            response = (Response) receiveChannel.readObject();
            if(!response.getStatus()){
                System.err.println("Request failed");
                return false;
            }else{
                System.out.println(currentRequest.getRequestType()+" Successfully Sent");
            }
            if(response.getWatchlist()!=null){
                userWatchlist = response.getWatchlist();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Could not send "+ currentRequest.getRequestType() +" request");
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Received class not found");
            return false;
        }
    }

    private boolean sendAddToWatchListRequest(){
        Request request = currentRequest;
        Response response;
        try{
            //Send request to server
            sendChannel.writeObject(request);
            //receive request from server
            response = (Response) receiveChannel.readObject();
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
    public boolean sendDisconnectRequest(){
        currentRequest = new Request();
        currentRequest.disconnect();
        return sendSimpleRequest(currentRequest);
    }
    public boolean sendLogoutRequest(){
        currentRequest = new Request();
        currentRequest.logout();
        return sendSimpleRequest(currentRequest);
    }

    public static void main (String[] args) {//Connection tests
        ServerConnection serverConnection = new ServerConnection("localhost", 12345);
        Request request = new Request();
        request.login("asd","asd");
        serverConnection.sendSimpleRequest(request);
        request = new Request();
        request.logout();
        serverConnection.sendSimpleRequest(request);
        request = new Request();
        request.disconnect();
        serverConnection.sendSimpleRequest(request);
    }
}
