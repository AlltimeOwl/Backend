package com.owl.payrit.global.encryption;

public interface EncryptionHelper {
    String encrypt(String plaintext) throws Exception;
    String decrypt(String encrypted) throws Exception;
}
