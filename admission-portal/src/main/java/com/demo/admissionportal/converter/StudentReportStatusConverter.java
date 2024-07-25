package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.StudentReportStatus;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class StudentReportStatusConverter extends AbstractConverter<StudentReportStatus, String> {
    @Override
    protected String convert(StudentReportStatus status) {
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
