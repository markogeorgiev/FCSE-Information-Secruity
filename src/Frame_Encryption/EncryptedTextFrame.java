package Frame_Encryption;

public class EncryptedTextFrame {
    byte[] encryptedMessage;
    byte[] mic;
    String packetHeader;
    int packetNumber;

    public EncryptedTextFrame(String packetHeader, int packetNumber, byte[] encryptedMessage, byte[] mic) {
        this.packetHeader = packetHeader;
        this.packetNumber = packetNumber;
        this.encryptedMessage = encryptedMessage;
        this.mic = mic;
    }

    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }

    public byte[] getMic() {
        return mic;
    }

    public String getPacketHeader() {
        return packetHeader;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setMic(byte[] mic) {
        this.mic = mic;
    }

    @Override
    public String toString() {
        byte[] slashedMic = new byte[8];
        System.arraycopy(mic, 0, slashedMic, 0, 8);
        return packetHeader
                + "|" + packetNumber
                + "|" + Utilities.toHexString(encryptedMessage)
                + "|" + Utilities.toHexString(slashedMic);
    }


}
