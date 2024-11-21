package com.esezak.server.MovieLookup.TVDB;
import com.esezak.server.MovieLookup.Content.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TVDBSearcher {
    private static final String API_URL = "https://api4.thetvdb.com/v4";
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZ2UiOiIiLCJhcGlrZXkiOiI5OGQ1MzgzNy00ZTk1LTRmNjQtYjI2ZC1lMDQyNzU4ZWFiMDEiLCJjb21tdW5pdHlfc3VwcG9ydGVkIjpmYWxzZSwiZXhwIjoxNzM0NzQ0OTQ4LCJnZW5kZXIiOiIiLCJoaXRzX3Blcl9kYXkiOjEwMDAwMDAwMCwiaGl0c19wZXJfbW9udGgiOjEwMDAwMDAwMCwiaWQiOiIyNjk4ODkyIiwiaXNfbW9kIjpmYWxzZSwiaXNfc3lzdGVtX2tleSI6ZmFsc2UsImlzX3RydXN0ZWQiOmZhbHNlLCJwaW4iOm51bGwsInJvbGVzIjpbXSwidGVuYW50IjoidHZkYiIsInV1aWQiOiIifQ.iuhDMRIVoahwRjGmZWOvCJszBYdYZt2HS-t6IUvMCdJITu9D1h0AXZUehRcwavRfm5LBb9uTMsiRmvpdt-ZOJ1q9fjRZKAJShFwSsdbnawxvhibnHdCA8Jyh60RUUzjMVOuFN2EzvPbgkZJcROXuPrc4tDyoK7YuKFlpcIkjbIB3OoW-yvi-MqPlYCw48MFFqgvQni08r2V8eKQKSw6c3VN_yFm3YiCgs1LgQ2_pldeypLfWbzcxWxRbOFWOg-FUTM6tC7FxMSQxouW0cA2282SACtDObh2IBv-yDgtRUY5fnuNeUd3zmls9ps1mJ4ckg10WXLG191Prr9_ych1vnxkwpqYlrz-pnsdSK69oEa7CztB8maanZFOGildBPUTGbyuid2dfYRcI60OiIgTwf0V4lZaFXy3qJ3iebvjnSg5WDpXmDspG7PaKGWg4_mKpVZkjQcWR_i3Si_aMTyoGLFRZPct0NAShKXG3L8c4i5BsIqv0hguHGuL-9EpRyrkaPHJLeCmzD8EsKXMbZxHzKHQGv6t5rM5bwFAxb4w7p-405nxdR94oTksYyoDayv6gWhYKC3lHPAJ14YIhDq8ti4MpTx3vtKBgyrAgo9GLPtpG5w1Fz14j2L7sBj7zRP-AInMsByxgFvZDDpMjudQubD78HwyEsFn0OyXLe_WtZyU";


    /**
     * @param contentName name of the content
     * @param contentType type of content movie/series
     * @return returns a parsed list of content objects
     */
    public ArrayList<Content> queryFromTVDB(String contentName, ContentType contentType) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL + "/search?query=" + stringParser(contentName) + "&type="+contentType.toString());
            request.setHeader("accept", "application/json");
            request.addHeader("Authorization", "Bearer " + TOKEN);
            //System.out.println("Executing request " + request.getRequestLine());
            ArrayList<Content> contents = null;
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    contents = parseJson(EntityUtils.toString(response.getEntity()));
                    return contents;
                } else {
                    System.err.println("Error: " + response.getStatusLine().getStatusCode());
                    return null;
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error: " + e.getMessage());
                return null;
            }
    }


    /**
     * @param queryName
     * @return parsed query<br>
     * Parses the input string for searching in api
     */
    private static String stringParser(String queryName) {
        queryName = queryName.replace(" ", "%20");
        queryName = queryName.replace("'", "%27");
        queryName = queryName.replace("&", "%26");
        queryName = queryName.replace("!", "%21");
        queryName = queryName.replace("?", "%3F");
        queryName = queryName.replace(":", "%3A");
        return queryName;
    }

    /**
     * @param response that is recieved from the TVDB
     * @return the content that is recieved from TVDB in a usable from
     */
    private static ArrayList<Content> parseJson(String response){
        String id="";String title =""; String relase_date ="";
        String genres = ""; String director = ""; String overview = "";
        String image_url = ""; String release_year="";
        ArrayList<Content> contents = new ArrayList<Content>();
        JSONObject json = new JSONObject(response);
        JSONArray jsonArray = json.getJSONArray("data");
        for(int i=0; i<jsonArray.length(); i++){
            JSONObject item = jsonArray.getJSONObject(i);
            id = item.getString("id");
            title = item.getString("name");
            if(item.getString("status").equals("released")){
                release_year = item.getString("year");
                relase_date = item.optString("first_air_time",release_year);
            }else{
                relase_date ="TBA";
            }
            genres = item.getJSONArray("genres").toString();
            director = item.optString("director","Not Known");
            overview = item.optString("overview","Not Known");
            image_url = item.getString("image_url");
            contents.add(new Content(id,title,image_url,overview,relase_date,genres,director));
        }
        return contents;
    }

    public static void main(String[] args) {
        TVDBSearcher tvdbSearcher = new TVDBSearcher();
        ArrayList<Content> contents =tvdbSearcher.queryFromTVDB("Lord of the rings",ContentType.movie);
        for(Content content : contents){
            content.displayContent();
        }
    }
}
