package com.esezak.client.UI.Elements;

import javax.swing.*;


import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public class PasswordField {
    JPasswordField passwordField;
    public PasswordField() {
        passwordField = new JPasswordField();
        passwordField.setFont(GLOBAL_FONT);
    }
    public JPasswordField getPasswordField() {
        return passwordField;
    }
    public String getPassword() {
        return passwordField.getText();
    }
}
