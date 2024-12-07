package com.esezak.client.UI.Elements.Panels;

import javax.swing.*;
import java.awt.*;

public class RightPanel extends SimplePanel{
    RightFormPanel rightFormPanel;
    public RightPanel(){
        super();
        rightFormPanel = new RightFormPanel();
        panel.setLayout(new GridLayout(3,1));
        panel.add(rightFormPanel.getPanel());
    }
}
