package gui.server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import gui.AbstractFrame;

public class LaunchFrame extends AbstractFrame {
    private final JButton launchBtn = new JButton("Start");
    private final JTextField portFld = new JTextField(15);

    public LaunchFrame(String title) {
        super(title);
        screenDesign();
    }

    @Override
    public void screenDesign() {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());

        mainPane.add(titleSet(), "North");
        mainPane.add(inputSet(), "Center");
        mainPane.add(buttonSet(), "South");

        setSize(300, 140);
        getRootPane().setDefaultButton(launchBtn);
        getContentPane().add(mainPane);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel titleSet() {
        JPanel panel = new JPanel();
        JLabel launch_Lbl = new JLabel("Server Launch");
        launch_Lbl.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
        panel.add(launch_Lbl);

        return panel;
    }

    private JPanel inputSet() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2));
        JLabel portLbl = new JLabel("Port" , JLabel.CENTER);
        portLbl.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
        portFld.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));

        panel.add(portLbl);
        panel.add(portFld);

        return panel;
    }

    private JPanel buttonSet() {
        JPanel panel = new JPanel();
        panel.add(launchBtn);
        launchBtn.addActionListener(this);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == launchBtn) {
            try {
                int portNum = Integer.parseInt(portFld.getText());
                new ServerFrame("Server Port( " + portNum + ")", portNum);
                dispose();
            } catch (NumberFormatException ex) {
                message("Port", "Please enter an integer.(" + ex.getMessage() + ")");
            }
        }
    }

    public static void main(String[] args) {
        new LaunchFrame("Launch");
    }
}
