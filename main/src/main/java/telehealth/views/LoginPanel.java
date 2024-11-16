package telehealth.views;

import telehealth.models.Doctor;
import telehealth.models.User;
import telehealth.utils.UserManager;
import telehealth.GUIShell;

import javax.swing.*;

public class LoginPanel extends BasePanel {

    public LoginPanel(GUIShell guiShell) {
        super(guiShell);

        // Adding components
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField(20);
        JButton loginButton = new JButton("Login");

        addComponent(emailLabel, 0, 0, 1, 1, 0.5, 0.5);
        addComponent(emailField, 1, 0, 1, 1, 0.5, 0.5);
        addComponent(passwordLabel, 0, 1, 1, 1, 0.5, 0.5);
        addComponent(passwordField, 1, 1, 1, 1, 0.5, 0.5);
        addComponent(loginButton, 0, 2, 1, 1, 0.5, 0.5);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            try {
                User user = UserManager.getUserByEmail(email);
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    if(user instanceof Doctor) {
                        guiShell.setUserRole("Doctor");
                    }
                    guiShell.hideLoginSignupPanels(user instanceof Doctor ? "Patient" : "Doctor", email, user.getEmail());
                    guiShell.showDoctorPanel(user.getEmail());
                } else {
                    JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error reading user information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
