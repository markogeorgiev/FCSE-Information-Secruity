package Frame_Encryption;

import java.io.ByteArrayOutputStream;
import java.security.SecureRandom;
import java.util.List;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Utilities {

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit AES
        return keyGen.generateKey();
    }

    public static byte[] getInitialVector() throws Exception {
        // Initialization vector (IV) for CTR mode - 16 bytes for AES
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv); // Fills IV with random bytes
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        return iv;
    }

    public static List<byte[]> splitInto16ByteChunks(byte[] data) {
        List<byte[]> chunks = new ArrayList<>();
        int chunkSize = 16;

        for (int i = 0; i < data.length; i += chunkSize) {
            int end = Math.min(data.length, i + chunkSize);
            byte[] chunk = new byte[16];
            if (end - i < 16) {
                for (int j = end - i; j < 16; j++) {
                    chunk[j] = 0;
                }
            }
            System.arraycopy(data, i, chunk, 0, end - i);
            chunks.add(chunk);
        }

        return chunks;
    }

    public static byte[] getMic(byte[] headerData, byte[] data, SecretKey key, IvParameterSpec iv) throws Exception {
        // Split data
        List<byte[]> chunks = splitInto16ByteChunks(headerData);
        chunks.addAll(splitInto16ByteChunks(data));

        // Initialize Cipher obj
        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] AESKTransfer = new byte[16];

        for (byte[] chunk : chunks) {
            AESKTransfer = cipher.doFinal(chunk);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(AESKTransfer));
        }

        return AESKTransfer;
    }

    public static byte[] getEncryptedData(byte[] data, SecretKey key, IvParameterSpec iv) throws Exception {
        List<byte[]> chunks = splitInto16ByteChunks(data);
        ByteArrayOutputStream encryptedData = new ByteArrayOutputStream();

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        byte[] AESKTransfer = new byte[16];

        for (byte[] chunk : chunks) {
            byte[] encryptedBlock = cipher.update(chunk);
            encryptedData.write(encryptedBlock);
        }

        byte[] finalBlock = cipher.doFinal();
        encryptedData.write(finalBlock);

        return encryptedData.toByteArray();
    }

    public static byte[] decryptData(byte[] data, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher decryptCipher = Cipher.getInstance("AES/CTR/NoPadding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);
        return decryptCipher.doFinal(data);
    }

    public static String toHexString(byte[] data) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : data) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
}
