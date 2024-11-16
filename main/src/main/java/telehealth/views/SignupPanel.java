package telehealth.views;

import telehealth.models.User;
import telehealth.models.Doctor;
import telehealth.utils.UserManager;
import telehealth.GUIShell;

import javax.swing.*;

public class SignupPanel extends BasePanel {

    public SignupPanel(GUIShell guiShell) {
        super(guiShell);

        // Adding components
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(20);
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField(20);
        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Patient", "Doctor"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        JButton signupButton = new JButton("Sign Up");

        addComponent(nameLabel, 0, 0, 1, 1, 0.5, 0.5);
        addComponent(nameField, 1, 0, 1, 1, 0.5, 0.5);
        addComponent(emailLabel, 0, 1, 1, 1, 0.5, 0.5);
        addComponent(emailField, 1, 1, 1, 1, 0.5, 0.5);
        addComponent(passwordLabel, 0, 2, 1, 1, 0.5, 0.5);
        addComponent(passwordField, 1, 2, 1, 1, 0.5, 0.5);
        addComponent(roleLabel, 0, 3, 1, 1, 0.5, 0.5);
        addComponent(roleComboBox, 1, 3, 1, 1, 0.5, 0.5);
        addComponent(signupButton, 1, 4, 1, 1, 0.5, 0.5);

        signupButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String role = (String) roleComboBox.getSelectedItem();
            String password = new String(passwordField.getText());
            try {
                User newUser;
                if(role == "Doctor") {
                    newUser = new Doctor(name, email, password);
                }
                else {
                    newUser = new User(name, email, role, password);
                }
                UserManager.addUser(newUser);
                JOptionPane.showMessageDialog(this, "User registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving user information: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
