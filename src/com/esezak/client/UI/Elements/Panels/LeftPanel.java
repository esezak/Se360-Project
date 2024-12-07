package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.FilmsButton;
import com.esezak.client.UI.Elements.Buttons.WatchlistButton;

import javax.swing.*;
import java.awt.*;

public class LeftPanel extends SimplePanel{
    private FilmsButton filmsButton;
    private WatchlistButton watchlistButton;
    public LeftPanel() {
        super();
        filmsButton = new FilmsButton();
        watchlistButton = new WatchlistButton();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(filmsButton.getButton());
        panel.add(watchlistButton.getButton());
    }
}
