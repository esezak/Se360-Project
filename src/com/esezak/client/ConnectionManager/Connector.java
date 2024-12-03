package com.esezak.client.ConnectionManager;
import com.esezak.server.MovieLookup.Content.Content;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Connector {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port)) {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ArrayList<Content> response;
            // Send a film name to the server
            Content content = new Content("a","a","a","a","a","a","a");
            String filmName = "Breaking Bad";

            output.writeObject(content);
            //response = (ArrayList<Content>) input.readObject();

            // Read the response from the server
            /*for (Content c : response) {
                c.displayContent();
            }*/
        } catch (UnknownHostException ex) {
            System.err.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex.getMessage());
        } /*catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }*/
    }
}
