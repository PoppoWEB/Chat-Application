package gui.client;

import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;

import gui.AbstractFrame;
import file.ImageSaves;

public class ImageSendFrame extends AbstractFrame {
    private final JTextField nameFld = new JTextField(15);
    private final JButton sendBtn = new JButton("Send");
    private final JButton selectBtn = new JButton("Select");
    private final double SEPA = 1.1;
    private final PrintWriter send;
    private final BufferedOutputStream output;
    private File imgFile;

    public ImageSendFrame(String title ,PrintWriter send, BufferedOutputStream output) {
        super(title);
        this.send = send;
        this.output = output;
        screenDesign();
    }

    @Override
    public void screenDesign() {
        JPanel mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout());

        mainPane.add(setFld(), "North");
        mainPane.add(setBtn(), "South");

        getContentPane().add(mainPane);
        setSize(500, 500);
        setVisible(true);
    }

    private JPanel setFld() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("File Name"));
        panel.add(nameFld);

        return panel;
    }

    private JPanel setBtn() {
        JPanel panel = new JPanel();
        panel.add(sendBtn);
        panel.add(selectBtn);

        sendBtn.addActionListener(this);
        selectBtn.addActionListener(this);
        return panel;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (imgFile == null) return;

        BufferedImage img;
        try {
            img = ImageIO.read(imgFile);
            if (img == null) {
                message("File could not be read.", "Read Error");
                throw new IOException();
            }
            double wid = img.getWidth();
            double hig = img.getHeight();

            while (getWidth() - 120 <= wid || getHeight() - 120 <= hig) {
                wid /= SEPA;
                hig /= SEPA;
            }

            g.drawImage(img, (getWidth() - (int)wid) / 2, (getHeight() - (int)hig)/2, (int)wid, (int)hig , this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean ifImg() {
        if (imgFile != null) {
            String path = imgFile.getPath();
            if (path.endsWith(".png") || path.endsWith(".jpg")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendBtn) {
            if (nameFld.getText().equals("")) {
                message("Enter the name of the file.", "Enter");
            } else if (nameFld.getText().indexOf(".") != -1) {
                message("Do not include dots in file names.", "Name");
            }
            else if (ifImg()) {
                String path = imgFile.getPath();
                int idx = path.lastIndexOf(".");
                send.println("#IMAGE_ADD#" + nameFld.getText().concat(path.substring(idx, path.length())));
                ImageSaves.sendIm(imgFile.getPath(), output);
                dispose();
            } else {
                message("Select an image file.", "Select");
            }
        } else if (e.getSource() == selectBtn) {
            ImageSaves saves = new ImageSaves();
            imgFile = saves.Open(JFileChooser.FILES_ONLY);
            if (ifImg()) {
                repaint();
            }
        }
    }
}
