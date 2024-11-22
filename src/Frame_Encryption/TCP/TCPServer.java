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

    public TCPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void runServer() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ConnectionManagerThread newClient = new ConnectionManagerThread(socket);
                synchronized (this) {
                    activeClients.put(newClient.getUsername(), newClient);
                }
                newClient.start();
                newClient.listenForMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10003);
        TCPServer server = new TCPServer(serverSocket);
        server.runServer();
    }
}


