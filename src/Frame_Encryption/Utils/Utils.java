package Frame_Encryption.Utils;

import Frame_Encryption.TCP.ConnectionManagerThread;
import Frame_Encryption.TCP.TCPServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class Utils {
    public static void endCommunication(Socket socket, BufferedReader reader, BufferedWriter writer, boolean erroneousEnd){
        if (erroneousEnd){
            try{
                socket.close();
                reader.close();
                writer.close();
            } catch (Exception e){
                System.out.println("Something went wrong...");
            }
            System.out.println("Closing connection...");
        } else {
            erroneousEnd(socket, reader, writer);
        }
    }
    public static void erroneousEnd(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try{
            socket.close();
            reader.close();
            writer.close();
        } catch (Exception e){
            System.out.println("Something went REALLY wrong...");
        }
        System.out.println("Closing connection REALLY badly...");
    }
    public static void sendTo(String recipientUser, String data){
        synchronized (Utils.class){
            ConnectionManagerThread connectionManagerThread = TCPServer.activeClients.get(recipientUser);

        }
    }
}
