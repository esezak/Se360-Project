package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.client.UI.Elements.Buttons.LoginButton;
import com.esezak.client.UI.Elements.Buttons.LogoutButton;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.client.UI.Elements.TextFields.PasswordField;
import com.esezak.client.UI.Elements.TextFields.UsernameTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightFormPanel extends SimplePanel{
    SimpleLabel usernameLabel = new SimpleLabel("Username:");
    SimpleLabel passwordLabel = new SimpleLabel("Password:");
    SimpleLabel connectionLabel = new SimpleLabel("Status:");
    SimpleLabel connectionStatusLabel = new SimpleLabel("Not Connected");
    UsernameTextField usernameTextField = new UsernameTextField();
    PasswordField passwordField = new PasswordField();
    SimpleButton connectButton = new SimpleButton("Connect");
    SimpleButton disconnectButton = new SimpleButton("Disconnect");
    LoginButton loginButton = new LoginButton(usernameTextField, passwordField);
    LogoutButton logoutButton = new LogoutButton();
    ServerConnection connection;

    public RightFormPanel(ServerConnection connection) {
        super();
        this.connection = connection;
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
        connectButton.getButton().addActionListener(new ConnectButtonListener());
        disconnectButton.getButton().addActionListener(new DisconnectButtonListener());
        disconnectButton.getButton().setEnabled(false);
    }
    private class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.setConnection()){
                System.out.println("Connection established");
                connectButton.getButton().setEnabled(false);
                disconnectButton.getButton().setEnabled(true);
            }else{
                System.err.println("Could not connect to server");
            }
        }
    }
    private class DisconnectButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.sendDisconnectRequest()){
                System.out.println("Disconnected from server");
                connectButton.getButton().setEnabled(true);
                disconnectButton.getButton().setEnabled(false);
            }else{
                System.err.println("Could not disconnect from server");
            }
        }
    }
}
