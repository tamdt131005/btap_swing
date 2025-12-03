package com.mycompany.btap.view;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    public HomeView() {
        setTitle("Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcome = new JLabel("Welcome to the application!");
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcome, BorderLayout.CENTER);
    }
}
