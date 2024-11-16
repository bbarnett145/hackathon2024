package telehealth.views;

import javax.swing.*;

public class NotePanel extends JPanel {
    public NotePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JTextArea noteArea = new JTextArea(20, 50);
        JButton saveButton = new JButton("Save Note");

        add(new JScrollPane(noteArea));
        add(saveButton);
    }
}
