package telehealth.views;

import telehealth.GUIShell;

import javax.swing.*;

public class WellnessPanel extends BasePanel {

    public WellnessPanel(GUIShell guiShell) {
        super(guiShell);

        JLabel emergencyLabel = new JLabel("Emergency Services:");
        JButton contactEmergencyButton = new JButton("Contact Emergency");

        JLabel resourcesLabel = new JLabel("Resources:");
        JButton viewResourcesButton = new JButton("View Resources");

        addComponent(emergencyLabel, 0, 0, 1, 1, 0.5, 0.5);
        addComponent(contactEmergencyButton, 1, 0, 1, 1, 0.5, 0.5);
        addComponent(resourcesLabel, 0, 1, 1, 1, 0.5, 0.5);
        addComponent(viewResourcesButton, 1, 1, 1, 1, 0.5, 0.5);

        contactEmergencyButton.addActionListener(e -> {
            // logic
            JOptionPane.showMessageDialog(this, "Dr. Barnett Phone - (555) 555-5555\nDr. Barnett office hours - 9am-5pm\nNearby Clinics: Mayo Clinic, 123 Main St, City, State, 12345\nLocal Emergency Phone - 911, 123, 000");
        });

        viewResourcesButton.addActionListener(e -> {
            // logic
            JOptionPane.showMessageDialog(this, "Shared Care Plan for Marge Simpson, effective 11/16/2024");
        });
    }
}
