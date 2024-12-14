package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftPanel extends SimplePanel{
    private SimpleButton filmsButton;
    private SimpleButton watchlistButton;
    private CenterPanel centerPanel;
    public LeftPanel(CenterPanel centerPanel) {
        super();
        this.centerPanel = centerPanel;
        filmsButton = new SimpleButton("Search Tab");
        filmsButton.getButton().addActionListener(new FilmsButtonListener());
        watchlistButton = new SimpleButton("Watchlist");
        watchlistButton.getButton().addActionListener(new WatchlistButtonListener());
        panel.setLayout(new GridLayout(18,1,0,1));
        panel.add(filmsButton.getButton());
        panel.add(watchlistButton.getButton());
        setLeftPanelState(false);
    }
    public void setLeftPanelState(boolean state){
        setFilmsButtonState(state);
        setWatchlistButtonState(state);
    }

    public void setFilmsButtonState(boolean state){
        filmsButton.getButton().setEnabled(state);
    }
    public void setWatchlistButtonState(boolean state){
        watchlistButton.getButton().setEnabled(state);
    }
    private class FilmsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            centerPanel.retrieveOldFilms();
        }
    }
    private class WatchlistButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("WatchlistButton clicked");
        }
    }
}
