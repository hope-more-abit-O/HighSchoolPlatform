package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.util.enum_validator.EnumSpecificAddress;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.Normalizer;

public class EnumSpecificAddressValidator implements ConstraintValidator<EnumSpecificAddress, String> {
    @Override
    public boolean isValid(String address, ConstraintValidatorContext constraintValidatorContext) {
        if (address == null) {
            return true;
        }

        address = address.trim();

        if (address.isEmpty()) {
            return false; // Empty addresses are usually invalid
        }

        for (char c : address.toCharArray()) {
            if (!(Character.isLetter(c) ||
                    Character.isDigit(c) || // Allow digits for house numbers, etc.
                    c == ' ' ||
                    isVietnameseCharacter(c) ||
                    isAllowedSpecialCharacter(c))) { // New function for other characters
                return false;
            }
        }

        return true;
    }

    // Efficiently check for Vietnamese diacritics
    private boolean isVietnameseCharacter(char c) {

        String normalizedChar = Normalizer.normalize(String.valueOf(c), Normalizer.Form.NFD);
        return normalizedChar.matches("\\p{InCombiningDiacriticalMarks}+");
    }

    // Function to handle additional allowed characters
    private boolean isAllowedSpecialCharacter(char c) {
        String allowedSpecialCharacters = "-/.,()";
        return allowedSpecialCharacters.indexOf(c) >= 0;
    }
}
