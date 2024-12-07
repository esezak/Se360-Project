package com.esezak.server.Database.Init;
import org.apache.http.annotation.Obsolete;

import java.sql.*;

public class DB_Init {
    public static void main(String[] args) {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String createUsersTable = """
            CREATE TABLE IF NOT EXISTS Users (
                username TEXT PRIMARY KEY,
                password TEXT NOT NULL
            );
        """;

                String createMoviesTable = """
            CREATE TABLE IF NOT EXISTS Movies (
                movie_id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                release_year TEXT NOT NULL,
                genre TEXT,
                director TEXT,
                overview TEXT,
                image_url TEXT,
                rating REAL
            );
        """;

                String createReviewsTable = """
            CREATE TABLE IF NOT EXISTS Reviews (
                username TEXT,
                movie_id INTEGER,
                comment TEXT,
                user_rating REAL,
                review_date TEXT,
                PRIMARY KEY (username, movie_id),
                FOREIGN KEY (username) REFERENCES Users(username),
                FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)
            );
        """;

                String createWatchlistTable = """
            CREATE TABLE IF NOT EXISTS Watchlist (
                username TEXT,
                movie_id INTEGER,
                date_added TEXT,
                user_rating REAL,
                status TEXT,
                PRIMARY KEY (username, movie_id),
                FOREIGN KEY (username) REFERENCES Users(username),
                FOREIGN KEY (movie_id) REFERENCES Movies(movie_id)
            );
        """;
                statement.executeUpdate(createUsersTable);
                statement.executeUpdate(createMoviesTable);
                statement.executeUpdate(createReviewsTable);
                statement.executeUpdate(createWatchlistTable);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }

}