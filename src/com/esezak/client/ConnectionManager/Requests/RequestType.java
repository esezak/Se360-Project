package com.esezak.client.ConnectionManager.Requests;

public enum RequestType {
    //Requests
        //User Requests
            LOGIN,
            //Send -> Username + Password + Request Type
            // Response -> ok | fail
            LOGOUT,
            //Send -> Request Type
            // Response -> ok | fail
            GET_USER_WATCHLIST,
            //Send -> Request Type
            // Response -> ArrayList<Content> ok | fail if not logged in
            ADD_MOVIE_TO_WATCHLIST,
            //Send -> Request Type + "MovieID"
            //Response -> ok | fail if not logged in
            RATE_MOVIE,
            //Send -> Request Type + "MovieID" + Review
            //Response -> ok | fail if not logged in

        //TODO Movie Requests
        SEARCH_MOVIE,
        //Send -> "Movie name"
        // Response -> ArrayList<Content> ok | fail
        GET_MOVIE_INFORMATION,
        //Send -> "MovieID"
        // Response -> Content + ArrayList<Review> ok | fail


}
