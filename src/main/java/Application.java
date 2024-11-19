/*
 * @author: Pedro Seabra
 * Project: Multicast Totalmente Ordenado
 */

public class Application {

    public static void main(String[] args) {
        String secret = "000102030405060708090a0b0c0d0e0f";
        String plaintext = "00112233445566778899aabbccddeeff";

        Encrypt crypto = new Encrypt();
        System.out.println("Texto de entrada: " + plaintext);
        System.out.println("Texto cifrado: " + crypto.encrypt(plaintext, secret));
    }
}