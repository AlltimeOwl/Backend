package com.owl.payrit.global.encryption;

import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PromissoryPaperEncryptionHelper{

    private static Cipher cipher;
    private static Key key;

    public PromissoryPaperEncryptionHelper(@Value("${encrypt.algo}") String algo, @Value("${encrypt.key}") String keyString) throws Exception {
        key = new SecretKeySpec(keyString.getBytes(), algo);
        cipher = Cipher.getInstance(algo);
    }

    public static String encrypt(String plaintext) {
        if (plaintext == null) return null;
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(plaintext.getBytes());
           encryptedString = Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedString;
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null) return null;
        String decryptedString = null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decorded = Base64.getDecoder().decode(encrypted);
            byte[] decrypted = cipher.doFinal(decorded);
            decryptedString = new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}
