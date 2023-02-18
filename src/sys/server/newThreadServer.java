package sys.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

import function.comp.JUsefulList;

public class newThreadServer {
    private static int IDs = 0;
    
    public newThreadServer(int port,JTextArea log, JUsefulList<String> userList, JUsefulList<String> imageList) {
        new Thread(new Runnable() {
            public void run() {
                try (ServerSocket server = new ServerSocket(port)) {
                    log.append("Start accepting clients\n");
                    Socket socket = null;
        
                    while (true) {
                        socket = server.accept();
                        ConnectionServer2 cserver2 = new ConnectionServer2(socket, IDs++, log, userList, imageList);
                        cserver2.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();    
    }
}
