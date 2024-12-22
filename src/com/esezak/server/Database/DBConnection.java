package com.esezak.server.Database;

import com.esezak.server.MovieLookup.Content.Content;

import java.sql.*;

public class DBConnection {
    private Connection dbConnection;
    private String url = "jdbc:sqlite:mydb.db";

    public DBConnection() {
        try {
            dbConnection = DriverManager.getConnection(url);
            System.out.println("Connection Successful");
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }
    }

    public Connection getDbConnection() {
        return dbConnection;
    }

    public void closeConnection() {
        try {
            if (dbConnection != null) {
                System.out.println("Connection Closed");
                dbConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToDatabase(Content content) {
        String query = "INSERT INTO Movies (movie_id, title, release_year, genre, director, overview, image_url, rating) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            if (dbConnection != null && !isDuplicate(content)) {
                try (PreparedStatement statement = dbConnection.prepareStatement(query)) {
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
                }
            } else {
                System.out.println("Duplicate entry: " + content.getTitle());
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }/* finally {
            closeConnection();
        }*/
    }

    public boolean isDuplicate(Content content) throws SQLException {
        String checkQuery = "SELECT 1 FROM Movies WHERE movie_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(checkQuery)) {
            pstmt.setString(1, content.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean verifyPassword(String username, String password) {
        String query = "SELECT password FROM Users WHERE username = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return password.equals(storedPassword);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying credentials: " + e.getMessage());
        }
        return false;
    }

    public boolean isMovieInWatchlist(String username, String movieId) throws SQLException {
        String query = "SELECT 1 FROM Watchlist WHERE username = ? AND movie_id = ?";
        try (PreparedStatement pstmt = dbConnection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, movieId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
