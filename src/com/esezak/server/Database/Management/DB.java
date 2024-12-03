package com.esezak.server.Database.Management;

import com.esezak.server.MovieLookup.Content.Content;

import java.sql.*;

public class DB {
    // Database connection details
    private static final String DB_URL = "jdbc:sqlite:mydb.db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = " ";

    public static void addToDatabase(Content content) {
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
                }
                statement.executeUpdate();
            }else{
                System.out.println("Duplicate entry");
            }
        } catch (SQLException e) {
            System.err.println("Database Error: " + e.getMessage());
        }
    }

    public static boolean isDuplicate(Connection conn, Content content) throws SQLException {
        String checkQuery = "SELECT 1 FROM Movies WHERE movie_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
            pstmt.setString(1, content.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
