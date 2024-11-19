import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class Utils {
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = Constants.HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = Constants.HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public static byte[][] stringToAESByteArray(String input) {
        byte[][] response = new byte[4][4];
//        byte[] inputBytes = input.getBytes(StandardCharsets.US_ASCII);
        byte[] inputBytes = HexFormat.of().parseHex(input);

        int i, j = 0;
        while(j < 4) {
            i = 0;
            while(i < 4) {
                response[i][j] = inputBytes[j*4 + i];
                i++;
            }
            j++;
        }

        return response;
    }

    public static String byteArraytoString(byte[][] byteAES) {
        byte[] flattenedBytes = new byte[16];

        for(var j = 0; j < 4; j++) {
            for(var i = 0; i < 4; i++) {
                flattenedBytes[(j*4)+i] = byteAES[i][j];
            }
        }

        return bytesToHex(flattenedBytes);
    }

    public static String byteArraytoStringRowWise(byte[][] byteAES) {
        byte[] flattenedBytes = new byte[16];

        for(var j = 0; j < 4; j++) {
            for(var i = 0; i < 4; i++) {
                flattenedBytes[(j*4)+i] = byteAES[j][i];
            }
        }

        return bytesToHex(flattenedBytes);
    }

    public static byte sBoxSubstitute(byte input) {
        int i = (input & 0xFF) >>> 4, j = (input & 0xFF)  & 0xf;
        return (byte) Constants.SBox[i][j];
    }

    public static byte[][][] expandKey(byte[][] key) {
        byte[][][] expandedKey = new byte[11][4][4];

        //A rodada inicial (expandedKey[0]) usa as colunas da própria chave
        for(var i = 0; i < 4; i++) {
            expandedKey[0][i] = new byte[]{key[0][i], key[1][i], key[2][i], key[3][i]};
        }

        for(var i = 4; i < 44; i++) {
            var temp = expandedKey[(i-1)/4][(i-1)%4];
            if(i%4 == 0) {
                var changedWord = SubstituteWord(RotateWord(temp));
                var leftMostByte = (byte) (changedWord[0] ^ Constants.RoundConstants[(i/4)-1]);
                temp = new byte[]{leftMostByte, changedWord[1], changedWord[2], changedWord[3]};
            }

             var test = new byte[]{
                (byte) (expandedKey[(i-4)/4][(i-4)%4][0] ^ temp[0]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][1] ^ temp[1]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][2] ^ temp[2]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][3] ^ temp[3])
            };
            expandedKey[i/4][i%4] = test;
        }

        return expandedKey;
    }

    private static byte[] RotateWord(byte[] word) {
        return new byte[]{word[1], word[2], word[3], word[0]};
    }

    private static byte[] SubstituteWord(byte[] word) {
        return new byte[]{
                (byte) (sBoxSubstitute(word[0]) & 0xFF),
                (byte) (sBoxSubstitute(word[1]) & 0xFF),
                (byte) (sBoxSubstitute(word[2]) & 0xFF),
                (byte) (sBoxSubstitute(word[3]) & 0xFF),
        };
    }

    public static byte multiplyByte(byte input, int times) {
        if(times == 1)
            return input;

        byte result = input;
        int leftmostBit;

        int multiplicationsByTwo = (times/2);
        var i = 0;

        while(i < multiplicationsByTwo) {
            //desloca o bit mais à esquerda para a última posição (mais à direita) e compara com 1
            leftmostBit = (result >> 7) & 1;

            result = (byte) (result << 1);
            if(leftmostBit == 1)
                result = (byte) (result ^ 0x1b); //0x1b == 0001 1011 = 27

            i++;
        }
        if(times%2 == 1) //soma
            result = (byte) (result ^ input);

        return result;
    }
}
