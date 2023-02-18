package gui;

import java.awt.event.*;
import javax.swing.*;

public abstract class AbstractFrame extends JFrame implements ActionListener {
    protected AbstractFrame(String title) {
        super(title);
    }

    protected final void message(String title, String msg) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    protected final void message(String title, String msg, int option) {
        JOptionPane.showMessageDialog(this, msg, title, option);
    }

    protected abstract void screenDesign();
}
