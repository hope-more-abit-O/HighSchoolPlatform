package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.Gender;
import org.modelmapper.AbstractConverter;

/**
 * Converts `Gender` enum values to their corresponding Vietnamese string representations.
 *
 * <p>This converter class extends the `AbstractConverter` class from Spring Framework's conversion system.
 * It provides a way to convert `Gender` enums to Vietnamese string representations during data binding or other conversion scenarios.
 * The conversion logic utilizes a switch statement to map each `Gender` enum constant (MALE, FEMALE) to its corresponding Vietnamese translation.
 * If the provided `gender` value doesn't match any defined enum constant, it returns "KHÁC" (meaning "Other").
 *
 * @see AbstractConverter
 * @see Gender
 */
public class GenderStatusConverter extends AbstractConverter<Gender, String> {

    /**
     * Converts a `Gender` enum value to its corresponding Vietnamese string representation.
     *
     * <p>This method utilizes a switch statement to map each `Gender` enum constant
     * (MALE, FEMALE) to its corresponding Vietnamese translation ("NAM" for MALE, "NỮ" for FEMALE).
     * If the provided `gender` value doesn't match any defined enum constant, it returns "KHÁC" (meaning "Other").
     *
     * @param gender The `Gender` enum value to be converted.
     * @return The Vietnamese string representation of the `gender` value ("NAM", "NỮ", or "KHÁC").
     */
    @Override
    protected String convert(Gender gender) {
        switch (gender) {
            case MALE:
                return "NAM";
            case FEMALE:
                return "NỮ";
            default:
                return "KHÁC";
        }
    }
}