package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.AdmissionStatus;
import com.demo.admissionportal.constants.SubjectStatus;
import org.modelmapper.AbstractConverter;

public class AdmissionStatusConverter extends AbstractConverter<AdmissionStatus, String> {
    @Override
    protected String convert(AdmissionStatus status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case ACTIVE:
                return "Hoạt động";
            case INACTIVE:
                return "Không hoạt động";
            case PENDING:
                return "Chờ duyệt";
            case DENY:
                return "Từ chối";
            default:
                return status.name();
        }
    }
}