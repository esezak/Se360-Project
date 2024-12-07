package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.client.UI.Elements.TextFields.SimpleTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends SimplePanel {
    private SimpleButton searchButton;
    private SimpleTextField searchTextField = new SimpleTextField(20);
    private SimpleLabel label = new SimpleLabel("Search:");
    private CenterPanel centerPanel;

    public TopPanel(CenterPanel centerPanel) {
        super();
        this.centerPanel = centerPanel;
        searchButton = new SimpleButton("Search");
        searchButton.getButton().addActionListener(new onSearchButtonClick());
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(label.getLabel());
        panel.add(searchTextField.getTextField());
        panel.add(searchButton.getButton());
    }
    private class onSearchButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            centerPanel.retrieveFilms();
        }
    }

}
