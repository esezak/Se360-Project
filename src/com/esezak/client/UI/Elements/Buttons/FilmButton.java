package com.esezak.client.UI.Elements.Buttons;

import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class FilmButton extends SimpleButton{
    ImageIcon icon;
    private static int total = 1;
    private String filmName;
    private Content film;
    public FilmButton(String filmName, String iconPath) {
        super(filmName);
        this.filmName = filmName+total;
        icon = new ImageIcon(iconPath);
        button.setIcon(icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBackground(Color.gray);
        button.addActionListener(e -> {
            System.out.println("Display Film Name: "+ this.filmName);
        });
        total++;
    }
    public FilmButton(Content film) {
        super(film.getTitle());
        this.film = film;
        this.filmName = film.getTitle();
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBackground(Color.gray);
        setIcon(this.film.getImage_url());
    }
    private void setIcon(String imgpath){
        try{
            URL url = new URL(imgpath);
            icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(170, 250, Image.SCALE_SMOOTH));
            button.setIcon(icon);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public FilmButton(){//Test method
        super("A Film With A Very Very Very Very Long Title");
        String asd = "https://artworks.thetvdb.com/banners/movies/104007/posters/104007.jpg";
        this.film = null;
        this.filmName = null;
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBackground(Color.gray);
        setIcon(asd);
    }
}
