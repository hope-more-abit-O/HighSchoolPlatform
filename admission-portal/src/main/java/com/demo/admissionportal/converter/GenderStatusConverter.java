package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.Gender;
import org.modelmapper.AbstractConverter;

public class GenderStatusConverter extends AbstractConverter<Gender, String> {

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
