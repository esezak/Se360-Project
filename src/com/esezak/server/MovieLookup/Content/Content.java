package com.esezak.server.MovieLookup.Content;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Content implements Serializable {
    private final String id;
    private final String title;
    private final String release_date;
    private final String genres;
    private final String director;
    private String overview;
    private final String image_url;
    private float avg_rating;
    private int total_rating;
    private int number_of_reviews;

    public Content(String id, String title, String image_url, String overview, String release_date, String genres, String director) {
        this.id = id;
        this.title = title;
        this.image_url = image_url;
        this.overview = overview;
        this.release_date = release_date;
        this.genres = genres;
        this.director = director;
        this.avg_rating = 0;
        this.number_of_reviews = 0;
        this.total_rating = 0;
    }
    public Content(JSONObject json) {
        this.id = json.getString("id");
        this.title = json.getString("title");
        this.release_date = json.getString("release_date");
        this.genres = json.getString("genres");
        this.director = json.getString("director");
        this.overview = json.getString("overview");
        this.image_url = json.getString("image_url");
        this.avg_rating = json.getFloat("avg_rating");
        this.total_rating = json.getInt("total_rating");
        this.number_of_reviews = json.getInt("number_of_reviews");
    }
    public void displayContent() {
        System.out.println("ID: " + id);
        System.out.println("Title: " + title);
        System.out.println("Genres: " + genres);
        System.out.println("Director: " + director);
        System.out.println("Overview: " + overview);
        System.out.println("Image URL: " + image_url);
        System.out.println("Rating: " + avg_rating);
        System.out.println("ReleaseDate: " + release_date);
        System.out.println("Image: " + image_url);
        System.out.println("--------------------------");
    }
    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getRelease_date() {
        return release_date;
    }
    public String getGenres() {
        return genres;
    }
    public String getDirector() {
        return director;
    }
    public String getOverview() {
        return overview;
    }
    public String getImage_url() {
        return image_url;
    }
    public float getAvg_rating() {
        return avg_rating;
    }
    private void calcAverageRating() {
        this.avg_rating += (float)this.total_rating/(float)this.number_of_reviews;
    }
    public void addRating(int newRating) {
        this.total_rating += newRating;
        this.number_of_reviews++;
        calcAverageRating();
    }
    public void updateRating(int oldRating, int newRating) {
        this.total_rating -= oldRating;
        this.total_rating += newRating;
        calcAverageRating();
    }
    public void deleteRating(int rating) {
        this.total_rating -= rating;
        this.number_of_reviews--;
        calcAverageRating();
    }
    public int getTotal_rating() {
        return total_rating;
    }
    public int getNuber_of_reviews() {
        return number_of_reviews;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public static JSONObject toJsonObject(Content content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", content.getId());
        jsonObject.put("title", content.getTitle());
        jsonObject.put("release_date", content.getRelease_date());
        jsonObject.put("genres", content.getGenres());
        jsonObject.put("director", content.getDirector());
        jsonObject.put("overview", content.getOverview());
        jsonObject.put("image_url", content.getImage_url());
        jsonObject.put("avg_rating", content.getAvg_rating());
        jsonObject.put("number_of_reviews", content.getNuber_of_reviews());
        jsonObject.put("total_rating", content.getTotal_rating());
        return jsonObject;
    }
    public String toJsonString() {
        return toJsonObject(this).toString();
    }
    public static ArrayList<Content> fromJsonArray(String data) {
        JSONArray jsonArray = new JSONArray(data);
        ArrayList<Content> contents = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            contents.add(new Content(jsonArray.getJSONObject(i)));
        }
        return contents;
    }
    public static String toJsonArray(ArrayList<Content> contents) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < contents.size(); i++) {
            jsonArray.put(i,toJsonObject(contents.get(i)));
        }
        return jsonArray.toString();
    }
}
