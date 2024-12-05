package com.esezak.server.ConnectionManager;

import com.esezak.client.ConnectionManager.Requests.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionThread extends Thread {
    private Socket connection;
    private ObjectOutputStream sendChannel;

    private ObjectInputStream receiveChannel;
    private Request currentRequest;
    private boolean loggedIn = false;
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
            currentRequest = (Request) receiveChannel.readObject();



        } catch (IOException e) {
            System.err.println("Could not establish connection");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not recieve request");
        }
    }
    private boolean handleRequest(Request request) {

        return false;
    }
}
