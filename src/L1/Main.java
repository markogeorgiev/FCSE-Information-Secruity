package L1;

import javax.crypto.spec.IvParameterSpec;
import java.util.Arrays;


public class Main {


    public static void main(String[] args) throws Exception {
        String frameHeader = "FRAME_HEADER";
        String message = "My secrete message. Please don't steal it!";
        int packetNumber = 4;

        ClearTextFrame clearTextFrame = new ClearTextFrame(frameHeader, message, packetNumber);
        System.out.println("Start Frame Clear Text: " +clearTextFrame);

        // Generate MIC, Initial Vectors & Key
        Encryptor encryptor = new Encryptor(clearTextFrame);

        // Encrypt Message
        EncryptedTextFrame encryptedTextFrame = encryptor.getEncrypted();
        System.out.println("Start Frame Encrypted Text (Hex): " + encryptedTextFrame);

        ClearTextFrame decryptedFrame = encryptor.getDecrypted();
        // Decrypt Message
        System.out.println("Received Frame Clear Text: " + decryptedFrame);
        if (Arrays.equals(encryptor.getMic(),
                Utilities.getMic(decryptedFrame.getFrameHeader().getBytes(),
                        decryptedFrame.getData().getBytes(),
                        encryptor.getKey(),
                        new IvParameterSpec(encryptor.getMicInitialVector())))){
            System.out.println("The MICs Match CONGRATULATIONS!!!");
        } else {
            System.out.println("Pain");
        }
    }
}