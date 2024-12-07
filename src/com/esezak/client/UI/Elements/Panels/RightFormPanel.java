package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;

import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.client.UI.Elements.TextFields.PasswordField;
import com.esezak.client.UI.Elements.TextFields.SimpleTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RightFormPanel extends SimplePanel{
    SimpleLabel usernameLabel = new SimpleLabel("Username:");
    SimpleLabel passwordLabel = new SimpleLabel("Password:");
    SimpleLabel connectionLabel = new SimpleLabel("Server:");
    SimpleLabel connectionStatusLabel = new SimpleLabel("Not Connected");
    SimpleLabel loginLabel = new SimpleLabel("Login Status:");
    SimpleLabel loginStatusLabel = new SimpleLabel("Not Logged in");
    SimpleTextField usernameTextField = new SimpleTextField();
    PasswordField passwordField = new PasswordField();
    SimpleButton connectButton = new SimpleButton("Connect");
    SimpleButton disconnectButton = new SimpleButton("Disconnect");
    SimpleButton loginButton = new SimpleButton("Login");
    SimpleButton logoutButton = new SimpleButton("Logout");
    TopPanel topPanel;
    LeftPanel leftPanel;
    ServerConnection connection;

    public RightFormPanel(ServerConnection connection, TopPanel topPanel,LeftPanel leftPanel) {
        super();
        this.connection = connection;
        this.topPanel = topPanel;
        this.leftPanel = leftPanel;
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
        panel.add(loginLabel.getLabel());
        panel.add(loginStatusLabel.getLabel());
        loginStatusLabel.getLabel().setForeground(Color.RED);
        connectionStatusLabel.getLabel().setForeground(Color.red);
        connectButton.getButton().addActionListener(new ConnectButtonListener());
        disconnectButton.getButton().addActionListener(new DisconnectButtonListener());
        disconnectButton.getButton().setEnabled(false);
        loginButton.getButton().addActionListener(new LoginButtonListener());
        loginButton.getButton().setEnabled(false);
        logoutButton.getButton().addActionListener(new LogoutButtonListener());
        logoutButton.getButton().setEnabled(false);
    }
    private class ConnectButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.setConnection()){
                System.out.println("Connection established");
                connectionStatusLabel.getLabel().setForeground(Color.GREEN);
                connectionStatusLabel.getLabel().setText("Connected");
                connectButton.getButton().setEnabled(false);
                logoutButton.getButton().setEnabled(false);
                disconnectButton.getButton().setEnabled(true);
                loginButton.getButton().setEnabled(true);
                leftPanel.setFilmsButtonState(true);
                topPanel.setSearchStatus(true);
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
                loginButton.getButton().setEnabled(false);
                logoutButton.getButton().setEnabled(false);
                connectionStatusLabel.getLabel().setForeground(Color.red);
                connectionStatusLabel.getLabel().setText("Disconnected");
                topPanel.setSearchStatus(false);
                leftPanel.setFilmsButtonState(false);
            }else{
                System.err.println("Could not disconnect from server");
            }
        }
    }
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.sendLoginRequest(usernameTextField.getText(), passwordField.getPassword())){
                System.out.println("Logged in");
                disconnectButton.getButton().setEnabled(false);
                loginButton.getButton().setEnabled(false);
                logoutButton.getButton().setEnabled(true);
                connectButton.getButton().setEnabled(false);
                leftPanel.setWatchlistButtonState(true);
                loginStatusLabel.getLabel().setForeground(Color.green);
                loginStatusLabel.getLabel().setText("Logged in");
                usernameTextField.getTextField().setEditable(false);
                passwordField.getPasswordField().setEditable(false);
            }else{
                System.err.println("Could not login");
            }
        }
    }
    private class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.sendLogoutRequest()){
                System.out.println("Logged out");
                disconnectButton.getButton().setEnabled(true);
                loginButton.getButton().setEnabled(true);
                logoutButton.getButton().setEnabled(false);
                leftPanel.setWatchlistButtonState(false);
                loginStatusLabel.getLabel().setText("Not Logged in");
                loginStatusLabel.getLabel().setForeground(Color.red);
                usernameTextField.getTextField().setEditable(true);
                passwordField.getPasswordField().setEditable(true);
            }
        }
    }
}
