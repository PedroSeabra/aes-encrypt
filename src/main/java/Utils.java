import java.nio.charset.StandardCharsets;
import java.util.Arrays;

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
        byte[] inputBytes = input.getBytes(StandardCharsets.US_ASCII);

        int i = 0, j = 0;
        while(j < 4) {
            while(i < 4) {
                System.out.println(j*4 + i);
                response[i][j] = inputBytes[j*4 + i];
                i++;
            }
            j++;
        }

        return response;
    }

    public static byte sBoxSubstitute(byte input) {
        int i = input >>> 4, j = input & 0xf;
        return (byte) Constants.SBox[i][j];
    }

    public static byte[][][] expandKey(byte[][] key) {
        byte[][][] expandedKey = new byte[11][4][4];

        //A rodada inicial (expandedKey[0]) usa as colunas da própria chave
        for(var i = 0; i < 4; i++) {
            expandedKey[0][i] = new byte[]{key[0][i*4], key[1][i*4], key[2][i*4], key[3][i*4]};
        }

        for(var i = 4; i < 44; i++) {
            var temp = expandedKey[(i-1)/4][(i-1)%4];
            if(i%4 == 0) {
                var changedWord = SubstituteWord(RotateWord(temp));
                var leftMostByte = (byte) (changedWord[0] ^ Constants.RoundConstants[i/4]);
                temp = new byte[]{leftMostByte, changedWord[1], changedWord[2], changedWord[3]};
            }

            expandedKey[i/4][i%4] = new byte[]{
                (byte) (expandedKey[(i-4)/4][(i-4)%4][0] ^ temp[0]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][1] ^ temp[1]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][2] ^ temp[2]),
                (byte) (expandedKey[(i-4)/4][(i-4)%4][3] ^ temp[3])
            };
        }

        return expandedKey;
    }

    private static byte[] RotateWord(byte[] word) {
        return new byte[]{word[1], word[2], word[3], word[0]};
    }

    private static byte[] SubstituteWord(byte[] word) {
        return new byte[]{
                sBoxSubstitute(word[0]),
                sBoxSubstitute(word[1]),
                sBoxSubstitute(word[2]),
                sBoxSubstitute(word[3]),
        };
    }
}
