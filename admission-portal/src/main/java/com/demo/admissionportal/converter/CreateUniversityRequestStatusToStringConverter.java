package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.CreateUniversityRequestStatus;
import org.modelmapper.AbstractConverter;

public class CreateUniversityRequestStatusToStringConverter extends AbstractConverter<CreateUniversityRequestStatus, String> {
    @Override
    protected String convert(CreateUniversityRequestStatus status) {
        switch (status) {
            case ACCEPTED:
                return "Chấp nhận";
            case REJECTED:
                return "Từ chối";
            case PENDING:
                return "Chờ duyệt";
            default:
                return "Không biết";
        }
    }
}
