package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.SubjectStatus;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class SubjectStatusConverter extends AbstractConverter<SubjectStatus, String> {
    @Override
    protected String convert(SubjectStatus status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case ACTIVE:
                return "Hoạt động";
            case INACTIVE:
                return "Không hoạt động";
            default:
                return status.name();
        }
    }
}
