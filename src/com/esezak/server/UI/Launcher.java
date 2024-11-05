package com.esezak.server.UI;

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ServerMainWindow main = new ServerMainWindow();
            }
        });
    }
}
