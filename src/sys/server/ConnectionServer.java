package sys.server;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import function.comp.Terminal;

public class ConnectionServer extends Server {
    protected final int ID;
    protected PrintWriter send;
    protected BufferedReader receive;

    public ConnectionServer(Socket socket, int ID) {
        super(socket);
        this.ID = ID;
        init();
    }

    @Override
    public void init() {
        try {
            send = new PrintWriter(getOutputStream(), true);
            receive = new BufferedReader(new InputStreamReader(getInputStream()));
        } catch (IOException e) {
            System.out.println("Server: send or receive instance is null");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while (true) {
                if ((line = receive.readLine()) != null) {
                    command(line);
                } else break;
            }
        } catch (SocketException e) {
            System.out.println("Socket(ID:" + ID + ") CLOSE");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    protected void command(String line) {
        String tmp;
        if (line.startsWith("#JOIN#")) {
            tmp = line.substring("#JOIN#".length());
            Terminal.accept(ID, send);
            line = "[JOIN] " + tmp;
        } else if (line.startsWith("#END#")) {
            tmp = line.substring("#END#".length());
            Terminal.exitMember(this.ID);
            line = "[END] " + tmp;
        }
        System.out.println(line);
        Terminal.messageAll(line);
    }

    public void close(String id) {
        if (send != null) send.close();
        if (receive != null) Terminal.exitMember(Integer.parseInt(id));
        this.close();
    }
}