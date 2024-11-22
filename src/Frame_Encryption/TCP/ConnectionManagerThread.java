package Frame_Encryption.TCP;

import Frame_Encryption.Utils.Utils;

import java.io.*;
import java.net.Socket;

public class ConnectionManagerThread extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public ConnectionManagerThread(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            username = reader.readLine();
        } catch (Exception e) {
            Utils.endCommunication(socket, reader, writer, false);
        }
    }

    @Override
    public void run() {
        //Listen 2 messages
        StringBuilder sb = new StringBuilder();
        String data = "NO_DATA";
        String recipientUsername = "NO_USERNAME";
        try {
            data = reader.readLine();
            recipientUsername = reader.readLine();
            Utils.sendTo(recipientUsername, data);
        } catch (IOException e) {
            Utils.endCommunication(socket, reader, writer, true);
        }
    }

    public void listenForMessages(){
        while (true){
            reader.lines().forEach(System.out::println);
        }
    }

    public String getUsername() {
        return username;
    }
}
