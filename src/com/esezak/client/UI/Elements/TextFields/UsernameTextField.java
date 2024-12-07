package com.esezak.client.UI.Elements.TextFields;

import javax.swing.*;

public class UsernameTextField extends SimpleTextField {
    public UsernameTextField() {
        super();
    }
    public String getUsername() {
        return textField.getText();
    }
    public JTextField getTextField() {
        return textField;
    }
}
