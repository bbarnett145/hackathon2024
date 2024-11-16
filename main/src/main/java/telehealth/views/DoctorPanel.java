package telehealth.views;

import telehealth.GUIShell;
import telehealth.models.Reminder;
import telehealth.models.User;
import telehealth.utils.UserManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DoctorPanel extends JPanel {
    private GUIShell guiShell;
    private String doctorEmail;
    private JComboBox<String> allPatientsComboBox;
    private JComboBox<String> patientComboBox;
    private JList<Reminder> reminderList;
    private JTextField reminderField;
    private JButton associateButton;
    private JButton unassociateButton;
    private JButton setReminderButton;

    public DoctorPanel(GUIShell guiShell, String doctorEmail) {
        this.guiShell = guiShell;
        this.doctorEmail = doctorEmail;
        initComponents();
        populateAllPatientsComboBox();
        populateAssociatedPatientsComboBox();
    }

    public void setEmail(String email) {
        this.doctorEmail = email;
        populateAssociatedPatientsComboBox();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        allPatientsComboBox = new JComboBox<>();
        patientComboBox = new JComboBox<>();
        reminderList = new JList<>();
        reminderField = new JTextField(20);
        associateButton = new JButton("Associate");
        unassociateButton = new JButton("Unassociate");
        setReminderButton = new JButton("Set Reminder");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("All Patients:"));
        topPanel.add(allPatientsComboBox);
        topPanel.add(associateButton);
        topPanel.add(new JLabel("Associated Patients:"));
        topPanel.add(patientComboBox);
        topPanel.add(unassociateButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(reminderList), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Reminder:"));
        bottomPanel.add(reminderField);
        bottomPanel.add(setReminderButton);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        associateButton.addActionListener(e -> {
            String patientEmail = (String) allPatientsComboBox.getSelectedItem();
            if (patientEmail != null) {
                UserManager.associatePatientWithDoctor(patientEmail, doctorEmail);
                patientComboBox.addItem(patientEmail);
                JOptionPane.showMessageDialog(this, "Patient associated: " + patientEmail);
            }
        });

        unassociateButton.addActionListener(e -> {
            String patientEmail = (String) patientComboBox.getSelectedItem();
            if (patientEmail != null) {
                UserManager.unassociatePatientWithDoctor(patientEmail, doctorEmail);
                patientComboBox.removeItem(patientEmail);
                JOptionPane.showMessageDialog(this, "Patient unassociated: " + patientEmail);
            }
        });

        patientComboBox.addActionListener(e -> {
            String patientEmail = (String) patientComboBox.getSelectedItem();
            if (patientEmail != null) {
                Set<Reminder> reminders = UserManager.getRemindersForPatient(patientEmail);
                reminderList.setListData(reminders.toArray(new Reminder[0]));
            }
        });

        setReminderButton.addActionListener(e -> {
            String patientEmail = (String) patientComboBox.getSelectedItem();
            String reminderText = reminderField.getText();
            if (patientEmail != null && !reminderText.isEmpty()) {
                Reminder reminder = new Reminder(reminderText);
                UserManager.addReminderToPatient(patientEmail, reminder);
                Set<Reminder> reminders = UserManager.getRemindersForPatient(patientEmail);
                reminderList.setListData(reminders.toArray(new Reminder[0]));
            }
        });
    }

    private void populateAllPatientsComboBox() {
        List<User> patients = UserManager.getAllUsers().stream()
                .filter(user -> "Patient".equals(user.getRole()))
                .collect(Collectors.toList());

        for (User patient : patients) {
            allPatientsComboBox.addItem(patient.getEmail());
        }
    }

    private void populateAssociatedPatientsComboBox() {
        patientComboBox.removeAllItems();
        Set<String> associatedPatients = UserManager.getPatientsForDoctor(doctorEmail);
        for (String patientEmail : associatedPatients) {
            patientComboBox.addItem(patientEmail);
        }
    }
}
