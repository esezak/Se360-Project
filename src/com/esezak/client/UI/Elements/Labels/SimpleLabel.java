package com.esezak.client.UI.Elements.Labels;

import javax.swing.*;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public class SimpleLabel {
    JLabel label;
    public SimpleLabel(String labelText) {
        label = new JLabel(labelText);
        label.setFont(GLOBAL_FONT);
    }

    public JLabel getLabel() {
        return label;
    }
}
