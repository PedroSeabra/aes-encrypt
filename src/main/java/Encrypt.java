import java.nio.charset.StandardCharsets;

public class Encrypt {
    byte[][] initialState = new byte[4][4];
    byte[][] secretByteArray = new byte[4][4];

    byte[][][] expandedKey;

    public String encrypt(String clearText, String encryptionKey) {
        initialState = Utils.stringToAESByteArray(clearText);
        secretByteArray = Utils.stringToAESByteArray(encryptionKey);
        expandedKey = Utils.expandKey(secretByteArray);

        addRoundKey(0);
        int round = 1;
        while(round < 11) {
            subBytes();
            shiftRows();

            if(round != 10)
                mixColumns();

            addRoundKey(round);

            //System.out.println("Current State: " + Utils.byteArraytoString(initialState));
            round++;
        }

        System.out.println("||||||||||||||||");
        return Utils.byteArraytoString(initialState);
    }

    private void addRoundKey(int round) {
        System.out.println("Round "+round+":");
        System.out.println("state: " + Utils.bytesToHex(initialState[0])
            + Utils.bytesToHex(initialState[1])
            + Utils.bytesToHex(initialState[2])
            + Utils.bytesToHex(initialState[3]));
        System.out.println("roundKey: " + Utils.bytesToHex(expandedKey[round][0])
                + Utils.bytesToHex(expandedKey[round][1])
                + Utils.bytesToHex(expandedKey[round][2])
                + Utils.bytesToHex(expandedKey[round][3]));
        for(var i = 0; i < 4; i++) {
            initialState[0][i] = (byte) (initialState[0][i] ^ expandedKey[round][0][i]);
            initialState[1][i] = (byte) (initialState[1][i] ^ expandedKey[round][1][i]);
            initialState[2][i] = (byte) (initialState[2][i] ^ expandedKey[round][2][i]);
            initialState[3][i] = (byte) (initialState[3][i] ^ expandedKey[round][3][i]);
        }
        System.out.println("state: " + Utils.bytesToHex(initialState[0])
                + Utils.bytesToHex(initialState[1])
                + Utils.bytesToHex(initialState[2])
                + Utils.bytesToHex(initialState[3]));
    }

    private void subBytes() {
        for(var i = 0; i < 4; i++) {
            initialState[0][i] = Utils.sBoxSubstitute(initialState[0][i]);
            initialState[1][i] = Utils.sBoxSubstitute(initialState[0][i]);
            initialState[2][i] = Utils.sBoxSubstitute(initialState[0][i]);
            initialState[3][i] = Utils.sBoxSubstitute(initialState[0][i]);
        }
    }

    private void shiftRows() {
        initialState[1][0] = Utils.sBoxSubstitute(initialState[1][1]);
        initialState[1][1] = Utils.sBoxSubstitute(initialState[1][2]);
        initialState[1][2] = Utils.sBoxSubstitute(initialState[1][3]);
        initialState[1][3] = Utils.sBoxSubstitute(initialState[1][0]);

        initialState[2][0] = Utils.sBoxSubstitute(initialState[2][2]);
        initialState[2][1] = Utils.sBoxSubstitute(initialState[2][3]);
        initialState[2][2] = Utils.sBoxSubstitute(initialState[2][0]);
        initialState[2][3] = Utils.sBoxSubstitute(initialState[2][1]);

        initialState[3][0] = Utils.sBoxSubstitute(initialState[3][3]);
        initialState[3][1] = Utils.sBoxSubstitute(initialState[3][0]);
        initialState[3][2] = Utils.sBoxSubstitute(initialState[3][1]);
        initialState[3][3] = Utils.sBoxSubstitute(initialState[3][2]);
    }

    private void mixColumns() {
        for(var j = 0; j < 4; j++) {
            initialState[0][j] = (byte) (Utils.multiplyByte(initialState[0][j], Constants.mixColumnsTable[0][0]) ^
                    Utils.multiplyByte(initialState[1][j], Constants.mixColumnsTable[0][1]) ^
                    Utils.multiplyByte(initialState[2][j], Constants.mixColumnsTable[0][2]) ^
                    Utils.multiplyByte(initialState[3][j], Constants.mixColumnsTable[0][3]));
            initialState[1][j] = (byte) (Utils.multiplyByte(initialState[0][j], Constants.mixColumnsTable[1][0]) ^
                    Utils.multiplyByte(initialState[1][j], Constants.mixColumnsTable[1][1]) ^
                    Utils.multiplyByte(initialState[2][j], Constants.mixColumnsTable[1][2]) ^
                    Utils.multiplyByte(initialState[3][j], Constants.mixColumnsTable[1][3]));
            initialState[2][j] = (byte) (Utils.multiplyByte(initialState[0][j], Constants.mixColumnsTable[2][0]) ^
                    Utils.multiplyByte(initialState[1][j], Constants.mixColumnsTable[2][1]) ^
                    Utils.multiplyByte(initialState[2][j], Constants.mixColumnsTable[2][2]) ^
                    Utils.multiplyByte(initialState[3][j], Constants.mixColumnsTable[2][3]));
            initialState[3][j] = (byte) (Utils.multiplyByte(initialState[0][j], Constants.mixColumnsTable[3][0]) ^
                    Utils.multiplyByte(initialState[1][j], Constants.mixColumnsTable[3][1]) ^
                    Utils.multiplyByte(initialState[2][j], Constants.mixColumnsTable[3][2]) ^
                    Utils.multiplyByte(initialState[3][j], Constants.mixColumnsTable[3][3]));
        }
    }
}
