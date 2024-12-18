package com.esezak.server.MovieLookup.TVDB;
import com.esezak.server.Database.DBConnection;
import com.esezak.server.MovieLookup.Content.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class TVDBSearcher {
    private static final String API_URL = "https://api4.thetvdb.com/v4";
    //Last TOKEN generated: 15/12/24
    //Valid for one month
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZ2UiOiIiLCJhcGlrZXkiOiI5OGQ1MzgzNy00ZTk1LTRmNjQtYjI2ZC1lMDQyNzU4ZWFiMDEiLCJjb21tdW5pdHlfc3VwcG9ydGVkIjpmYWxzZSwiZXhwIjoxNzM2OTAxMDk1LCJnZW5kZXIiOiIiLCJoaXRzX3Blcl9kYXkiOjEwMDAwMDAwMCwiaGl0c19wZXJfbW9udGgiOjEwMDAwMDAwMCwiaWQiOiIyNjk4ODkyIiwiaXNfbW9kIjpmYWxzZSwiaXNfc3lzdGVtX2tleSI6ZmFsc2UsImlzX3RydXN0ZWQiOmZhbHNlLCJwaW4iOm51bGwsInJvbGVzIjpbXSwidGVuYW50IjoidHZkYiIsInV1aWQiOiIifQ.vCYedcwBmpxxjRmX-fgDRooN5j-fxdnFGXQvK8jyFLQ2nquUSSXBImUA4IuVpif6HfKflp8zFEXQjmFaisJdKWllO6hAu6ljohT2jn-4TJB_06DdlLtB1Wp_StXsgc_FVUGvaz7AdfP1rrDqG9Pd_ihBl5zCunx-Yi5cgxJffCCP2xhf58krJ9AW6JZVR1pW0lCWiutKSOnIMZaRT4C-Q9q4c3AQ8DXwEQQUFSPnliuGUtAE-D_TCHkHAaKUi7VMWQSy1bGcNnqLEn_Hn7DE92rBQjsd-9KAREMxymrQ2Wv9HTLEhdHjsMay0xgYm6mFJF7INaTOCF36TRM7tTZFHfk6ivy39KmuEF7jfwMQVyMxfkLhFEjTtwot8W_OHQKtNiaCZ3i9oL5q41_DkAp0xn07hOLrOHWS65sE_U1F7uruKJKRBILSFwdxMIEwzVsMDwjeN6TCSHJdS6bdyCVhwYOtwEzLhXn11FxV97_t5p9zAz_Nunzveama2e77n_IJKqtV32hnaY2gMQIEhJA0s8CqZdz4PmLhzk8i8K0P4EY977x_SECOkuieSb3SGR9WNrzH0X7qRi07l9L8ETrZz5TtOydD9o8LEY4mywCnZJWPoRHV3cKQT5P_YtpAbHLvFacCMreKztZ6THrVlrPeQmroU_sI86jq-dc5lUwZ5Ls";




    /**
     *
     * @param contentName name of the content
     * @return list of content objects
     */
    public static ArrayList<Content> queryFromTVDB(String contentName) {
        ArrayList<Content> contents = null;
        DBConnection dbConnection = new DBConnection();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL + "/search?query=" + stringParser(contentName) + "&type=movie");

            request.setHeader("accept", "application/json");
            request.addHeader("Authorization", "Bearer " + TOKEN);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    contents = parseJson(EntityUtils.toString(response.getEntity()));
                    for (Content content : contents) {
                        dbConnection.addToDatabase(content);
                    }
                } else {
                    System.err.println("Error: " + response.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return contents;
    }

    private static ArrayList<Content> parseJson(String response) {
        ArrayList<Content> contents = new ArrayList<>();
        JSONObject json = new JSONObject(response);
        JSONArray jsonArray = json.getJSONArray("data");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String id = item.getString("id");
            String title = item.getString("name");
            String releaseYear = item.optString("year", "TBA");
            String genres = "unknown";
            if(item.optJSONArray("genres")!=null)
                genres = item.optJSONArray("genres").toString();
            String director = item.optString("director", "Not Known");
            String overview = item.optString("overview", "Not Known");
            String imageUrl = item.optString("image_url", "");

            contents.add(new Content(id, title, imageUrl, overview, releaseYear, genres, director));
        }
        return contents;
    }
    private static String stringParser(String queryName) {
        return queryName.replace(" ", "%20")
                .replace("'", "%27")
                .replace("&", "%26")
                .replace("!", "%21")
                .replace("?", "%3F")
                .replace(":", "%3A");
    }

    public static void main(String[] args) {
        ArrayList<Content> movieContents = TVDBSearcher.queryFromTVDB("Star Wars");
        if (movieContents != null) {
            for (Content content : movieContents) {
                content.displayContent();
            }
        }
    }
}
