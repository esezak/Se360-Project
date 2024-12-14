package com.esezak.client.UI.Elements.Buttons;

import com.esezak.client.UI.Elements.Panels.CenterPanel;
import com.esezak.client.UI.Elements.Panels.FilmPanel;
import com.esezak.server.MovieLookup.Content.Content;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

public class FilmButton extends SimpleButton {


    ImageIcon icon;
    private Content film;
    private CenterPanel centerPanel;
    private String infoString;
    public FilmButton(Content film, CenterPanel centerPanel) {
        super("text");
        this.film = film;
        this.centerPanel = centerPanel;
        infoString = "<html><body><h2>"+film.getTitle()+"</h2>"+
                "<br>Genres: "+film.getGenres()+
                "<br><p style=\"font-size:10vw\">Overview: "+formatOverview()+"</p>"+
                "<br>Rating: "+film.getAvg_rating()+"</body></html>";
        button.setText(infoString);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalTextPosition(SwingConstants.NORTH);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setBackground(Color.gray);
        button.addActionListener(new on_film_button_click());

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
    private String formatOverview(){
        String[] words = film.getOverview().split(" ");
        StringBuilder formatted = new StringBuilder();
        for(int i = 0; i < words.length; i++){
            if(i != 0 && i%10 == 0){
                formatted.append("<br>");
            }
            formatted.append(words[i]+" ");
        }
        this.film.setOverview(formatted.toString());
        return formatted.toString();
    }
    public Content getFilm() {
        return film;
    }
    private class on_film_button_click implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            centerPanel.getPanel().removeAll();
            centerPanel.getPanel().add(new FilmPanel(film,icon).getPanel());
            centerPanel.getPanel().revalidate();
        }
    }
}
