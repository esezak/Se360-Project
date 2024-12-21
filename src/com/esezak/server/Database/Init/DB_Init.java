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
                movie_id TEXT,
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
                String createTriggerWatchlistOnReview1 = """
                CREATE TRIGGER IF NOT EXISTS UpdateWatchlistOnReviewChange1
                AFTER INSERT ON Reviews
                FOR EACH ROW
                BEGIN
                    UPDATE Watchlist
                    SET user_rating = NEW.user_rating
                    WHERE username = NEW.username AND movie_id = NEW.movie_id;
                END;
            """;
                String createTriggerWatchlistOnReview2 = """
                CREATE TRIGGER IF NOT EXISTS UpdateWatchlistOnReviewChange2
                AFTER UPDATE ON Reviews
                FOR EACH ROW
                BEGIN
                    UPDATE Watchlist
                    SET user_rating = NEW.user_rating
                    WHERE username = NEW.username AND movie_id = NEW.movie_id;
                END;
            """;
                String createTriggerReviewOnWatchlist1 = """
                CREATE TRIGGER IF NOT EXISTS UpdateReviewOnWatchlistChange1
                AFTER INSERT ON Watchlist
                FOR EACH ROW
                BEGIN
                    INSERT INTO Reviews (username, movie_id, user_rating, review_date, comment)
                    VALUES (NEW.username, NEW.movie_id, NEW.user_rating, DATE('now'), 'From Watchlist')
                    ON CONFLICT (username, movie_id)
                    DO UPDATE SET user_rating = NEW.user_rating;
                END;
            """;
                String createTriggerReviewOnWatchlist2 = """
                CREATE TRIGGER IF NOT EXISTS UpdateReviewOnWatchlistChange2
                AFTER UPDATE ON Watchlist
                FOR EACH ROW
                BEGIN
                    INSERT INTO Reviews (username, movie_id, user_rating, review_date, comment)
                    VALUES (NEW.username, NEW.movie_id, NEW.user_rating, DATE('now'), 'From Watchlist')
                    ON CONFLICT (username, movie_id)
                    DO UPDATE SET user_rating = NEW.user_rating;
                END;
            """;
                statement.executeUpdate(createUsersTable);
                statement.executeUpdate(createMoviesTable);
                statement.executeUpdate(createReviewsTable);
                statement.executeUpdate(createWatchlistTable);
                statement.executeUpdate(createTriggerWatchlistOnReview1);
                statement.executeUpdate(createTriggerWatchlistOnReview2);
                statement.executeUpdate(createTriggerReviewOnWatchlist1);
                statement.executeUpdate(createTriggerReviewOnWatchlist2);
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