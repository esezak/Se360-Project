package com.esezak.client.UI.Elements.TextFields;

import javax.swing.*;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public abstract class SimpleTextField {
    JTextField textField;
    public SimpleTextField() {
        textField = new JTextField(10);
        textField.setEditable(true);
        textField.setFont(GLOBAL_FONT);
    }

    public JTextField getTextField() {
        return textField;
    }
    public String getText() {
        return textField.getText();
    }
}
