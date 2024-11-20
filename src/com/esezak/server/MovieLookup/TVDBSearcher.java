package com.esezak.server.MovieLookup;

import java.net.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;
public class TVDBSearcher {
    private static final String API_URL = "https://api4.thetvdb.com/v4";
    private static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhZ2UiOiIiLCJhcGlrZXkiOiI5OGQ1MzgzNy00ZTk1LTRmNjQtYjI2ZC1lMDQyNzU4ZWFiMDEiLCJjb21tdW5pdHlfc3VwcG9ydGVkIjpmYWxzZSwiZXhwIjoxNzM0NzQ0OTQ4LCJnZW5kZXIiOiIiLCJoaXRzX3Blcl9kYXkiOjEwMDAwMDAwMCwiaGl0c19wZXJfbW9udGgiOjEwMDAwMDAwMCwiaWQiOiIyNjk4ODkyIiwiaXNfbW9kIjpmYWxzZSwiaXNfc3lzdGVtX2tleSI6ZmFsc2UsImlzX3RydXN0ZWQiOmZhbHNlLCJwaW4iOm51bGwsInJvbGVzIjpbXSwidGVuYW50IjoidHZkYiIsInV1aWQiOiIifQ.iuhDMRIVoahwRjGmZWOvCJszBYdYZt2HS-t6IUvMCdJITu9D1h0AXZUehRcwavRfm5LBb9uTMsiRmvpdt-ZOJ1q9fjRZKAJShFwSsdbnawxvhibnHdCA8Jyh60RUUzjMVOuFN2EzvPbgkZJcROXuPrc4tDyoK7YuKFlpcIkjbIB3OoW-yvi-MqPlYCw48MFFqgvQni08r2V8eKQKSw6c3VN_yFm3YiCgs1LgQ2_pldeypLfWbzcxWxRbOFWOg-FUTM6tC7FxMSQxouW0cA2282SACtDObh2IBv-yDgtRUY5fnuNeUd3zmls9ps1mJ4ckg10WXLG191Prr9_ych1vnxkwpqYlrz-pnsdSK69oEa7CztB8maanZFOGildBPUTGbyuid2dfYRcI60OiIgTwf0V4lZaFXy3qJ3iebvjnSg5WDpXmDspG7PaKGWg4_mKpVZkjQcWR_i3Si_aMTyoGLFRZPct0NAShKXG3L8c4i5BsIqv0hguHGuL-9EpRyrkaPHJLeCmzD8EsKXMbZxHzKHQGv6t5rM5bwFAxb4w7p-405nxdR94oTksYyoDayv6gWhYKC3lHPAJ14YIhDq8ti4MpTx3vtKBgyrAgo9GLPtpG5w1Fz14j2L7sBj7zRP-AInMsByxgFvZDDpMjudQubD78HwyEsFn0OyXLe_WtZyU";
    public String run() {
        String filmName = "Legends of Tomorrow";
        return getSeriesInfo(filmName);
    }
    public String getSeriesInfo(String seriesName) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL + "/search?query=" + stringParser(seriesName) + "&type=series");
            request.setHeader("accept", "application/json");
            request.addHeader("Authorization", "Bearer " + TOKEN);
            System.out.println("Executing request " + request.getRequestLine());
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    return "Error: " + response.getStatusLine().getStatusCode();
                }
            }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error fetching series info";
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
        return queryName;
    }

    public static void main(String[] args) {
        TVDBSearcher tvdbSearcher = new TVDBSearcher();
        System.out.println(tvdbSearcher.run());
    }
}
