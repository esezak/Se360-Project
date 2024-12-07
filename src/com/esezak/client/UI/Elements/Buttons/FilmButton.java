package com.esezak.client.UI.Elements.Buttons;

import javax.swing.*;

public class FilmButton extends SimpleButton {
    ImageIcon icon;
    public FilmButton(String filmName, String iconPath) {
        super(filmName);
        icon = new ImageIcon(iconPath);
        button.setIcon(icon);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
    }
}
