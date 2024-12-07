package com.esezak.client.UI.Elements.Buttons;

public class WatchlistButton extends SimpleButton {
    public WatchlistButton() {
        super("Watchlist");
        button.setEnabled(false);
        button.addActionListener(e -> {
            System.out.println("Watchlist button clicked");
        });
    }
}
