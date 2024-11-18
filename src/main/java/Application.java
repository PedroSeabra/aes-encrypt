/*
 * @author: Pedro Seabra
 * Project: Multicast Totalmente Ordenado
 */

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        String s = "ABCDEFGHIJKLMNOP";
        String s2 = "z";

        byte[] b = s.getBytes(StandardCharsets.US_ASCII);
        byte[] b2 = s2.getBytes(StandardCharsets.US_ASCII);

        byte c = (byte) (b[0] ^ b2[0]);

        System.out.println(b.length);
        System.out.println(Arrays.toString(b));
        System.out.println(b2.length);
        System.out.println(Arrays.toString(b2));
        System.out.println(c);
        System.out.println((char) c);
        System.out.println(Utils.bytesToHex(b) + " | " + Utils.bytesToHex(b2) + " | " + Utils.bytesToHex(new byte[]{c}));
    }
}