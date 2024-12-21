package com.esezak.client.ConnectionManager;

public enum RequestType {
    //Requests
        //User Requests
            LOGIN,
            //Send -> Username + Password + Request type  (post)
            // Response -> ok | fail
            LOGOUT,
            //Send -> Request Type (get)
            // Response -> ok | fail
            DISCONNECT,
            //Send -> Request Type (get)
            // Response -> ok | fail
            GET_USER_WATCHLIST,
            //Send -> Request Type (get)
            // Response -> ArrayList<Content> ok | fail if not logged in
            ADD_MOVIE_TO_WATCHLIST,
            //Send -> Request Type + "MovieID" (post)
            //Response -> ok | fail if not logged in
            RATE_MOVIE,
            //Send -> Request Type + "MovieID" + Review (post)
            //Response -> ok | fail if not logged in
        SEARCH_MOVIE,
        //Send -> "Movie name"
        // Response -> ArrayList<Content> ok | fail
        GET_MOVIE_INFORMATION,
        //Send -> "MovieID"
        // Response -> Content + ArrayList<Review> ok | fail
        UPDATE_WATCHLIST,
        //Send -> "username" + watchlistJson
        //response -> ok | fail
        DELETE_MOVIE_FROM_WATCHLIST,
        //Send -> "username" + movieID
        //response -> ok |fail

}