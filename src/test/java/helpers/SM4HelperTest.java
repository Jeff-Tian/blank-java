package helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.Test;

public class SM4HelperTest {
    @Test
    public void testDecrypt() throws DataLengthException, UnsupportedEncodingException, IllegalStateException, InvalidCipherTextException {
        String encryptedHex = "e0da77dce666fc0925995d0207ad2cc48ec23dafaf4dc9c557b4976a60786a67";
        String keyHex = "0123456789abcdeffedcba9876543210";
        String ivHex = "12324512345678900101010101010101";

        String expectedOutput = "2905171310230000000029000000342B20230905184959000000000000000000";

        String actualOutput = SM4Helper.decrypt(encryptedHex, keyHex, ivHex);
        assertEquals(expectedOutput, actualOutput);
    }
}
