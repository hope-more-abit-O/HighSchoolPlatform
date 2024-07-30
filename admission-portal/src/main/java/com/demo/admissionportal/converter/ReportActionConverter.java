package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.PostReportActionType;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class ReportActionConverter extends AbstractConverter<PostReportActionType, String> {
    @Override
    protected String convert(PostReportActionType actionType) {
        if (actionType == null) {
            return null;
        }
        switch (actionType) {
            case NONE:
                return "KHÔNG";
            case DELETE:
                return "XÓA";
            default:
                return actionType.name();
        }
    }
}
