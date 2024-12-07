package com.esezak.client.UI.Elements.Buttons;

public class FilmsButton extends SimpleButton{
    public FilmsButton() {
        super("Films");
        button.addActionListener(e -> {
            System.out.println("Films button clicked");
        });
    }
}
