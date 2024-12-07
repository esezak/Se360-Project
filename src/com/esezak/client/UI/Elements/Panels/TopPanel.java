package com.esezak.client.UI.Elements.Panels;

import com.esezak.client.ConnectionManager.ServerConnection;
import com.esezak.client.UI.Elements.Buttons.SimpleButton;
import com.esezak.client.UI.Elements.Labels.SimpleLabel;
import com.esezak.client.UI.Elements.TextFields.SimpleTextField;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopPanel extends SimplePanel {
    private SimpleButton searchButton;
    private SimpleTextField searchTextField = new SimpleTextField(20);
    private SimpleLabel label = new SimpleLabel("Search:");
    private CenterPanel centerPanel;
    private ServerConnection connection;
    public TopPanel(CenterPanel centerPanel, ServerConnection connection) {
        super();
        this.connection = connection;
        this.centerPanel = centerPanel;
        searchButton = new SimpleButton("Search");
        searchButton.getButton().addActionListener(new onSearchButtonClick());
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(label.getLabel());
        panel.add(searchTextField.getTextField());
        panel.add(searchButton.getButton());
        setSearchStatus(false);
    }
    public void setSearchStatus(boolean status) {
        setSearchTextFieldState(status);
        setSearchButtonState(status);
    }
    public void setSearchButtonState(boolean state) {
        searchButton.getButton().setEnabled(state);
    }
    public void setSearchTextFieldState(boolean state) {
        searchTextField.getTextField().setEnabled(state);
    }
    private class onSearchButtonClick implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!searchTextField.getText().isEmpty()) {
                //centerPanel.films = connection.sendFilmQuery(searchTextField.getText()).getMovies();
            }
            //centerPanel.retrieveFilms();
            centerPanel.testPhoto();
        }
    }

}
