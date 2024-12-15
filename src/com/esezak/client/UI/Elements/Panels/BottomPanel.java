package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Labels.SimpleLabel;

import java.awt.*;

public class BottomPanel extends SimplePanel {
    public BottomPanel() {
        super();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        SimpleLabel label = new SimpleLabel("Powered By: TVDB.com");
        label.getLabel().setForeground(Color.BLUE);
        panel.add(label.getLabel());
    }
}
