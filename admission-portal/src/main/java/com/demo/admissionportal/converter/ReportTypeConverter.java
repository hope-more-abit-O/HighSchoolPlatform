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
                return "Báo cáo bài viết vi phạm ";
            case COMMENT:
                return "Báo cáo bình luận vi phạm";
            case FUNCTION:
                return "Báo lỗi chức năng";
            case OTHER:
                return "Khác";
            default:
                return type.name();
        }
    }
}
