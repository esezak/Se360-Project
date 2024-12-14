package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;

import javax.swing.*;
import java.awt.*;

public class FilmPanel extends SimplePanel {
    private SimplePanel filmDetailsPanel;
    private JScrollPane scrollPane;
    private SimplePanel reviewsPanel;
    private Content film;
    private ImageIcon filmIcon;
    private String infoString;
    public FilmPanel(Content film, ImageIcon icon) {
        super();
        this.film = film;
        this.filmIcon = icon;
        infoString = "<html><body><div><h2>"+film.getTitle()+"</h2>"+
                "<p style=\"font-size:10px\">Genres: "+film.getGenres()+"</p>"+
                "<p style=\"font-size:10px\">  Overview: "+film.getOverview()+"</p>"+
                "<p>  Rating: "+film.getAvg_rating()+"</p></div></body></html>";

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        ;
        //----------------------------------------------
        filmDetailsPanel = new SimplePanel();
        SimpleLabel label = new SimpleLabel(icon,infoString);
        label.getLabel().setIconTextGap(10);
        filmDetailsPanel.getPanel().add(label.getLabel(),BorderLayout.NORTH);
        panel.add(filmDetailsPanel.getPanel(),BorderLayout.NORTH);
        reviewsPanel = new SimplePanel();
        reviewsPanel.getPanel().setLayout(new GridLayout(0,1));
        for(int i = 0; i < 10; i++){
            addReview(new Review("Default User",4,"this is the comment we have all been waiting for this might be heaven or this could be hell, this comment is very loong and we don't know what is going to happen, anything could happen right"));
        }
        panel.add(reviewsPanel.getPanel(),BorderLayout.SOUTH);

    }
    private void addReview(Review review) {

        String htmlReview = "<html><body><h2>User: "+review.getAuthor()+"</h2>"+
                "<p style=\"font-size:12px\">Score: "+review.getRating()+"</p>"+
                "<p style=\"font-size:12px\">Comment: "+formatLongText(review.getComment())+"</p></body></html>";
        SimpleLabel label = new SimpleLabel(htmlReview);
        reviewsPanel.getPanel().add(label.getLabel());
    }
    private String formatLongText(String longText) {
        String[] words = longText.split(" ");
        StringBuilder formatted = new StringBuilder();
        for(int i = 0; i < words.length; i++){
            if(i != 0 && i%10 == 0){
                formatted.append("<br>");
            }
            formatted.append(words[i]).append(" ");
        }
        return formatted.toString();
    }

}
