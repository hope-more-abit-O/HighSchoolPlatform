package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.EducationLevel;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class EducationLevelConverter extends AbstractConverter<EducationLevel, String> {
    @Override
    protected String convert(EducationLevel level) {
        if (level == null) {
            return null;
        }

        switch (level) {
            case SECONDARY:
                return "Trung học";
            case HIGH:
                return "Đại học";
            case OTHER:
                return "Khác";
            default:
                return level.name();
        }
    }
}
