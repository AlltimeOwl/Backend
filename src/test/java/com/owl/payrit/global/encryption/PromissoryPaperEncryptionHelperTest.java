package com.owl.payrit.global.encryption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class PromissoryPaperEncryptionHelperTest {

    @Test
    void encrypt() throws Exception{
        String plainText = "Hello";
        String encryption = PromissoryPaperEncryptionHelper.encrypt(plainText);

        assertNotEquals(plainText, encryption);
    }

    @Test
    void decrypt() throws Exception{
        String plainText = "Hello";
        String encryption = PromissoryPaperEncryptionHelper.encrypt(plainText);
        String decryption = PromissoryPaperEncryptionHelper.decrypt(encryption);

        assertEquals(plainText, decryption);
    }
}