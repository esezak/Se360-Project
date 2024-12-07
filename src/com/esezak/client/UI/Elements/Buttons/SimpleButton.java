package com.esezak.client.UI.Elements.Buttons;

import javax.swing.*;

import java.awt.*;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_BORDER;
import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public abstract class SimpleButton {
    public static final Insets BUTTON_MARGINS = new Insets(3, 20, 3, 20);
    JButton button;
    public SimpleButton(String label) {
        button = new JButton(label);
        button.setFocusable(false);
        button.setFont(GLOBAL_FONT);
        button.setMargin(BUTTON_MARGINS);
        button.setBorder(GLOBAL_BORDER);
    }
    public JButton getButton() {
        return button;
    }
}
