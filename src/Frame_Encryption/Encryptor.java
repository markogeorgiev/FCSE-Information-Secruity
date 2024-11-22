package Frame_Encryption;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;

public class Encryptor {
    ClearTextFrame clearTextFrame;
    EncryptedTextFrame encryptedTextFrame;
    SecretKey key;
    byte[] micInitialVector;
    byte[] ctrPreloadInitialVector;
    byte[] mic;

    public Encryptor(ClearTextFrame clearTextFrame) throws Exception {
        this.clearTextFrame = clearTextFrame;
        // Generate AES key & Initial Vectors
        key = Utilities.generateKey();
        micInitialVector = Utilities.getInitialVector();
        ctrPreloadInitialVector = Utilities.getInitialVector();
        mic = Utilities.getMic(
                clearTextFrame.getFrameHeader().getBytes(),
                clearTextFrame.getData().getBytes(),
                key,
                new IvParameterSpec(micInitialVector));
    }

    public EncryptedTextFrame getEncrypted() throws Exception {
        encryptedTextFrame = new EncryptedTextFrame(clearTextFrame.getFrameHeader(),
                clearTextFrame.getPacketNumber(),
                Utilities.getEncryptedData(clearTextFrame.getData().getBytes(),
                        key,
                        new IvParameterSpec(ctrPreloadInitialVector)),
                mic);
        return encryptedTextFrame;
    }

    public ClearTextFrame getDecrypted() throws Exception {
        return new ClearTextFrame(clearTextFrame.frameHeader,
                        new String(Utilities.decryptData(encryptedTextFrame.getEncryptedMessage(),
                                key,
                                new IvParameterSpec(ctrPreloadInitialVector)),
                                StandardCharsets.UTF_8),
                        clearTextFrame.getPacketNumber());

    }

    public byte[] getMic() {
        return mic;
    }

    public SecretKey getKey() {
        return key;
    }

    public byte[] getMicInitialVector() {
        return micInitialVector;
    }

    public byte[] getCtrPreloadInitialVector() {
        return ctrPreloadInitialVector;
    }
}
