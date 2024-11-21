package com.esezak.client.ConnectionManager;
import java.io.*;
import java.net.*;
public class Connector {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(hostname, port)) {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Send a film name to the server
            String filmName = "Breaking Bad";
            writer.println(filmName);

            // Read the response from the server
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Server response: " + response);
            }
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}
