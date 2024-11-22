package Frame_Encryption;

public class ClearTextFrame {
    String frameHeader;
    String data;
    int packetNumber;

    public ClearTextFrame(String frameHeader, String data, int packetNumber) {
        this.frameHeader = frameHeader;
        this.data = data;
        this.packetNumber = packetNumber;
    }

    public String getFrameHeader() {
        return frameHeader;
    }

    public String getData() {
        return data;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    @Override
    public String toString(){
        return frameHeader + "|" + packetNumber + "|" + data.replaceAll("\u0000+$", "");
    }
}

