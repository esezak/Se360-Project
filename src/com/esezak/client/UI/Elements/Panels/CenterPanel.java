package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.FilmButton;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel extends SimplePanel {
    private JScrollPane scrollPane;
    public ArrayList<Content> films = new ArrayList<>();
    public CenterPanel() {
        super();
        panel.setLayout(new GridLayout(0,1));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void retrieveFilms(){
        panel.removeAll();
        for(Content c : films){
            panel.add(new FilmButton(c).getButton());
        }
        panel.revalidate();
    }
    public void testPhoto(){
        panel.add(new FilmButton().getButton());
        panel.revalidate();
    }

}
