package sys.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ConnectionClient extends Client {
    String user;
    PrintWriter send;
    BufferedReader receive;

    ConnectionClient(String user, int port) throws IOException {
        super("localhost", port);
        this.user = user;
        init();
    }

    ConnectionClient(String user, String ip, int port) throws IOException {
        super(ip, port);
        this.user = user;
        init();
    }

    ConnectionClient(String user, String ip, int port, int TIMEOUT) throws IOException {
        super(ip, port, TIMEOUT);
        this.user = user;
        init();
    }

    void init() {
        try {
            send = new PrintWriter(getOutputStream(), true);
            receive = new BufferedReader(new InputStreamReader(getInputStream()));
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void start() {
        send();
        receive();
    }
    
    @Override
    void receive() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        if (receive.ready()) {
                            System.out.println(receive.readLine());
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

    @Override
    void send() {
        new Thread(new Runnable() {
            public void run() {
                try (Scanner sc = new Scanner(System.in);){
                    send.println("#JOIN#" + user);
                    System.out.println("<Input>");
                    while (true) {
                        String line = sc.nextLine();
                        if (line.startsWith("#END#")) {
                            send.println("#END#" + user);
                            break;
                        }
                        send.println(user + ": " + line);
                    }
                } catch (Exception e) {
                    System.out.println("Forced termination.");
                } finally {
                    close();
                }
            }
        }).start();
    }

    @Override
    void close() {
        try {
            if (send != null) send.close();
            if (receive != null) receive.close();
            super.close();
        } catch (IOException e) {
            System.out.println("Failed to close buffer.");
        }
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: Enter name and port IP address, Port");
            System.out.println("Example: Java Name Port -> Java [NAME] [IP] [PORT]");
        }
        try {
            new ConnectionClient(args[0], args[1] , Integer.parseInt(args[2]));
        } catch (IOException e) {
            System.out.println("Server:" + args[1] + " did not exist.");
        }
    }
}