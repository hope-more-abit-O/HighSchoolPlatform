package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.MessageStatus;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class MessageStatusConverter extends AbstractConverter<MessageStatus, String> {
    @Override
    protected String convert(MessageStatus status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case RECEIVED:
                return "Đã nhận";
            case DELIVERED:
                return "Đã gửi";
            default:
                return status.name();
        }
    }
}
