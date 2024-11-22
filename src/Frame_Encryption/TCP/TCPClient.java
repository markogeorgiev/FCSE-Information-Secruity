package Frame_Encryption.TCP;
import Frame_Encryption.Utils.Utils;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    String username;

    public TCPClient(Socket socket, String username){
        this.username = username;
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(username);
            writer.flush();
        } catch (Exception e){
            Utils.endCommunication(socket,reader,writer,true);
        }
    }

    public void listForPackets(){
        //TODO implement
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("Start inputting your message (To end reading input EOF on a separate line!):");
        while (true){
            String currLine = sc.nextLine();
            if (currLine.equals("EOF")){
                break;
            } else {
                stringBuilder.append(currLine);
            }
        }

        try {
            writer.write(stringBuilder.toString());
            writer.flush();

            System.out.println("Who do you want to send the message to?");
            System.out.println("Please enter a username to send: ");
            String recipientUserName = sc.nextLine();
            writer.write(recipientUserName);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(stringBuilder);
        Utils.endCommunication(socket,reader,writer,false);
        sc.close();
    }

    public String getUsername(){
        return username;
    }

    public String getInetAddress(){
        return socket.getInetAddress().toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter your username: ");
        String username = sc.nextLine();

        TCPClient client = new TCPClient(new Socket("localhost", 10003), username);

        client.start();
    }
}
