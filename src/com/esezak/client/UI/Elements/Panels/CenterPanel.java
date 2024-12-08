package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.FilmButton;
import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel extends SimplePanel implements Runnable{
    private JScrollPane scrollPane;
    public ArrayList<Content> films = new ArrayList<>();
    private Content toBeAdded;
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

    public void retrieveFilms() {
        panel.removeAll();
        Thread t = null;
        for(Content c : films){
            toBeAdded = c;
            t = new Thread(this);
            t.start();
            try {
                t.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        panel.add(new FilmButton(toBeAdded).getButton());
        panel.revalidate();
        System.out.println("Process finished");
    }
}
