package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Labels.SimpleLabel;

import java.awt.*;

public class BottomPanel extends SimplePanel {
    SimpleLabel label;
    public BottomPanel() {
        super();
        label = new SimpleLabel("Powered By: TVDB.com");
        label.getLabel().setForeground(Color.BLUE);
        panel.add(label.getLabel());
    }
}
