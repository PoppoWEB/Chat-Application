package gui.client;

import java.awt.event.*;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;

import gui.AbstractFrame;
import function.error.FieldNullException;
import function.error.NonStandardNameException;

public class LoginFrame extends AbstractFrame {
    private final JButton loginBtn = new JButton("Login");
    private final String[] fieldNames = {"Name:", "Ip:", "Port:"};
    private final JTextField[] inputFields = new JTextField[fieldNames.length];
    {
        for (int i = 0; i < inputFields.length; i++) {
            inputFields[i] = new JTextField(15);
            inputFields[i].setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 15));
        }
    }

    public LoginFrame(String title) {
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

        setSize(300, 200);
        getRootPane().setDefaultButton(loginBtn);
        getContentPane().add(mainPane);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel titleSet() {
        JPanel panel = new JPanel();
        JLabel login_Lbl = new JLabel("Login");
        login_Lbl.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 20));
        panel.add(login_Lbl);

        return panel;
    }

    private JPanel inputSet() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));
        JLabel fieldLbl[] = new JLabel[fieldNames.length];

        for (int i = 0; i < fieldLbl.length; i++) {
            fieldLbl[i] = new JLabel(fieldNames[i]);
            fieldLbl[i].setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
            panel.add(fieldLbl[i]);
            panel.add(inputFields[i]);
        }

        return panel;
    }

    private JPanel buttonSet() {
        JPanel panel = new JPanel();
        panel.add(loginBtn);
        loginBtn.addActionListener(this);

        return panel;
    }

    private void loginCheck() throws Exception {
        for (int i = 0; i < inputFields.length; i++) {
            if (inputFields[i].getText().equals("")) {
                throw new FieldNullException(String.valueOf(i));
            }
        }
        String name = inputFields[0].getText();
        String ip   = inputFields[1].getText();

        if (name.startsWith("#")) throw new NonStandardNameException(name);
        else if (name.length() > 8) throw new NonStandardNameException("\nThe name is long(<8): " + name);
        else if (!ip.contains(".") && !ip.equals("localhost")) throw new NonStandardNameException("\nIP address: " +ip);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            try {
                loginCheck();
                String name = inputFields[0].getText();
                String ip   = inputFields[1].getText();
                int portNum = Integer.parseInt(inputFields[2].getText());
                new ClientFrame(ip , portNum, name);
                dispose();
            } catch (FieldNullException ex) {
                message("Field", "Please fill in the text field.");
            } catch (NonStandardNameException ex) {
                message("Input", "The following input is incorrect." + ex.getMessage());
            } catch (NumberFormatException ex) {
                message("Port", "Please enter an integer.(" + inputFields[1].getText() + ")");
            } catch (IOException ex) {
                message("Server", "Server:" + inputFields[2].getText() + " did not exist.");
            } catch (Exception ex) {
                message("Error", "An unexpected error occurred.", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new LoginFrame("Login");
    }
}