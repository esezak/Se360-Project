package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;

public class FilmPanel extends SimplePanel {
    private SimplePanel filmDetailsPanel;
    private SimplePanel filmInfoPanel;
    private JScrollPane scrollPane;
    private SimplePanel reviewsPanel;
    private SimplePanel reviewsTablePanel;
    private Content film;
    private ImageIcon filmIcon;
    private String infoString;
    public FilmPanel(Content film, ImageIcon icon) {
        super();
        this.film = film;
        this.filmIcon = icon;
        infoString = "<html><body><div><h2>"+film.getTitle()+"</h2>"+
                "<p>Genres: "+film.getGenres()+"</p>"+
                "<p style=\"font-size:10vw\">  Overview: "+film.getOverview()+"</p>"+
                "<p>  Rating: "+film.getAvg_rating()+"</p></div></body></html>";
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        panel.setLayout(new GridLayout(2,1));
        //----------------------------------------------
        filmDetailsPanel = new SimplePanel();
        //filmDetailsPanel.getPanel().add(new JLabel(icon),BorderLayout.WEST);//Add icon
        //filmInfoPanel = new SimplePanel();
        //filmInfoPanel.getPanel().setLayout(new GridLayout(1,1));
        //filmInfoPanel.getPanel().add(new SimpleLabel(infoString).getLabel());
        //filmDetailsPanel.getPanel().add(filmInfoPanel.getPanel(),BorderLayout.CENTER);
        SimpleLabel label = new SimpleLabel(icon,infoString);
        label.getLabel().setIconTextGap(10);
        filmDetailsPanel.getPanel().add(label.getLabel(),BorderLayout.WEST);
        panel.add(filmDetailsPanel.getPanel());




    }

}
