package com.ferenckis.tutorial.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Component
public class CredentialHandler {

    private static final String PROVIDER = "PBEWithMD5AndDES";
    private static final String ENCODING = "UTF-8";
    private static final int ITERATION_COUNT = 20;

    private static final byte[] SALT = {(byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12};

    @Value("${credentials.key.word}")
    private String keyWord;

    public String encrypt(String property) {
        try {
            return base64Encode(initCipher(ENCRYPT_MODE).doFinal(property.getBytes(ENCODING)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decrypt(String property) {
        try {
            return new String(initCipher(DECRYPT_MODE).doFinal(base64Decode(property)), ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String base64Encode(byte[] bytes) {
        return getEncoder().encodeToString(bytes);
    }

    private byte[] base64Decode(String property) throws IOException {
        return getDecoder().decode(property);
    }

    private Cipher initCipher(int mode) throws Exception {
        SecretKey key = SecretKeyFactory.getInstance(PROVIDER).generateSecret(new PBEKeySpec(keyWord.toCharArray()));
        Cipher cipher = Cipher.getInstance(PROVIDER);
        cipher.init(mode, key, new PBEParameterSpec(SALT, ITERATION_COUNT));
        return cipher;
    }

}

