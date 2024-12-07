package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.ConnectButton;
import com.esezak.client.UI.Elements.Buttons.DisconnectButton;
import com.esezak.client.UI.Elements.Buttons.LoginButton;
import com.esezak.client.UI.Elements.Buttons.LogoutButton;
import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.client.UI.Elements.TextFields.PasswordField;
import com.esezak.client.UI.Elements.TextFields.UsernameTextField;

import javax.swing.*;
import java.awt.*;

public class RightFormPanel extends SimplePanel{
    SimpleLabel usernameLabel = new SimpleLabel("Username:");
    SimpleLabel passwordLabel = new SimpleLabel("Password:");
    SimpleLabel connectionLabel = new SimpleLabel("Status:");
    SimpleLabel connectionStatusLabel = new SimpleLabel("Not Connected");
    UsernameTextField usernameTextField = new UsernameTextField();
    PasswordField passwordField = new PasswordField();
    ConnectButton connectButton = new ConnectButton();
    DisconnectButton disconnectButton = new DisconnectButton();
    LoginButton loginButton = new LoginButton(usernameTextField, passwordField);
    LogoutButton logoutButton = new LogoutButton();

    public RightFormPanel() {
        super();
        panel.setPreferredSize(new Dimension(250, 10));
        panel.setLayout(new GridLayout(6,2,5,5));
        panel.add(usernameLabel.getLabel());
        panel.add(usernameTextField.getTextField());
        panel.add(passwordLabel.getLabel());
        panel.add(passwordField.getPasswordField());
        panel.add(connectButton.getButton());
        panel.add(disconnectButton.getButton());
        panel.add(connectionLabel.getLabel());
        panel.add(connectionStatusLabel.getLabel());
        panel.add(loginButton.getButton());
        panel.add(logoutButton.getButton());
        connectionStatusLabel.getLabel().setForeground(Color.red);

    }
}
