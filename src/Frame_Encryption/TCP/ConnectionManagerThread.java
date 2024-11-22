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
        } catch (Exception e) {
            Utils.endCommunication(socket, reader, writer, false);
        }
    }

    @Override
    public void run() {
        try {
            //Request Username:
            writer.write("Connection successful. Provide a username:");
            writer.flush();

            //Get response
            username = reader.readLine();
            boolean firstTime = true;
            //Allow message sending:
            while (socket.isConnected()) {
                if (firstTime) {
                    if (username != null) {
                        writer.write(String.format("Welcome %s! You can now start sending messages.\n", username));
                        firstTime = false;
                    }
                } else {
                    writer.write(String.format("Hi again, %s. You can send messages again.\n", username));
                }
                writer.flush();

                //Receive data
                String message = reader.readLine();
                StringBuilder currentMessage = new StringBuilder();
                while (!message.equals("END")) {
                    currentMessage.append(message);
                    message = reader.readLine();
                }

                //Confirm data receive
                writer.write("Message received: " + currentMessage.substring(0, 10) + "...\n");
                writer.write("Who do you want to send the message to? Select a recipient from the list\n");
                //Send username list
                StringBuilder sb = new StringBuilder();
                synchronized (this) {
                    TCPServer.activeClients.keySet()
                            .forEach(username -> sb.append(username).append("\n"));
                }
                writer.write(sb.toString());
                writer.flush();

                //Receive user selection
                String selectedUser = reader.readLine();

                //Forward to that user
                synchronized (this) {
                    Socket targetUser = TCPServer.activeClients.get(selectedUser).getSocket();
                    if (targetUser != null) {
                        Writer simpleWriter = new OutputStreamWriter(targetUser.getOutputStream());
                        simpleWriter.write(String.format("Message from %s: %s", username, currentMessage));
                        simpleWriter.flush();
                        simpleWriter.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void listenForMessages(){
        while (true){
            reader.lines().forEach(System.out::println);
        }
    }

    public String getInetAddress(){
        return socket.getInetAddress().toString();
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }
}
