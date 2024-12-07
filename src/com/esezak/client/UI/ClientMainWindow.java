package com.esezak.client.UI;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.client.UI.Elements.Panels.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static com.esezak.client.UI.Elements.Buttons.SimpleButton.BUTTON_MARGINS;

public class ClientMainWindow {
    private JFrame frame;
    private TopPanel topPanel;
    private LeftPanel leftPanel;
    private RightPanel rightPanel;
    private CenterPanel centerPanel;
    private BottomPanel bottomPanel;
    public static final Font GLOBAL_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Border GLOBAL_BORDER = BorderFactory.createEmptyBorder(5,5,5,5);
    private ServerConnection connection;
    private boolean isConencted = false;
    private boolean isLoggedIn = false;


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
        frame.setResizable(false);
        frame.setLayout(new BorderLayout(5,5));
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setVisible(true);
    }

    private void setCenterPanel(){
        centerPanel = new CenterPanel();
        frame.getContentPane().add(centerPanel.getPanel(), BorderLayout.CENTER);
    }

    private void setTopPanel(){
        topPanel = new TopPanel();
        frame.getContentPane().add(topPanel.getPanel(), BorderLayout.NORTH);
    }

    private void setLeftPanel(){
        leftPanel = new LeftPanel();
        frame.add(leftPanel.getPanel(), BorderLayout.WEST);
    }
    private void setRightPanel(){
        rightPanel = new RightPanel();
        frame.getContentPane().add(rightPanel.getPanel(), BorderLayout.EAST);
    }

    private void setBottomPanel(){
        bottomPanel = new BottomPanel();
        frame.getContentPane().add(bottomPanel.getPanel(), BorderLayout.SOUTH);
    }
}
