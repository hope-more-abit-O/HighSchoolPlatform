package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.ReportType;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ReportTypeConverter extends AbstractConverter<ReportType, String> {
    @Override
    protected String convert(ReportType type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case POST:
                return "Bài viết không phù hợp ";
            case COMMENT:
                return "Bình luận không phù hợp";
            case FUNCTION:
                return "Khác";
            default:
                return type.name();
        }
    }
}
