package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.client.UI.ClientMainWindow;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.SimpleLabel;
import com.esezak.client.UI.Elements.SimpleTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.esezak.client.UI.ClientMainWindow.GLOBAL_FONT;

public class RightPanel extends SimplePanel{
    private SimplePanel rightFormPanel;
    private SimpleLabel usernameLabel = new SimpleLabel("Username:");
    private SimpleLabel passwordLabel = new SimpleLabel("Password:");
    private SimpleLabel connectionLabel = new SimpleLabel("Server:");
    public SimpleLabel connectionStatusLabel = new SimpleLabel("Not Connected");
    private SimpleLabel loginLabel = new SimpleLabel("Login Status:");
    public SimpleLabel loginStatusLabel = new SimpleLabel("Not Logged in");
    public SimpleTextField usernameTextField = new SimpleTextField();
    private JPasswordField passwordField;
    public SimpleButton connectButton = new SimpleButton("Connect");
    public SimpleButton disconnectButton = new SimpleButton("Disconnect");
    public SimpleButton loginButton = new SimpleButton("Login");
    public SimpleButton logoutButton = new SimpleButton("Logout");
    private TopPanel topPanel;
    private LeftPanel leftPanel;
    private ServerConnection connection;
    private ClientMainWindow clientMainWindow;
    public RightPanel(ClientMainWindow clientMainWindow){
        super();
        rightFormPanel = new SimplePanel();
        panel.setLayout(new GridLayout(3,1));
        passwordField = new JPasswordField();
        passwordField.setFont(GLOBAL_FONT);
        this.connection = clientMainWindow.connection;
        this.topPanel = clientMainWindow.topPanel;
        this.leftPanel = clientMainWindow.leftPanel;
        this.clientMainWindow = clientMainWindow;
        this.rightFormPanel.getPanel().setPreferredSize(new Dimension(250, 10));
        this.rightFormPanel.getPanel().setLayout(new GridLayout(6,2,5,5));
        this.rightFormPanel.getPanel().add(usernameLabel.getLabel());
        this.rightFormPanel.getPanel().add(usernameTextField.getTextField());
        this.rightFormPanel.getPanel().add(passwordLabel.getLabel());
        this.rightFormPanel.getPanel().add(passwordField);
        this.rightFormPanel.getPanel().add(connectButton.getButton());
        this.rightFormPanel.getPanel().add(disconnectButton.getButton());
        this.rightFormPanel.getPanel().add(connectionLabel.getLabel());
        this.rightFormPanel.getPanel().add(connectionStatusLabel.getLabel());
        this.rightFormPanel.getPanel().add(loginButton.getButton());
        this.rightFormPanel.getPanel().add(logoutButton.getButton());
        this.rightFormPanel.getPanel().add(loginLabel.getLabel());
        this.rightFormPanel.getPanel().add(loginStatusLabel.getLabel());
        loginStatusLabel.getLabel().setForeground(Color.RED);
        connectionStatusLabel.getLabel().setForeground(Color.red);
        connectButton.getButton().addActionListener(new RightPanel.ConnectButtonListener());
        disconnectButton.getButton().addActionListener(new RightPanel.DisconnectButtonListener());
        disconnectButton.getButton().setEnabled(false);
        loginButton.getButton().addActionListener(new RightPanel.LoginButtonListener());
        loginButton.getButton().setEnabled(false);
        logoutButton.getButton().addActionListener(new RightPanel.LogoutButtonListener());
        logoutButton.getButton().setEnabled(false);
        panel.add(rightFormPanel.getPanel());
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
                clientMainWindow.isConnected = true;

            }else{
                System.err.println("Could not connect to server");
            }
        }
    }
    private class DisconnectButtonListener implements ActionListener {
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
                clientMainWindow.isConnected = false;
                clientMainWindow.isLoggedIn = false;
            }else{
                System.err.println("Could not disconnect from server");
            }
        }
    }
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(connection.sendLoginRequest(usernameTextField.getText(), getPassword())){
                System.out.println("Logged in");
                clientMainWindow.setUsername(usernameTextField.getText());
                clientMainWindow.setPassword(getPassword());
                disconnectButton.getButton().setEnabled(false);
                loginButton.getButton().setEnabled(false);
                logoutButton.getButton().setEnabled(true);
                connectButton.getButton().setEnabled(false);
                leftPanel.setWatchlistButtonState(true);
                loginStatusLabel.getLabel().setForeground(Color.green);
                loginStatusLabel.getLabel().setText("Logged in");
                usernameTextField.getTextField().setEditable(false);
                passwordField.setEditable(false);
                clientMainWindow.isConnected = true;
                clientMainWindow.isLoggedIn = true;
                clientMainWindow.leftPanel.setWatchlist();
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
                clientMainWindow.setUsername(null);
                clientMainWindow.setPassword(null);
                disconnectButton.getButton().setEnabled(true);
                loginButton.getButton().setEnabled(true);
                logoutButton.getButton().setEnabled(false);
                leftPanel.setWatchlistButtonState(false);
                loginStatusLabel.getLabel().setText("Not Logged in");
                loginStatusLabel.getLabel().setForeground(Color.red);
                usernameTextField.getTextField().setEditable(true);
                passwordField.setEditable(true);
                clientMainWindow.isConnected = true;
                clientMainWindow.isLoggedIn = false;
                clientMainWindow.leftPanel.deleteOldUserData();
            }
        }
    }
    public String getPassword() {
        return passwordField.getText();
    }
}
