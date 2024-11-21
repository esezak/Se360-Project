package com.esezak.server.MovieLookup;

import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.ContentType;
import com.esezak.server.MovieLookup.TVDB.TVDBSearcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
    }
    static class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (InputStream input = socket.getInputStream();//gets input from client
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());){

                String filmName = reader.readLine();
                ArrayList<Content> data = TVDBSearcher.queryFromTVDB(filmName, ContentType.movie);
                output.writeObject(data);
                System.out.println("Data sent to client");
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}

