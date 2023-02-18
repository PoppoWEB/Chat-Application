package sys.server;

import java.io.IOException;
import java.net.*;

public class ThreadServer {
    private static int IDs = 0;
    public ThreadServer(int port) throws ConnectException {
        try (ServerSocket server = new ServerSocket(port);){
            System.out.println("Wait...");
            Socket socket = null;
    
            while (true) {
                socket = server.accept();
                ConnectionServer cserver = new ConnectionServer(socket, IDs++);
                cserver.start();
            }
        } catch (ConnectException e) {
            throw new ConnectException();
        } catch (IOException e) {
            System.out.println("IOException\n" + e.toString());
        } 
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: Enter a port number as an argument.");
            System.out.println("Example1: Java PortNumber.");
        }
        try {
            new ThreadServer(Integer.parseInt(args[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}