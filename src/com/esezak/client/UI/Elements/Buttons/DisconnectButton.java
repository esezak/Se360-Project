package com.esezak.client.UI.Elements.Buttons;

public class DisconnectButton extends SimpleButton{
    public DisconnectButton(){
        super("Disconnect");
        button.addActionListener(e -> {
            System.out.println("Disconnect button clicked");
        });
    }
}
