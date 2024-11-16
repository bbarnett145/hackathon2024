package telehealth.views;

import telehealth.GUIShell;

import javax.swing.*;
import java.awt.*;

public abstract class BasePanel extends JPanel {
    protected GUIShell guiShell;

    public BasePanel(GUIShell guiShell) {
        this.guiShell = guiShell;
        setLayout(new GridBagLayout());
    }

    protected void addComponent(JComponent component, int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(component, gbc);
    }
}
