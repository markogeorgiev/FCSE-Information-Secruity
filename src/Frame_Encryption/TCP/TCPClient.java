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

    public TCPClient(Socket socket){
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (Exception e){
            Utils.endCommunication(socket,reader,writer,true);
        }
    }

    public void listForPackets(){

    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String serverMessage;

        try {
            //Receive request for username
            serverMessage = reader.readLine();
            System.out.println(serverMessage);
            //Send username
            String username = sc.nextLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername(){
        return username;
    }

    public String getInetAddress(){
        return socket.getInetAddress().toString();
    }

    public static void main(String[] args) throws IOException {
        TCPClient client = new TCPClient(new Socket("localhost", 10003));

        client.start();
    }
}
