package file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;

public class TextSaves implements Create {
    private final static TextSaves singlton = new TextSaves();

    private TextSaves() {}

    public static TextSaves getInstance() {
        return singlton;
    }

    public boolean createText(String title, String msg) {
        File file = Open(JFileChooser.DIRECTORIES_ONLY);
        if (file != null) {
            file = new File(file.toPath() + "/" + title + ".txt");
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.print(msg);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}