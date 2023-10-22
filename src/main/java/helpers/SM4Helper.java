package helpers;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.encoders.Hex;

public class SM4Helper {

    public static String decrypt(String encryptedHex, String keyHex, String ivHex) throws
            DataLengthException, IllegalStateException, InvalidCipherTextException {
        BlockCipher engine = new SM4Engine();
        BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));

        byte[] keyBytes = Hex.decode(keyHex);
        byte[] ivBytes = Hex.decode(ivHex);

        cipher.init(false, new ParametersWithIV(new KeyParameter(keyBytes), ivBytes));

        byte[] encryptedBytes = Hex.decode(encryptedHex);
        byte[] decryptedBytes = new byte[cipher.getOutputSize(encryptedBytes.length)];

        int processed = cipher.processBytes(encryptedBytes, 0, encryptedBytes.length, decryptedBytes, 0);

        cipher.doFinal(decryptedBytes, processed);

        return Hex.toHexString(decryptedBytes).toUpperCase(); // .substring(0, 50);
    }

}
