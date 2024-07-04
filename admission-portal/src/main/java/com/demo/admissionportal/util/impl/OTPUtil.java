package com.demo.admissionportal.util.impl;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * The type Otp util.
 */
@Component
public class OTPUtil {
    /**
     * Generate otp string.
     *
     * @return the string
     */
    public String generateOTP() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return Integer.toString(randomNumber);
    }
}
