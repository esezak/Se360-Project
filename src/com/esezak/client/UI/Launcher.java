package com.esezak.client.UI;

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {ClientMainWindow main = new ClientMainWindow();}
        });
    }
}
