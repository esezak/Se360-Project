package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.server.ConnectionManager.Response;
import org.json.JSONArray;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeftPanel extends SimplePanel{
    private final SimpleButton filmsButton;
    private final SimpleButton watchlistButton;
    private final CenterPanel centerPanel;
    private final ClientMainWindow clientMainWindow;
    private String watchlist;
    private WatchlistPanel watchlistPanel;
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
    public void setWatchlist(){
        Response response = clientMainWindow.connection.getWatchlistRequest(clientMainWindow.getUsername());
        watchlist = response.getData();
    }
    public void deleteOldUserData(){
        watchlist = null;
        watchlistPanel = null;
    }
    public boolean isMovieInWatchlist(String movieID){
        if(watchlist == null){
            return false;
        }
        JSONArray jsonArray = new JSONArray(watchlist);
        for(int i = 0; i < jsonArray.length(); i++){
            if(jsonArray.getJSONObject(i).getString("movie_id").equals(movieID)){
                return true;
            }
        }
        return false;
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
            centerPanel.getPanel().removeAll();
            if(watchlist == null){
                setWatchlist();
            }
            if(watchlistPanel == null){
                watchlistPanel =  new WatchlistPanel(watchlist,clientMainWindow);
            }
            centerPanel.getPanel().add(watchlistPanel.getPanel());
            centerPanel.getPanel().revalidate();
        }
    }

}
