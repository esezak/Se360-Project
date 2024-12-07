package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.server.ConnectionManager.Server;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends SimplePanel{
    RightFormPanel rightFormPanel;
    public RightPanel(ServerConnection connection){
        super();
        rightFormPanel = new RightFormPanel(connection);
        panel.setLayout(new GridLayout(3,1));
        panel.add(rightFormPanel.getPanel());
    }
}
