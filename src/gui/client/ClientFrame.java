package gui.client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import gui.AbstractFrame;
import sys.client.ConnectionClient2;
import function.comp.JUsefulList;

public class ClientFrame extends AbstractFrame {
    private final JButton sendBtn = new JButton("Send");
    private final JButton imageBtn = new JButton("Image");
    private final JTextArea logArea = new JTextArea(1,1);
    private final JTextField sendFld = new JTextField(30);
    private final JUsefulList<String> userList = new JUsefulList<>();
    private final JUsefulList<String> imageList = new JUsefulList<>();
    private final ConnectionClient2 client;
    private final String userName;

    public ClientFrame(String ip, int port, String userName) throws IOException {
        super("Client Port(" + port + ")");
        this.userName = userName;
        this.client = new ConnectionClient2(userName, ip, port, logArea, userList, imageList);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                client.end();
            }
        });

        imageList.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JList<?> list = (JList<?>)e.getSource();
                if (e.getClickCount() == 2) {
                    int idx = list.locationToIndex(e.getPoint());
                    if (idx != -1) {
                        String name = (String)list.getModel().getElementAt(idx);
                        client.send("#IMAGE_DL#", name);
                    }
                }
            }
        });
        
        screenDesign();
    }

    @Override
    public void screenDesign() {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());

        mainPane.add(setTop(), "North");
        mainPane.add(setCenter(), "Center");
        mainPane.add(setUnder() , "South");

        getContentPane().add(mainPane);
        getRootPane().setDefaultButton(sendBtn);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JPanel setTop() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("User Name: " + userName));

        return panel;
    }

    private JSplitPane setCenter() {
        JSplitPane panel = new JSplitPane();

        logArea.setLineWrap(true);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        panel.setDividerLocation(150);
        panel.setRightComponent(scrollPane);
        panel.setLeftComponent(setList());

        return panel;
    }

    private JPanel setList() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JScrollPane userPane = new JScrollPane();
        userPane.getViewport().setView(userList.getList());

        JScrollPane imagePane = new JScrollPane();
        imagePane.getViewport().setView(imageList.getList());

        panel.add(new JLabel("Member" , JLabel.CENTER));
        panel.add(userPane);
        panel.add(new JLabel("Image" , JLabel.CENTER));
        panel.add(imagePane);

        return panel;
    }

    private JPanel setUnder() {
        JPanel panel = new JPanel();
        sendBtn.addActionListener(this);
        imageBtn.addActionListener(this);
        panel.add(sendFld);
        panel.add(sendBtn);
        panel.add(imageBtn);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendBtn) {
            if (!sendFld.getText().equals("")) {
                client.send(sendFld.getText());
                sendFld.setText("");
            }
        } else if (e.getSource() == imageBtn) {
            new ImageSendFrame("Image Frame", client.getSend(),client.getOutput());
        } 
    }
}