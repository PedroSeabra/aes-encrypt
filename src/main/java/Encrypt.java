public class Encrypt {
    byte[][] initialState = new byte[4][4];
    byte[][] secretByteArray = new byte[4][4];

    byte[][][] expandedKey;

    public String encrypt(String clearText, String encryptionKey) {
        initialState = Utils.stringToAESByteArray(clearText);
        secretByteArray = Utils.stringToAESByteArray(encryptionKey);
        expandedKey = Utils.expandKey(secretByteArray);

        System.out.println("Plaintext (byte array): " + Utils.byteArraytoString(initialState));
        System.out.println("Cipher key (byte array): " + Utils.byteArraytoString(secretByteArray));

        System.out.println("Round 0:");
        System.out.println("\tState: " + Utils.byteArraytoString(initialState));
        System.out.println("\tRoundKey: " + Utils.byteArraytoStringRowWise(expandedKey[0]));

        addRoundKey(0);

        System.out.println("\tState (after addRoundKey): " + Utils.byteArraytoString(initialState));

        int round = 1;
        while(round < 11) {
            System.out.println("Round " + round + ":");

            subBytes();
            System.out.println("\tState (after subBytes): " + Utils.byteArraytoString(initialState));

            shiftRows();
            System.out.println("\tState (after shiftRows): " + Utils.byteArraytoString(initialState));

            if(round != 10) {
                mixColumns();
                System.out.println("\tState (after mixColumns): " + Utils.byteArraytoString(initialState));
            }

            System.out.println("\tRoundKey: " + Utils.byteArraytoStringRowWise(expandedKey[round]));

            addRoundKey(round);
            System.out.println("\tState (after addRoundKey): " + Utils.byteArraytoString(initialState));
            System.out.println("--------------------\n\n");

            round++;
        }

        return Utils.byteArraytoString(initialState);
    }

    private void addRoundKey(int round) {
        for(var i = 0; i < 4; i++) {
            initialState[0][i] = (byte) (initialState[0][i] ^ expandedKey[round][i][0]);
            initialState[1][i] = (byte) (initialState[1][i] ^ expandedKey[round][i][1]);
            initialState[2][i] = (byte) (initialState[2][i] ^ expandedKey[round][i][2]);
            initialState[3][i] = (byte) (initialState[3][i] ^ expandedKey[round][i][3]);
        }
    }

    private void subBytes() {
        for(var i = 0; i < 4; i++) {
            initialState[0][i] = Utils.sBoxSubstitute(initialState[0][i]);
            initialState[1][i] = Utils.sBoxSubstitute(initialState[1][i]);
            initialState[2][i] = Utils.sBoxSubstitute(initialState[2][i]);
            initialState[3][i] = Utils.sBoxSubstitute(initialState[3][i]);
        }
    }

    private void shiftRows() {
        byte[][] shiftedState = new byte[4][4];
        
        shiftedState[0][0] = initialState[0][0];
        shiftedState[0][1] = initialState[0][1];
        shiftedState[0][2] = initialState[0][2];
        shiftedState[0][3] = initialState[0][3];

        shiftedState[1][0] = initialState[1][1];
        shiftedState[1][1] = initialState[1][2];
        shiftedState[1][2] = initialState[1][3];
        shiftedState[1][3] = initialState[1][0];

        shiftedState[2][0] = initialState[2][2];
        shiftedState[2][1] = initialState[2][3];
        shiftedState[2][2] = initialState[2][0];
        shiftedState[2][3] = initialState[2][1];

        shiftedState[3][0] = initialState[3][3];
        shiftedState[3][1] = initialState[3][0];
        shiftedState[3][2] = initialState[3][1];
        shiftedState[3][3] = initialState[3][2];

        initialState = shiftedState;
    }

    private void mixColumns() {
        byte[][] mixedColumns = new byte[4][4];

        for(var j = 0; j < 4; j++) {
            mixedColumns[0][j] = (byte) (Utils.multiplyByte(initialState[0][j], 0x02) ^
                    Utils.multiplyByte(initialState[1][j], 0x03) ^
                    Utils.multiplyByte(initialState[2][j], 0x01) ^
                    Utils.multiplyByte(initialState[3][j], 0x01));

            mixedColumns[1][j] = (byte) (Utils.multiplyByte(initialState[0][j], 0x01) ^
                    Utils.multiplyByte(initialState[1][j], 0x02) ^
                    Utils.multiplyByte(initialState[2][j], 0x03) ^
                    Utils.multiplyByte(initialState[3][j], 0x01));

            mixedColumns[2][j] = (byte) (Utils.multiplyByte(initialState[0][j], 0x01) ^
                    Utils.multiplyByte(initialState[1][j], 0x01) ^
                    Utils.multiplyByte(initialState[2][j], 0x02) ^
                    Utils.multiplyByte(initialState[3][j], 0x03));

            mixedColumns[3][j] = (byte) (Utils.multiplyByte(initialState[0][j], 0x03) ^
                    Utils.multiplyByte(initialState[1][j], 0x01) ^
                    Utils.multiplyByte(initialState[2][j], 0x01) ^
                    Utils.multiplyByte(initialState[3][j], 0x02));
        }

        initialState = mixedColumns;
    }
}
