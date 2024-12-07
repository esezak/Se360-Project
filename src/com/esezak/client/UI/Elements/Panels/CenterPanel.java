package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.FilmButton;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;

import javax.swing.*;
import java.awt.*;

public class CenterPanel extends SimplePanel {
    public CenterPanel() {
        super();
        panel.setLayout(new FlowLayout());
        panel.add(new FilmButton("test","Posters/000.jpg").getButton());
    }
}
