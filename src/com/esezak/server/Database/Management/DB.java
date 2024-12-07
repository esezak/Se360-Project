package com.esezak.server.Database.Management;

import com.esezak.server.Database.Init.ConnectDB;
import com.esezak.server.MovieLookup.Content.Content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
    public static void addToDatabase(Content content) {
<<<<<<< Updated upstream
        String query = "INSERT INTO Movies (movie_id, title, release_year, genre, director, overview, image_url,rating) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) ";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if(!isDuplicate(connection,content)){
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, content.getId());
                statement.setString(2, content.getTitle());
                statement.setString(3, content.getRelease_date());
                statement.setString(4, content.getGenres());
                statement.setString(5, content.getDirector());
                statement.setString(6, content.getOverview());
                statement.setString(7, content.getImage_url());
                double rating = content.getAvg_rating();
                if (rating >= 0) {
                    statement.setDouble(8, rating);
                } else {
                    statement.setNull(8, java.sql.Types.REAL);
=======
        String query = "INSERT INTO Movies (movie_id, title, release_year, genre, director, overview, image_url, rating) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        ConnectDB connectDB = new ConnectDB();
        try (Connection connection = connectDB.getConnection()) {
            if (connection != null && !isDuplicate(connection, content)) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, content.getId());
                    statement.setString(2, content.getTitle());
                    statement.setString(3, content.getRelease_date());
                    statement.setString(4, content.getGenres());
                    statement.setString(5, content.getDirector());
                    statement.setString(6, content.getOverview());
                    statement.setString(7, content.getImage_url());

                    double rating = content.getAvg_rating();
                    if (rating >= 0) {
                        statement.setDouble(8, rating);
                    } else {
                        statement.setNull(8, java.sql.Types.REAL);
                    }

                    statement.executeUpdate();
                    System.out.println("Content added to database: " + content.getTitle());
>>>>>>> Stashed changes
                }
            } else {
                System.out.println("Duplicate entry: " + content.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        } finally {
            connectDB.closeConnection();
        }
    }

    public static boolean isDuplicate(Connection connection, Content content) throws SQLException {
        String checkQuery = "SELECT 1 FROM Movies WHERE movie_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {
            pstmt.setString(1, content.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public static boolean verifyPassword(Connection connection, String username, String password) {
        String query = "SELECT password FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
        return false;
    }

    public static boolean isMovieInWatchlist(Connection connection, String username, String movieId) throws SQLException {
        String query = "SELECT 1 FROM Watchlist WHERE username = ? AND movie_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
