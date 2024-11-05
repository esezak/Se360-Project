package com.esezak.server.MovieLookup;
import java.io.*;
import java.net.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

public class TVDBSearcher {
    private static final String API_KEY = "98d53837-4e95-4f64-b26d-e042758eab01";
    private static final String API_URL = "https://api.thetvdb.com/login";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            System.out.println("Server is listening on port 12345");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerThread(socket).start();
            }
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    static class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 OutputStream output = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(output, true)) {

                String filmName = reader.readLine();
                String filmInfo = getFilmInfo(filmName);
                writer.println(filmInfo);
            } catch (IOException ex) {
                System.out.println("Server exception: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        private String getFilmInfo(String filmName) {
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpGet request = new HttpGet(API_URL + "/search/series?name=" + URLEncoder.encode(filmName, "UTF-8"));
                request.addHeader("Authorization", "Bearer " + API_KEY);

                try (CloseableHttpResponse response = httpClient.execute(request)) {
                    if (response.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(response.getEntity());
                    } else {
                        return "Error: " + response.getStatusLine().getStatusCode();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error fetching film info";
            }
        }
    }
}
