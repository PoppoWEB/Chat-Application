package sys.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import javax.swing.JTextArea;

import gui.server.ImageViewFrame;
import file.ImageSaves;
import function.comp.JUsefulList;

public class ConnectionClient2 extends ConnectionClient {
    private final JTextArea log;
    private final JUsefulList<String> userList;
    private final JUsefulList<String> imageList;
    private final File file = new File("lib");
    private BufferedInputStream input;
    private BufferedOutputStream output;

    public ConnectionClient2(String userName, String ip, int port, JTextArea log, JUsefulList<String> userList, JUsefulList<String> imageList) throws IOException {
        super(userName, ip, port);
        this.log = log;
        this.userList = userList;
        this.imageList = imageList;
    }

    @Override
    void init() {
        try {
            input = new BufferedInputStream(getInputStream());
            output = new BufferedOutputStream(getOutputStream());
            super.init();
        } catch (IOException e) {
            e.printStackTrace();
        }    
    }

    @Override
    void start() {
        send.println("#JOIN#" + user);
        receive();
    }

    @Override
    void receive() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        if (receive.ready()) {
                            command(receive.readLine());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("BufferedReader threw an error.");                    
                } finally {
                    close();
                }
            }
        }).start();
    }

    public void send(String line) {
        send.println(user + " : " + line);
    }

    public void send(String cmd, String... line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length; i++) {
            sb.append(line[i]);
            if (i + 1 < line.length) {
                sb.append(",");
            }
        }
        send.println(cmd + sb.toString());
    }

    public void send(File file, String name) {
        send.println("#IMAGE_ADD#" + name);
        ImageSaves.sendIm(file.getPath(), output);
    }

    private void command(String line) {
        if (line.startsWith("#END#")) {
            close();
        } else if (line.startsWith("#MEMBER_ADD#")) {
            String name = line.substring("#MEMBER_ADD#".length());
            userList.add( name );
        } else if (line.startsWith("#MEMBER_DEL#")) {
            String name = line.substring("#MEMBER_DEL#".length());
            userList.remove( name );
        } else if (line.startsWith("#IMAGE_ADD#")) {
            String name = line.substring("#IMAGE_ADD#".length());
            imageList.add( name );
        } else if (line.startsWith("#IMAGE_DL#")) {
            String name = line.substring("#IMAGE_DL#".length());
            boolean res = ImageSaves.receiveIm(file, name, input);
            if (res) {
                new ImageViewFrame("View (" + name +")", file.getPath() + "\\" + name);
            }
        } else {
            log.append(line + "\n");
        }
    }

    public void end() {
        send.println("#END#" + user);
        close();
    }

    public PrintWriter getSend() {
        return send;
    }

    public BufferedOutputStream getOutput() {
        return output;
    }

    @Override
    void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            super.close();
        } catch (IOException e) {
            System.out.println("Failed to close buffer Stream."); 
        }
        System.exit(1);
    }
}
