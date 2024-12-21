package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.SimpleLabel;
import com.esezak.server.ConnectionManager.Response;
import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public class FilmPanel extends SimplePanel {
    private SimplePanel filmDetailsPanel;
    private JScrollPane scrollPane;
    private SimplePanel reviewsPanel;
    public Content film;
    private String infoString;
    SimplePanel userInputHolderPanel;
    Integer[] ratings = {0,1,2,3,4,5,6,7,8,9,10};
    private JComboBox<Integer> ratingsComboBox;
    private SimplePanel buttonsPanel;
    public SimpleButton commentButton;
    public SimpleButton addToWatchListButton;
    ClientMainWindow clientMainWindow;
    JTextArea reviewTextArea;
    int userRating;
    private Review review;

    public FilmPanel(Content film, ImageIcon icon, ClientMainWindow clientMainWindow) {
        super();
        this.clientMainWindow = clientMainWindow;
        this.ratingsComboBox = new JComboBox<>(ratings);
        ratingsComboBox.setSelectedIndex(0);
        userRating = ratingsComboBox.getItemAt(ratingsComboBox.getSelectedIndex());
        ratingsComboBox.setFont(GLOBAL_FONT);
        ImageIcon addIcon = new ImageIcon("src/com/esezak/client/UI/Elements/Buttons/Icons/add.png");
        this.film = film;
        infoString = "<html><body><div><h2>"+film.getTitle()+"</h2>"+
                "<p style=\"font-size:10px\">Genres: "+film.getGenres()+"</p>"+
                "<p style=\"font-size:10px\">  Overview: "+film.getOverview()+"</p>"+
                "<p>  Rating: "+film.getAvg_rating()+"</p></div></body></html>";

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        //----------------------------------------------
        filmDetailsPanel = new SimplePanel();
        SimpleLabel label = new SimpleLabel(icon,infoString);//Movie details
        label.getLabel().setIconTextGap(10);
        filmDetailsPanel.getPanel().add(label.getLabel(),BorderLayout.NORTH);
        panel.add(filmDetailsPanel.getPanel(),BorderLayout.NORTH);

        userInputHolderPanel = new SimplePanel();
        //------------Write Comment Section----------------
        commentButton = new SimpleButton("Write a comment");
        commentButton.getButton().setBorder(BorderFactory.createLineBorder(Color.black));
            //--------------Add to watchlist button--------------
        addToWatchListButton = new SimpleButton("Add to watch list");
        addToWatchListButton.getButton().setIcon(addIcon);
        addToWatchListButton.getButton().setBorder(BorderFactory.createLineBorder(Color.black));
        addToWatchListButton.getButton().setEnabled(clientMainWindow.isLoggedIn);
        addToWatchListButton.getButton().addActionListener(new onAddToWatchListButtonClick());
        buttonsPanel = new SimplePanel();
        buttonsPanel.getPanel().setLayout(new GridLayout(1,2));
        buttonsPanel.getPanel().add(commentButton.getButton());
        buttonsPanel.getPanel().add(addToWatchListButton.getButton());
        userInputHolderPanel.getPanel().add(buttonsPanel.getPanel(),BorderLayout.NORTH);

        panel.add(userInputHolderPanel.getPanel(),BorderLayout.CENTER);
        reviewsPanel = new SimplePanel();
        reviewsPanel.getPanel().setLayout(new GridLayout(0,1));
        commentButton.getButton().addActionListener(new onReviewButtonClick());
        Response response = clientMainWindow.connection.getFilmInformation(film.getId());
        System.out.println(response.getData());
        ArrayList<Review> reviews = response.getReviews();
        for(int i = 0; i < reviews.size(); i++){
            addReview(reviews.get(i));
        }
        panel.add(reviewsPanel.getPanel(),BorderLayout.SOUTH);

    }
    private void addReview(Review review) {
        String htmlReview = "<html><body><h2>User: "+review.getUsername()+"</h2>"+
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
    private class onReviewButtonClick implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            SimplePanel reviewPanel = new SimplePanel();
            SimplePanel submitAndRatingPanel = new SimplePanel();
            SimpleButton submitButton = new SimpleButton("Submit");
            submitButton.getButton().addActionListener(new onSubmitButtonClick());
            SimpleLabel reviewLabel = new SimpleLabel("Review:");
            reviewTextArea = new JTextArea();
            submitAndRatingPanel.getPanel().add(submitButton.getButton(), BorderLayout.EAST);
            submitAndRatingPanel.getPanel().add(ratingsComboBox, BorderLayout.CENTER);
            reviewPanel.getPanel().add(reviewLabel.getLabel(),BorderLayout.NORTH);
            reviewPanel.getPanel().add(reviewTextArea,BorderLayout.CENTER);
            reviewPanel.getPanel().add(submitAndRatingPanel.getPanel(),BorderLayout.EAST);
            userInputHolderPanel.getPanel().add(reviewPanel.getPanel(),BorderLayout.CENTER);
            clientMainWindow.centerPanel.getPanel().revalidate();
        }
    }
    private class onAddToWatchListButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clientMainWindow.connection.sendAddToWatchListRequest(film.getId(), clientMainWindow.getUsername())) {
                System.out.println("Added to watch list");
                addToWatchListButton.getButton().setEnabled(false);
            }else{
                System.out.println("Failed to add to watch list");
            }
        }
    }
    private class onSubmitButtonClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Submitting button");
            review = getReview();
            System.out.println("Review: "+review.toJson());
            clientMainWindow.connection.sendRateMovieRequest(film.getId(),review);
        }
        public Review getReview() {
            String comment = reviewTextArea.getText();
            int rating = ratingsComboBox.getSelectedIndex();
            String username = clientMainWindow.getUsername();
            return new Review(username,rating,comment);
        }

    }


}
