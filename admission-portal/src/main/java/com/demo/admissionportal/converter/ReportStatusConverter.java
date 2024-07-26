package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.ReportStatus;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ReportStatusConverter extends AbstractConverter<ReportStatus, String> {
    @Override
    protected String convert(ReportStatus status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case PENDING:
                return "Chờ duyệt";
            case COMPLETED:
                return "Hoàn thành";
            default:
                return status.name();
        }
    }
}
