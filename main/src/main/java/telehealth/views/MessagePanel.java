package telehealth.views;

import telehealth.GUIShell;
import telehealth.models.Message;
import telehealth.models.User;
import telehealth.utils.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public class MessagePanel extends BasePanel {
    private GUIShell guiShell;
    private JTextArea messageArea;
    private JTextField messageField;
    private JButton sendButton;
    private JButton importButton;
    private JComboBox<String> recipientComboBox;
    private JLabel newMessagesLabel;
    private boolean isComboBoxInitialized = false;

    public MessagePanel(GUIShell guiShell) {
        super(guiShell);
        this.guiShell = guiShell;
        initComponents();
        populateRecipientComboBox();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        recipientComboBox = new JComboBox<>();
        recipientComboBox.addActionListener(e -> {
            if (isComboBoxInitialized) {
                loadMessages();
            }
        });
        add(recipientComboBox, BorderLayout.NORTH);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        newMessagesLabel = new JLabel("New Messages");
        newMessagesLabel.setForeground(Color.BLUE);
        newMessagesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(newMessagesLabel, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        bottomPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> sendMessage());
        bottomPanel.add(sendButton, BorderLayout.EAST);

        importButton = new JButton("Import");
        importButton.addActionListener(e -> displayImportMenu());
        bottomPanel.add(importButton, BorderLayout.WEST);
        importButton.setVisible(false);

        add(bottomPanel, BorderLayout.SOUTH);

        // Add mouse listener to hide new messages label when clicked
        messageArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newMessagesLabel.setVisible(false);
            }
        });
    }

    public void displayImportMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem importNotesItem = new JMenuItem("Import Notes");
        JMenuItem importCarePlansItem = new JMenuItem("Import Care Plans");
        popupMenu.add(importNotesItem);
        popupMenu.add(importCarePlansItem);
        popupMenu.show(importButton, 0, importButton.getHeight());
    }

    public void populateRecipientComboBox() {
        recipientComboBox.removeAllItems();
        if ("Doctor".equals(guiShell.getUserRole())) {
            Collection<String> patients = UserManager.getPatientsForDoctor(guiShell.getUserEmail());
            for (String patientEmail : patients) {
                recipientComboBox.addItem(patientEmail);
                importButton.setVisible(true);
            }
            Collection<User> users = UserManager.getAllUsers();
            for (User user : users) {
                if ("Doctor".equals(user.getRole())) {
                    recipientComboBox.addItem(user.getEmail());
                }
            }
        } else if ("Patient".equals(guiShell.getUserRole())) {
            Collection<String> doctors = UserManager.getDoctorsForPatient(guiShell.getUserEmail());
            for (String doctorEmail : doctors) {
                recipientComboBox.addItem(doctorEmail);
            }
        }
        isComboBoxInitialized = true;
    }

    private void sendMessage() {
        String recipientEmail = (String) recipientComboBox.getSelectedItem();
        String messageText = messageField.getText();
        if (recipientEmail != null && !messageText.isEmpty()) {
            UserManager.sendMessage(guiShell.getUserEmail(), recipientEmail, messageText);
            messageArea.append("You: " + messageText + "\n");
            messageField.setText("");
        }
    }

    private void loadMessages() {
        String recipientEmail = (String) recipientComboBox.getSelectedItem();
        if (recipientEmail != null) {
            messageArea.setText("");
            List<Message> messages = UserManager.getMessages(guiShell.getUserEmail(), recipientEmail);
            for (Message message : messages) {
                messageArea.append(message.getFromEmail() + ": " + message.getMessageText() + "\n");
            }
        }
    }


    /*
    private void checkForNewMessages() {
        String recipientEmail = (String) recipientComboBox.getSelectedItem();
        if (recipientEmail != null) {
            List<Message> messages = UserManager.getMessages(guiShell.getUserEmail(), recipientEmail);
            if (!messages.isEmpty()) {
                SwingUtilities.invokeLater(() -> {
                    newMessagesLabel.setVisible(true);
                    loadMessages();
                });
            }
        }
    }
        */
}
