package com.esezak.server.UI;

import javax.swing.*;
import java.awt.*;

public class ServerMainWindow {
    private JFrame frame;
    private JPanel centerPanel;
    private Font globalFont = new Font("Arial", Font.BOLD, 16);


    public ServerMainWindow() {
        initialize();
        setCenterPanel();
    }
    private void initialize() {
        frame = new JFrame();
        frame.setSize(400, 240);
        frame.setTitle("Server UI");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);

    }

    private void setCenterPanel(){
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.GRAY);
        frame.getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

}
