package com.esezak.client.UI.Elements.Buttons;

public class LogoutButton extends SimpleButton{
    public LogoutButton(){
        super("Logout");
        button.addActionListener(e -> {
            System.out.println("Loguot button clicked");
        });
    }
}
