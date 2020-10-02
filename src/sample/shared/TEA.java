package sample.shared;


public class TEA {
    private final static int Delta = 0x9E3779B9;
    private final static int Rounds = 32;
    private final static int ShiftedDelta = 0xC6EF3720;

    private int[] S = new int[4];

    /**
     * Initialize the cipher for encryption or decryption.
     * @param key a 16 byte (128-bit) key
     */
    public TEA(byte[] key) {
        if (key == null)
            throw new RuntimeException("Invalid key: Key was null");
        if (key.length < 16)
            throw new RuntimeException("Invalid key: Length was less than 16 bytes");
        for (int off=0, i=0; i<4; i++) {
            S[i] = ((key[off++] & 0xff)) |
                    ((key[off++] & 0xff) <<  8) |
                    ((key[off++] & 0xff) << 16) |
                    ((key[off++] & 0xff) << 24);
        }
    }

    /**
     * Encrypt an array of bytes.
     * @param plainText the plaintext to encrypt
     * @return the encrypted text
     */
    public byte[] encrypt(byte[] plainText) {
        int paddedSize = ((plainText.length/8) + (((plainText.length%8)==0)?0:1)) * 2;
        int[] buffer = new int[paddedSize + 1];
        buffer[0] = plainText.length;
        pack(plainText, buffer, 1);
        encryptionAlgo(buffer);
        return unpack(buffer, 0, buffer.length * 4);
    }

    /**
     * Decrypt an array of bytes.
     * @param cipherText the cipher text to decrypt
     * @return the decrypted text
     */
    public byte[] decrypt(byte[] cipherText) {
        assert cipherText.length % 4 == 0;
        assert (cipherText.length / 4) % 2 == 1;
        int[] buffer = new int[cipherText.length / 4];
        pack(cipherText, buffer, 0);
        decryptionAlgo(buffer);
        return unpack(buffer, 1, buffer[0]);
    }

    void encryptionAlgo(int[] buf) {
        assert buf.length % 2 == 1;
        int i, v0, v1, sum, n;
        i = 1;
        while (i<buf.length) {
            n = Rounds;
            v0 = buf[i];
            v1 = buf[i+1];
            sum = 0;
            while (n-->0) {
                sum += Delta;
                v0  += ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
                v1  += ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
            }
            buf[i] = v0;
            buf[i+1] = v1;
            i+=2;
        }
    }

    void decryptionAlgo(int[] buf) {
        assert buf.length % 2 == 1;
        int i, v0, v1, sum, n;
        i = 1;
        while (i<buf.length) {
            n = Rounds;
            v0 = buf[i];
            v1 = buf[i+1];
            sum = ShiftedDelta;
            while (n--> 0) {
                v1  -= ((v0 << 4 ) + S[2] ^ v0) + (sum ^ (v0 >>> 5)) + S[3];
                v0  -= ((v1 << 4 ) + S[0] ^ v1) + (sum ^ (v1 >>> 5)) + S[1];
                sum -= Delta;
            }
            buf[i] = v0;
            buf[i+1] = v1;
            i+=2;
        }
    }

    /**
     * convert byte array into int array
     * @param src array to be converted
     * @param dest converted int array
     * @param destOffset the starting index of dest
     */
    void pack(byte[] src, int[] dest, int destOffset) {
        assert destOffset + (src.length / 4) <= dest.length;
        int i = 0, shift = 24;
        int j = destOffset;
        dest[j] = 0;
        while (i<src.length) {
            dest[j] |= ((src[i] & 0xff) << shift);
            if (shift==0) {
                shift = 24;
                j++;
                if (j<dest.length) dest[j] = 0;
            }
            else {
                shift -= 8;
            }
            i++;
        }
    }

    /**
     *
     * convert int array into byte array
     * @param src array to be converted
     * @param srcOffset the starting index of the src array
     * @param destLength the size of the plaintext
     * @return array of bytes
     */
    byte[] unpack(int[] src, int srcOffset, int destLength) {
        assert destLength <= (src.length - srcOffset) * 4;
        byte[] dest = new byte[destLength];
        int i = srcOffset;
        int count = 0;
        for (int j = 0; j < destLength; j++) {
            dest[j] = (byte) ((src[i] >> (24 - (8*count))) & 0xff);
            count++;
            if (count == 4) {
                count = 0;
                i++;
            }
        }
        return dest;
    }
}
