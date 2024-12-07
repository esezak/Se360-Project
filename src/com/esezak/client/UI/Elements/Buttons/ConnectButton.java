package com.esezak.client.UI.Elements.Buttons;

public class ConnectButton extends SimpleButton{
    public ConnectButton() {
        super("Connect");
        button.addActionListener(e -> {
            System.out.println("Connect button clicked");
        });
    }
}
