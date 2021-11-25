package helpers;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class JsonHelperTest {
    @Test
    void testStringify() {
        Object o = new Object();
        String res = JsonHelper.stringify(o);

        assertEquals("{}", res);
    }

    @Test
    void testParse() {
        String s = String.format("{\"isBase64Encoded\":false,\"statusCode\":200,\"headers\":{},\"body\": \"{}\"}");

        System.out.println("string = " + JsonHelper.parse(s));
//        assertEquals(s, actual);
    }
}