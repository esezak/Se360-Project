package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.FilmButton;
import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CenterPanel extends SimplePanel implements Runnable{
    private JScrollPane scrollPane;
    private ArrayList<Content> films = new ArrayList<>();
    private ArrayList<FilmButton> buttons = new ArrayList<>();
    private Content toBeAdded;
    private ClientMainWindow clientMainWindow;
    public CenterPanel(ClientMainWindow clientMainWindow) {
        super();
        this.clientMainWindow = clientMainWindow;
        panel.setLayout(new GridLayout(0,1));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(panel);
        scrollPane.createVerticalScrollBar();
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void retrieveNewFilms() {
        panel.removeAll();
        Thread t = null;
        for(Content c : films){
            toBeAdded = c;
            t = new Thread(this);
            t.start();
            try {
                t.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void retrieveOldFilms() {
        panel.removeAll();
        for(FilmButton f : buttons){
            panel.add(f.getButton());
        }
        panel.revalidate();
        panel.repaint();
    }
    @Override
    public void run() {
        FilmButton asd = new FilmButton(toBeAdded,this);
        buttons.add(asd);
        panel.add(asd.getButton());
        panel.revalidate();
        panel.repaint();
        //System.out.println("Process finished");
    }
    public void setFilms(ArrayList<Content> films){
        this.films = films;
    }
    public void resetFilms(){
        films.clear();
    }
    public void resetButtons(){
        buttons.clear();
    }
    public ClientMainWindow getClientMainWindow() {
        return clientMainWindow;
    }
}
