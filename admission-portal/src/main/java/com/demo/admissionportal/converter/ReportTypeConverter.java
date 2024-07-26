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
                return "Bài viết";
            case COMMENT:
                return "Bình luận";
            case FUNCTION:
                return "Chức năng";
            default:
                return type.name();
        }
    }
}
