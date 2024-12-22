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
    private CommentHolderPanel commentHolderPanel;
    SimplePanel userInputHolderPanel;
    Integer[] ratings = {0,1,2,3,4,5,6,7,8,9,10};
    private JComboBox<Integer> ratingsComboBox;
    private SimplePanel buttonsPanel;
    public SimpleButton commentButton;
    public SimpleButton addToWatchListButton;
    ClientMainWindow clientMainWindow;
    int userRating;
    private Review review;
    private boolean movieFound;
    private ImageIcon icon;
    private ImageIcon addIcon = new ImageIcon("src/com/esezak/client/UI/Elements/Buttons/Icons/add.png");
    private ImageIcon deleteIcon = new ImageIcon("src/com/esezak/client/UI/Elements/Buttons/Icons/delete.png");

    public FilmPanel(Content film, ImageIcon icon, ClientMainWindow clientMainWindow) {
        super();
        this.clientMainWindow = clientMainWindow;
        this.ratingsComboBox = new JComboBox<>(ratings);
        ratingsComboBox.setSelectedIndex(0);
        userRating = ratingsComboBox.getItemAt(ratingsComboBox.getSelectedIndex());
        ratingsComboBox.setFont(GLOBAL_FONT);

        this.film = film;
        this.icon = icon;
        movieFound = clientMainWindow.leftPanel.isMovieInWatchlist(film.getId());

        infoString = "<html><body><div><h2>"+film.getTitle()+"</h2>"+
                "<p style=\"font-size:11px\">Genres: "+film.getGenres()+"</p>"+
                "<p style=\"font-size:11px\">  Overview: "+film.getOverview()+"</p>"+
                "<p>  Rating: "+film.getAvg_rating()+"</p></div></body></html>";

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        //---------------------Movie Banner-------------------------
        filmDetailsPanel = new SimplePanel();
//        SimpleLabel movieBanner = new SimpleLabel(icon,infoString);//Movie details
//        movieBanner.getLabel().setIconTextGap(10);
//        filmDetailsPanel.getPanel().add(movieBanner.getLabel(),BorderLayout.NORTH);
//        panel.add(filmDetailsPanel.getPanel(),BorderLayout.NORTH);
        //--------------------------------------------------

        //-------------------Before Review Buttons + Write Review---------------------
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
//        buttonsPanel.getPanel().add(commentButton.getButton());
//        buttonsPanel.getPanel().add(addToWatchListButton.getButton());
//        userInputHolderPanel.getPanel().add(buttonsPanel.getPanel(),BorderLayout.NORTH);

//        panel.add(userInputHolderPanel.getPanel(),BorderLayout.CENTER);
        reviewsPanel = new SimplePanel();
        reviewsPanel.getPanel().setLayout(new GridLayout(0,1));
        commentButton.getButton().addActionListener(new onReviewButtonClick());
        setReviewButtonStates();
//        Response response = clientMainWindow.connection.getFilmInformation(film.getId());
//        System.out.println(response.getData());
//        ArrayList<Review> reviews = response.getReviews();
//        for(int i = 0; i < reviews.size(); i++){
//            addReview(reviews.get(i));
//        }
//        panel.add(reviewsPanel.getPanel(),BorderLayout.SOUTH);
        initialize();

    }
    public void initialize(){
        SimpleLabel movieBanner = new SimpleLabel(icon,infoString);//Movie details
        movieBanner.getLabel().setIconTextGap(10);
        filmDetailsPanel.getPanel().add(movieBanner.getLabel(),BorderLayout.NORTH);

        panel.add(filmDetailsPanel.getPanel(),BorderLayout.NORTH);
        buttonsPanel.getPanel().add(commentButton.getButton());
        buttonsPanel.getPanel().add(addToWatchListButton.getButton());
        userInputHolderPanel.getPanel().add(buttonsPanel.getPanel(),BorderLayout.NORTH);
        panel.add(userInputHolderPanel.getPanel(),BorderLayout.CENTER);
        movieFound = clientMainWindow.leftPanel.isMovieInWatchlist(film.getId());
        System.out.println("current movie found: "+movieFound);
        reviewsPanel.getPanel().removeAll();
        Response response = clientMainWindow.connection.getFilmInformation(film.getId());
        System.out.println(response.getData());
        ArrayList<Review> reviews = response.getReviews();
        for(int i = 0; i < reviews.size(); i++){
            addReview(reviews.get(i));
        }
        panel.add(reviewsPanel.getPanel(),BorderLayout.SOUTH);
        setReviewButtonStates();
        panel.revalidate();
        clientMainWindow.centerPanel.getPanel().repaint();
    }
    public void setReviewButtonStates(){
        if(!clientMainWindow.isLoggedIn){
            commentButton.getButton().setEnabled(false);
            commentButton.getButton().setToolTipText("You are not logged in");
        }else if(!movieFound){
            commentButton.getButton().setEnabled(false);
            commentButton.getButton().setToolTipText("You must add to watchlist to write a comment");
        }else{
            commentButton.getButton().setEnabled(true);
            commentButton.getButton().setToolTipText(null);
        }

        if(!clientMainWindow.isLoggedIn){
            addToWatchListButton.getButton().setEnabled(false);
            addToWatchListButton.getButton().setToolTipText("You are not logged in");
        }else if(!movieFound){
            addToWatchListButton.getButton().setEnabled(true);
        }else{
            addToWatchListButton.getButton().setText("Remove from Watchlist");
            addToWatchListButton.getButton().setEnabled(true);
        }
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
    private class CommentHolderPanel {
        private SimplePanel reviewPanel;
        private SimplePanel submitAndRatingPanel;
        private SimpleButton submitButton;
        private SimpleLabel reviewLabel;
        private JTextArea reviewTextArea;
        public CommentHolderPanel(){
            reviewPanel = new SimplePanel();
            submitAndRatingPanel = new SimplePanel();
            submitButton = new SimpleButton("Submit");
            submitButton.getButton().addActionListener(new onSubmitButtonClick());
            reviewLabel = new SimpleLabel("Review:");
            reviewTextArea = new JTextArea();
            reviewTextArea.setFont(GLOBAL_FONT);
            submitAndRatingPanel.getPanel().add(submitButton.getButton(), BorderLayout.EAST);
            submitAndRatingPanel.getPanel().add(ratingsComboBox, BorderLayout.CENTER);
            reviewPanel.getPanel().add(reviewLabel.getLabel(),BorderLayout.NORTH);
            reviewPanel.getPanel().add(reviewTextArea,BorderLayout.CENTER);
            reviewPanel.getPanel().add(submitAndRatingPanel.getPanel(),BorderLayout.EAST);
        }
        public JPanel getPanel(){
            return reviewPanel.getPanel();
        }

    }
    private class onReviewButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            commentHolderPanel = new CommentHolderPanel();
            userInputHolderPanel.getPanel().add(commentHolderPanel.getPanel(),BorderLayout.CENTER);
            clientMainWindow.centerPanel.getPanel().revalidate();
        }
    }
    private class onAddToWatchListButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(addToWatchListButton.getButton().getText().equals("Remove from Watchlist")){
                System.out.println("Remove from Watchlist not implemented");
                addToWatchListButton.getButton().setText("Add to watch list");
                addToWatchListButton.getButton().setIcon(addIcon);
            }else{
                if (clientMainWindow.connection.sendAddToWatchListRequest(film.getId(), clientMainWindow.getUsername())) {
                    System.out.println("Added to watch list");
                    addToWatchListButton.getButton().setText("Remove from Watchlist");
                    addToWatchListButton.getButton().setIcon(deleteIcon);
                }else{
                    System.out.println("Failed to add to watch list");
                }
            }
        }
    }
    private class onSubmitButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            review = getReview();
            System.out.println("Review: "+review.toJson());
            clientMainWindow.connection.sendRateMovieRequest(film.getId(),review);
            commentHolderPanel.getPanel().removeAll();
            userInputHolderPanel.getPanel().remove(commentHolderPanel.getPanel());
            initialize();
            userInputHolderPanel.getPanel().revalidate();
            userInputHolderPanel.getPanel().repaint();

        }
        public Review getReview() {
            String comment = commentHolderPanel.reviewTextArea.getText();
            int rating = ratingsComboBox.getSelectedIndex();
            String username = clientMainWindow.getUsername();
            return new Review(username,rating,comment);
        }

    }


}
