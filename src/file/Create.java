package file;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public interface Create {
    public default File Open(int FILE_OPTION) {    
        JFileChooser fileChooser = new JFileChooser("c:");
        fileChooser.setFileSelectionMode(FILE_OPTION);

        int select = fileChooser.showOpenDialog(null);

        if (select == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public default void messagelog(String msg, String title, int TYPE) {
        JOptionPane.showMessageDialog(null, msg, title, TYPE);
    }
}
