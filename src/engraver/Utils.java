package engraver;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    public static byte checksum(byte[] bytes) {
        int sum = 0;
        for (byte b : bytes)
            sum += (0xff & b);

        if (sum > 0xff)
            sum = ~sum + 1;
        return (byte) (sum & 255);
    }

    public static byte checksum(List<Byte> bytes) {
        int sum = 0;
        for (byte b : bytes)
            sum += (0xff & b);

        if (sum > 0xff)
            sum = ~sum + 1;
        return (byte) (sum & 255);
    }

    public static void main(String[] args) {
        byte[] b = new byte[] {-1, -1, -1, -1};
        System.out.println(checksum(b));
    }

    public static byte[] toPacket(byte instr_type, byte[] src) {
        byte[] ret = new byte[src.length + 4];
        ret[0] = instr_type;
        ret[1] = (byte) (ret.length >> 8);
        ret[2] = (byte) (ret.length);

        System.arraycopy(
            src, 0,
            ret, 3,
            src.length
        );
        ret[ret.length - 1] = checksum(src);

        return ret;
    }
}
