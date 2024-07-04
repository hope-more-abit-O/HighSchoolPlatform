package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.UniversityType;
import org.modelmapper.AbstractConverter;

public class UniversityTypeToStringConverter extends AbstractConverter<UniversityType, String> {

    @Override
    protected String convert(UniversityType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case PUBLIC:
                return "Công lập";
            case PRIVATE:
                return "Tư thục";
            default:
                return type.name(); // Default to English name if not found
        }
    }
}