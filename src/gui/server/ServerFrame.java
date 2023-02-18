package gui.server;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import file.TextSaves;
import gui.AbstractFrame;
import sys.server.newThreadServer;
import function.comp.*;

public class ServerFrame extends AbstractFrame {
    private final JButton saveBtn = new JButton("Save");
    private final JButton closeBtn = new JButton("Close");
    private final JTextArea logArea = new JTextArea(20,20);
    private final JUsefulList<String> userList = new JUsefulList<>();
    private final JUsefulList<String> imageList = new JUsefulList<>();
    private final String folderPath = "log";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");

    public ServerFrame(String title, int port) {
        super(title);
        new newThreadServer(port, logArea, userList, imageList);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Terminal.messageAll("#END#");
                System.exit(0);
            }
        });

        imageList.getList().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JList<?> list = (JList<?>)e.getSource();
                if (e.getClickCount() == 2) {
                    int idx = list.locationToIndex(e.getPoint());
                    if (idx != -1) {
                        String name = (String)list.getModel().getElementAt(idx);
                        new ImageViewFrame("View (" + name +")", folderPath + "\\" + name);
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
        
        mainPane.add(setCenter() , "Center");
        mainPane.add(setBtn() , "South");
        
        setSize(500, 400);
        getContentPane().add(mainPane);
        setVisible(true);
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

    private JPanel setBtn() {
        JPanel panel = new JPanel();
        saveBtn.addActionListener(this);
        closeBtn.addActionListener(this);
        panel.add(saveBtn);
        panel.add(closeBtn);

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveBtn) {
            String title = "Log_" + formatter.format(LocalDateTime.now());
            boolean res = TextSaves.getInstance().createText(title, logArea.getText());

            if (res) {
                message("Success", "Log successfully saved.\nTitle: " + title, JOptionPane.INFORMATION_MESSAGE);
            } else {
                message("Failure", "Failed to save log.", JOptionPane.NO_OPTION);
            }
        } else if (e.getSource() == closeBtn) {
            Terminal.messageAll("#END#");
            System.exit(0);
        } 
    }
}
