package com.demo.admissionportal.util.impl;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * The type Random code holland test.
 */
@Component
public class RandomCodeHollandTest {
    private static final String CHARACTERS = "0123456789";
    private static final int STRING_LENGTH = 13;
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate random string string.
     *
     * @return the string
     */
    public String generateRandomCode() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }
}
