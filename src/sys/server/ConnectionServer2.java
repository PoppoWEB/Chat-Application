package sys.server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextArea;

import file.ImageSaves;
import function.comp.JUsefulList;
import function.comp.Terminal;

public class ConnectionServer2 extends ConnectionServer {
    private final JTextArea log;
    private final JUsefulList<String> userList;
    private final JUsefulList<String> imageList;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("(HH:mm)");
    private final File file = new File("log");
    protected BufferedInputStream input;
    protected BufferedOutputStream output;

    public ConnectionServer2(Socket socket , int ID , JTextArea log, JUsefulList<String> usefulList, JUsefulList<String> imageList) {
        super(socket, ID);
        this.log = log;
        this.userList = usefulList;
        this.imageList = imageList;
    }

    @Override
    public void init() {
        super.init();
        try {
            input = new BufferedInputStream(getInputStream());
            output = new BufferedOutputStream(getOutputStream());
        } catch (IOException e) {
            System.out.println("Server: send or receive instance is null");
            e.printStackTrace();
        }
    }

    @Override
    protected void command(String line) {
        if (line.startsWith("#JOIN#")) {
            String name = line.substring("#JOIN#".length());
            Terminal.messageAll("#MEMBER_ADD#" + name);
            Terminal.accept(ID, send);
            userList.add( name );
            for (int i = 0; i < userList.getSize(); i++) {
                send.println("#MEMBER_ADD#" + userList.get(i));
            }
            line = "[JOIN] " + name;
        } else if (line.startsWith("#END#")) {
            String name = line.substring("#END#".length());
            Terminal.exitMember(this.ID);
            userList.remove( name );
            Terminal.messageAll("#MEMBER_DEL#" + name);
            line = "[END] " + name;
        } else if (line.startsWith("#IMAGE_ADD#")) {
            String name = line.substring("#IMAGE_ADD#".length());
            boolean res = ImageSaves.receiveIm(file, name, input);
            if (res) {
                imageList.add( name );
            } else {
                System.out.println("OUT RECEIVE");
            }
        } else if (line.startsWith("#IMAGE_DL#")) {
            String name = line.substring("#IMAGE_DL#".length());
            send.println("#IMAGE_DL#" + name);
            boolean res = ImageSaves.sendIm(file.getPath() + "\\" + name, output);
            if (res) {
                System.out.println("Success");
            } else {
                System.out.println("OUT SEND");
            }
            return;
        }
        log.append(line + " -> " + LocalDateTime.now().format(format) + "\n");
        Terminal.messageAll(line);
    }
}
