package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.FilmButton;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel extends SimplePanel {
    private JScrollPane scrollPane;
    private ArrayList<FilmButton> films = new ArrayList<>();
    public CenterPanel() {
        super();
        //panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setLayout(new GridLayout(0,5));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();

        System.out.println("test");
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void retrieveFilms(){
            films.add(new FilmButton("test: ","Posters/000.jpg"));
            panel.add(films.getLast().getButton());
        panel.revalidate();
    }
}
