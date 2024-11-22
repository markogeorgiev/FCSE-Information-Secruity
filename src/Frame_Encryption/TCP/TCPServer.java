package Frame_Encryption.TCP;

import Frame_Encryption.Utils.Utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPServer extends Thread {
    public static int FrameCounter = 0;
    ServerSocket serverSocket;
    public static Map<String, ConnectionManagerThread> activeClients = new HashMap<String, ConnectionManagerThread>();

    public TCPServer(int port) throws IOException {
        System.out.println("Starting server...");
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server started!");
        runServer();
    }

    public void runServer() {
        System.out.println("Waiting for connections...");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ConnectionManagerThread newClient = new ConnectionManagerThread(socket);
                synchronized (this) {
                    activeClients.put(newClient.getUsername(), newClient);
                }
                System.out.println("New client connected! Username: "
                        + newClient.getUsername()
                        + ":" + newClient.getInetAddress()
                );
                newClient.start();
                newClient.listenForMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TCPServer server = new TCPServer(10003);
    }
}


