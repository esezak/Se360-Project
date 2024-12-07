package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.server.ConnectionManager.Server;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends SimplePanel{
    RightFormPanel rightFormPanel;
    public RightPanel(ServerConnection connection,TopPanel topPanel,LeftPanel leftPanel){
        super();
        rightFormPanel = new RightFormPanel(connection, topPanel, leftPanel);
        panel.setLayout(new GridLayout(3,1));
        panel.add(rightFormPanel.getPanel());
    }
}
