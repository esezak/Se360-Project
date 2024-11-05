package com.esezak.client.UI;

import javax.swing.*;
import java.awt.*;

public class ClientMainWindow {
    private JFrame frame;
    private JPanel topPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JLabel connectionStatusLabel;
    private final Font globalFont = new Font("Arial", Font.BOLD, 16);
    private final Insets buttonMargins = new Insets(3, 20, 3, 20);
    private JTextField ipTextField;
    private JTextField usernameTextField;
    private JTextField passwordTextField;


    public ClientMainWindow() {
        initialize();
    }
    private void initialize() {
        setFrame();
        setLeftPanel();
        setRightPanel();
        setTopPanel();
        setBottomPanel();
        setCenterPanel();
    }
    private void setFrame(){
        frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setTitle("Client UI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout(5,5));
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setVisible(true);
    }

    private void setCenterPanel(){
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.GRAY);
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private void setTopPanel(){
        topPanel = new JPanel();
        topPanel.setBackground(Color.GRAY);
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    private void setLeftPanel(){
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createButton("Test"));
        leftPanel.setBackground(Color.GRAY);
        frame.add(leftPanel, BorderLayout.WEST);
    }
    private void setRightPanel(){
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.GRAY);
        rightPanel.setLayout(new GridBagLayout());
        rightPanelForm();
        frame.getContentPane().add(rightPanel, BorderLayout.EAST);
    }
    private void rightPanelForm(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(4, 4, 4, 4);

        //Username
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        rightPanel.add(createLabel("Username:"), gbc);
        gbc.gridx++;
        usernameTextField = createTextField(usernameTextField);
        rightPanel.add(usernameTextField, gbc);

        //Password
        gbc.gridx = 0;gbc.gridy++;
        rightPanel.add(createLabel("Password:"), gbc);
        gbc.gridx++;
        passwordTextField = createTextField(passwordTextField);
        rightPanel.add(passwordTextField, gbc);
        gbc.gridx = 0;gbc.gridy++;

        //Server Ip
        rightPanel.add(createLabel("Server Ip: "),gbc);
        gbc.gridx++;
        ipTextField = createTextField(ipTextField);
        rightPanel.add(ipTextField,gbc);

        //Connect Disconnect Buttons
        gbc.gridx = 0;gbc.gridy++;
        rightPanel.add(createConnectButton("Connect", ipTextField),gbc);
        gbc.gridx++;
        rightPanel.add(createButton("Disconnect"),gbc);
        gbc.gridy++; gbc.gridx = 0;

        //Status indicators
        rightPanel.add(createLabel("Status:"),gbc);
        gbc.gridx++;
        connectionStatusLabel = createLabel("Not Connected");
        rightPanel.add(connectionStatusLabel,gbc);
    }
    private void setBottomPanel(){
        bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.GRAY);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
    private JButton createButton(String text){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(globalFont);
        button.setMargin(buttonMargins);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.addActionListener(e -> {
            System.out.println("clicked");
        });
        return button;
    }
    private JButton createConnectButton(String text, JTextField textField){
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setFont(globalFont);
        button.setMargin(buttonMargins);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.addActionListener(e -> {
            System.out.println(textField.getText());
            connectionStatusLabel.setText("Connecting");
        });
        return button;
    }
    private JButton createButton(String text, String imgPath){
        ImageIcon icon = new ImageIcon(imgPath);
        JButton button = new JButton(text);
        button.setFocusable(false);
        button.setIcon(icon);
        button.setIconTextGap(5);
        button.setFont(globalFont);
        button.setMargin(buttonMargins);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.addActionListener(e -> {
            System.out.println("clicked");
        });
        return button;
    }
    private JLabel createLabel(String text){
        JLabel ipLabel = new JLabel(text);
        ipLabel.setFont(globalFont);
        ipLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return ipLabel;
    }
    private JTextField createTextField(JTextField textField){
        textField = new JTextField(10);
        textField.setFont(globalFont);
        //ipTextField.getBaseline(10, 10);
        return textField;
    }
}
