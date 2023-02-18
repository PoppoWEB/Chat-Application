package sys.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class Client {
    private final int TIMEOUT = 600000;
    private Socket socket;
    
    protected Client(String ip, int port) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), TIMEOUT);
        connectCheck();
    }

    protected Client(String ip, int port, int TIMEOUT) throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(ip, port), TIMEOUT);
        connectCheck();
    }

    private void connectCheck() {

        InetAddress inet = socket.getInetAddress();
        if (inet == null) {
            System.out.println("Connection failed.");
            close();
            System.exit(1);
        } else {
            System.out.println("Connect to " + inet);
        }
    }

    void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();

    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    abstract void send();
    abstract void receive();
}