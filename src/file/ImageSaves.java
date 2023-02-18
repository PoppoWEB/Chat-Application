package file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ImageSaves implements Create {
    public static boolean receiveIm(File file, String title, BufferedInputStream input) {
        if (!file.exists()) {
            file.mkdir();
        }

        String path = file.getPath();
        try (FileOutputStream out = new FileOutputStream(path + "\\" + title)) {
            byte buf[] = new byte[1024];
            int len = 0;
            int cnt = 0;
            while (true) {
                if (input.available() <= 0) {
                    Thread.sleep(50);
                    cnt++;
                    if (cnt >= 100) break;
                } else if (((len = input.read(buf)) != -1)) {
                    out.write(buf, 0, len);
                    cnt = 0;
                } else {
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendIm(String path, BufferedOutputStream output) {
        try (FileInputStream in = new FileInputStream(path)) {
            byte buf[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            output.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
