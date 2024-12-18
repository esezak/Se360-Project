package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.SimpleLabel;
import com.esezak.server.MovieLookup.Content.Content;
import com.esezak.server.MovieLookup.Content.Review;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FilmPanel extends SimplePanel {
    private SimplePanel filmDetailsPanel;
    private JScrollPane scrollPane;
    private SimplePanel reviewsPanel;
    public Content film;
    private String infoString;
    SimplePanel userInputHolderPanel;

    private SimplePanel buttonsPanel;
    public SimpleButton commentButton;
    public SimpleButton addToWatchListButton;
    ClientMainWindow clientMainWindow;

    public FilmPanel(Content film, ImageIcon icon, ClientMainWindow clientMainWindow) {
        super();
        this.clientMainWindow = clientMainWindow;
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
        ;
        //----------------------------------------------
        filmDetailsPanel = new SimplePanel();
        SimpleLabel label = new SimpleLabel(icon,infoString);
        label.getLabel().setIconTextGap(10);
        filmDetailsPanel.getPanel().add(label.getLabel(),BorderLayout.NORTH);
        panel.add(filmDetailsPanel.getPanel(),BorderLayout.NORTH);


        userInputHolderPanel = new SimplePanel();

        commentButton = new SimpleButton("Write a comment");
        commentButton.getButton().setBorder(BorderFactory.createLineBorder(Color.black));

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
        for(int i = 0; i < 10; i++){
            addReview(new Review("Default User",4,"this is the comment we have all been waiting for this might be heaven or this could be hell, this comment is very loong and we don't know what is going to happen, anything could happen right"));
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
            SimpleButton submitButton = new SimpleButton("Submit");
            SimpleLabel reviewLabel = new SimpleLabel("Review:");
            JTextArea reviewTextArea = new JTextArea();
            reviewPanel.getPanel().add(reviewLabel.getLabel(),BorderLayout.NORTH);
            reviewPanel.getPanel().add(reviewTextArea,BorderLayout.CENTER);
            reviewPanel.getPanel().add(submitButton.getButton(),BorderLayout.EAST);
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

}
