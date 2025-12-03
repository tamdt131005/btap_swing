
package com.mycompany.btap.view;

import javax.swing.*;
import java.awt.*;

public class vForgotPassword extends JFrame {

    private JTextField emailField;
    private JButton submitButton;

    public vForgotPassword() {
        setTitle("Forgot Password");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        // Panel for input field
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Enter your email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        // Panel for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        submitButton = new JButton("Submit");
        buttonPanel.add(submitButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    // Add getters if a controller needs them
}
