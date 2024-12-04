package com.esezak.server.MovieLookup.TVDB;
import com.esezak.server.Database.Management.DB;
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
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZ2UiOiIiLCJhcGlrZXkiOiI5OGQ1MzgzNy00ZTk1LTRmNjQtYjI2ZC1lMDQyNzU4ZWFiMDEiLCJjb21tdW5pdHlfc3VwcG9ydGVkIjpmYWxzZSwiZXhwIjoxNzM0NzQ0OTQ4LCJnZW5kZXIiOiIiLCJoaXRzX3Blcl9kYXkiOjEwMDAwMDAwMCwiaGl0c19wZXJfbW9udGgiOjEwMDAwMDAwMCwiaWQiOiIyNjk4ODkyIiwiaXNfbW9kIjpmYWxzZSwiaXNfc3lzdGVtX2tleSI6ZmFsc2UsImlzX3RydXN0ZWQiOmZhbHNlLCJwaW4iOm51bGwsInJvbGVzIjpbXSwidGVuYW50IjoidHZkYiIsInV1aWQiOiIifQ.iuhDMRIVoahwRjGmZWOvCJszBYdYZt2HS-t6IUvMCdJITu9D1h0AXZUehRcwavRfm5LBb9uTMsiRmvpdt-ZOJ1q9fjRZKAJShFwSsdbnawxvhibnHdCA8Jyh60RUUzjMVOuFN2EzvPbgkZJcROXuPrc4tDyoK7YuKFlpcIkjbIB3OoW-yvi-MqPlYCw48MFFqgvQni08r2V8eKQKSw6c3VN_yFm3YiCgs1LgQ2_pldeypLfWbzcxWxRbOFWOg-FUTM6tC7FxMSQxouW0cA2282SACtDObh2IBv-yDgtRUY5fnuNeUd3zmls9ps1mJ4ckg10WXLG191Prr9_ych1vnxkwpqYlrz-pnsdSK69oEa7CztB8maanZFOGildBPUTGbyuid2dfYRcI60OiIgTwf0V4lZaFXy3qJ3iebvjnSg5WDpXmDspG7PaKGWg4_mKpVZkjQcWR_i3Si_aMTyoGLFRZPct0NAShKXG3L8c4i5BsIqv0hguHGuL-9EpRyrkaPHJLeCmzD8EsKXMbZxHzKHQGv6t5rM5bwFAxb4w7p-405nxdR94oTksYyoDayv6gWhYKC3lHPAJ14YIhDq8ti4MpTx3vtKBgyrAgo9GLPtpG5w1Fz14j2L7sBj7zRP-AInMsByxgFvZDDpMjudQubD78HwyEsFn0OyXLe_WtZyU";



    /**
     *
     * @param contentName name of the content
     * @param contentType type of content (movie/series)
     * @return list of content objects
     */
    public static ArrayList<Content> queryFromTVDB(String contentName, ContentType contentType) {
        ArrayList<Content> contents = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL + "/search?query=" + stringParser(contentName) + "&type=" + contentType.toString());
            request.setHeader("accept", "application/json");
            request.addHeader("Authorization", "Bearer " + TOKEN);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    contents = parseJson(EntityUtils.toString(response.getEntity()), contentType);
                    for (Content content : contents) {
                        DB.addToDatabase(content);
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

    private static ArrayList<Content> parseJson(String response, ContentType type) {
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
        ArrayList<Content> movieContents = TVDBSearcher.queryFromTVDB("Star Wars", ContentType.movie);
        if (movieContents != null) {
            for (Content content : movieContents) {
                content.displayContent();
            }
        }
    }
}
