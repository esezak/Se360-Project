package com.esezak.server.MovieLookup.TVDB;

import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class TVDBAuthenticator {
    private static final String API_KEY = "98d53837-4e95-4f64-b26d-e042758eab01";
    private static final String API_URL = "https://api4.thetvdb.com/v4/login";

    /**
     * Prints a new login token in case the old one is expired. The expiration time is 1 month<br>
     * Last TOKEN generated: 15/12/24
     */
    public static void getToken() {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(API_URL);
            request.addHeader("accept", "application/json");
            request.addHeader("Content-Type", "application/json");

            JSONObject json = new JSONObject();
            json.put("apikey", API_KEY);

            StringEntity entity = new StringEntity(json.toString());
            request.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String result = EntityUtils.toString(response.getEntity());
                    JSONObject jsonResponse = new JSONObject(result);
                    //JSONArray jsonArray = jsonResponse.getJSONArray("data");
                    //JSONObject jsonr = jsonArray.getJSONObject(0);
                    System.out.println(jsonResponse.toString());
                } else {
                    System.out.println("Authentication failed: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        getToken();
    }
}
