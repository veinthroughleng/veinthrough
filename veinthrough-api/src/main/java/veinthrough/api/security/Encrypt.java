package veinthrough.api.security;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

public class Encrypt {

    // if a xor c = b, then b xor c = a
    public String xor(String str) {
        Preconditions.checkNotNull(str);

        byte key = (byte) 88;
        byte[] bytes;
        bytes = str.getBytes(Charsets.UTF_8);

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
        }
        return new String(bytes, Charsets.UTF_8);
    }
}
