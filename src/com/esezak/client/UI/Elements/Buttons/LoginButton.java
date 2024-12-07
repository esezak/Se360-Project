package com.esezak.client.UI.Elements.Buttons;

import com.esezak.client.UI.Elements.TextFields.PasswordField;
import com.esezak.client.UI.Elements.TextFields.UsernameTextField;

public class LoginButton extends SimpleButton{
    public LoginButton(UsernameTextField username, PasswordField password){
        super("Login");
        button.addActionListener(e -> {
            System.out.println("Login button clicked");
            System.out.println("Username: " + username.getUsername());
            System.out.println("Password: " + password.getPassword());
        });
    }
}
