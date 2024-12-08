package com.esezak.client.UI.Elements.Buttons;

import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class FilmButton extends SimpleButton{
    ImageIcon icon;
    private Content film;
    public FilmButton(Content film) {
        super(film.getTitle());
        this.film = film;
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
            System.err.println("could not get image URL");
        }
    }
}
