package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.server.ConnectionManager.Response;
import com.esezak.server.MovieLookup.Content.Content;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LeftPanel extends SimplePanel{
    public SimpleButton filmsButton;
    public SimpleButton watchlistButton;
    private CenterPanel centerPanel;
    private ArrayList<Content> watchlist = null;
    private ClientMainWindow clientMainWindow;
    public LeftPanel(ClientMainWindow clientMainWindow) {
        super();
        this.centerPanel = clientMainWindow.centerPanel;
        this.clientMainWindow = clientMainWindow;
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
            Response response = clientMainWindow.connection.getWatchlistRequest(clientMainWindow.getUsername());
            centerPanel.getPanel().removeAll();
            String data = null;
            if(response != null){
                data = response.getData();
            }
            centerPanel.getPanel().add(new WatchlistPanel(data,clientMainWindow).getPanel());
            centerPanel.getPanel().revalidate();
        }
    }
}
