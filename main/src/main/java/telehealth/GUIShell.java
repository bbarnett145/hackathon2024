package telehealth;

import telehealth.utils.UserManager;
import telehealth.views.SignupPanel;
import telehealth.views.LoginPanel;
import telehealth.views.NotePanel;
import telehealth.views.WellnessPanel;
import telehealth.views.MessagePanel;
import telehealth.views.DoctorPanel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUIShell {
    private JFrame frame;
    private JPanel navPanel;
    private JButton toggleButton;
    private JButton notesButton;
    private JButton wellnessButton;
    private JButton messageButton;
    private JButton doctorButton;
    private JButton exitButton;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private boolean showingLogin = true;
    private String userRole;
    private String userEmail;
    private String userName;
    private DoctorPanel doctorPanel = new DoctorPanel(this, null);

    public GUIShell() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load custom font
        loadCustomFonts();

        // Load user data
        UserManager.loadData();

        // Initialize the main frame
        frame = new JFrame("Telehealth App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        // Create a navigation bar
        navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 6));

        toggleButton = new JButton("Sign Up");
        notesButton = new JButton("Notes");
        wellnessButton = new JButton("Wellness");
        messageButton = new JButton("Messages");
        doctorButton = new JButton("Doctor Panel");
        exitButton = new JButton("Logout");

        // Add buttons
        navPanel.add(toggleButton);
        navPanel.add(notesButton);
        navPanel.add(wellnessButton);
        navPanel.add(messageButton);
        navPanel.add(doctorButton);
        navPanel.add(exitButton);

        // Add navigation panel
        frame.add(navPanel, BorderLayout.NORTH);

        // Initialize cards
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create and add panels
        SignupPanel signupPanel = new SignupPanel(this);
        LoginPanel loginPanel = new LoginPanel(this);
        NotePanel notePanel = new NotePanel();
        WellnessPanel wellnessPanel = new WellnessPanel(this);
        MessagePanel messagePanel = new MessagePanel(this);

        cardPanel.add(loginPanel, "Login");
        cardPanel.add(signupPanel, "Signup");
        cardPanel.add(notePanel, "Notes");
        cardPanel.add(wellnessPanel, "Wellness");
        cardPanel.add(messagePanel, "Messages");
        cardPanel.add(doctorPanel, "Doctor");

        frame.add(cardPanel, BorderLayout.CENTER);

        // Show login panel by default
        cardLayout.show(cardPanel, "Login");

        // Add action listeners to buttons
        toggleButton.addActionListener(e -> {
            if (showingLogin) {
                cardLayout.show(cardPanel, "Signup");
                toggleButton.setText("Login");
            } else {
                cardLayout.show(cardPanel, "Login");
                toggleButton.setText("Sign Up");
            }
            showingLogin = !showingLogin;
        });

        notesButton.addActionListener(e -> cardLayout.show(cardPanel, "Notes"));
        wellnessButton.addActionListener(e -> cardLayout.show(cardPanel, "Wellness"));
        messageButton.addActionListener(e -> cardLayout.show(cardPanel, "Messages"));
        messageButton.addActionListener(e -> messagePanel.populateRecipientComboBox());
        doctorButton.addActionListener(e -> cardLayout.show(cardPanel, "Doctor"));
        exitButton.addActionListener(e -> System.exit(0));

        // Disable navigation buttons initially
        setNavigationButtonsEnabled(false);

        frame.setVisible(true);

        // Save user data on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UserManager.saveData();
        }));
    }

    private void loadCustomFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/telehealth/fonts/SF-Pro-Display-Regular.otf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/main/java/telehealth/fonts/SF-Pro-Display-Bold.otf")));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }

    
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void hideLoginSignupPanels(String role, String email, String name) {
        toggleButton.setVisible(false);
        cardLayout.show(cardPanel, "Notes");
        setNavigationButtonsEnabled(true);
        this.userRole = role;
        this.userEmail = email;
        this.userName = name;
        doctorButton.setVisible("Doctor".equals(role));
    }

    private void setNavigationButtonsEnabled(boolean enabled) {
        notesButton.setEnabled(enabled);
        wellnessButton.setEnabled(enabled);
        messageButton.setEnabled(enabled);
        doctorButton.setEnabled(enabled);
        exitButton.setEnabled(enabled);
    }

    public String getUserRole() {
        return userRole;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GUIShell window = new GUIShell();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showDoctorPanel(String doctorEmail) {
        doctorPanel.setEmail(doctorEmail);
    }
}
