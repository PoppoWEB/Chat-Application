package gui.server;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import gui.AbstractFrame;

public class ImageViewFrame extends AbstractFrame {
    private BufferedImage images;
    private final double sepa = 1.1;

    public ImageViewFrame(String title, String filePath) {
        super(title);
        try {
            images = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            message("Not found.", "The specified image was not found.");
            return;
        }
        screenDesign();
    }

    @Override
    public void screenDesign() {
        setSize(800, 800);
        setResizable(true);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (images == null) return;
        double wid = images.getWidth();
        double hig = images.getHeight();

        while (getWidth() <= wid || getHeight() <= hig) {
            wid /= sepa;
            hig /= sepa;
        }
        g.drawImage(images, (getWidth() - (int)wid) / 2, (getHeight() - (int)hig)/2, (int)wid, (int)hig , this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}