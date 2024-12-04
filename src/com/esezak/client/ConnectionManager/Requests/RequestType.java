package com.esezak.client.ConnectionManager.Requests;

public enum RequestType {
    //Requests
        //User Requests
            LOGIN,
            //Send -> Username + Password + Request Type
            // Response -> ok | fail
            LOGOUT,
            //Send -> Username + Password + Request Type
            // Response -> ok | fail
            GET_USER_WATCHLIST,
            //Send -> Username + Password + Request Type
            // Response -> ArrayList<Content> ok | fail
            ADD_MOVIE_TO_WATCHLIST,
            //Send -> Username + Password + Request Type + "MovieID"
            //Response -> ok | fail
            RATE_MOVIE,
            //Send -> Username + Password + Request Type + "MovieID" + Review
            //Response -> ok | fail

        //TODO Movie Requests
        SEARCH_MOVIE,
        //Send -> "Movie name"
        // Response -> ArrayList<Content> ok | fail
        GET_MOVIE_INFORMATION,
        //Send -> "MovieID"
        // Response -> Content + ArrayList<Review> ok | fail


}
