package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.constants.AdmissionScoreStatus;
import org.modelmapper.AbstractConverter;

public class AdmissionScoreStatusConverter  extends AbstractConverter<AdmissionScoreStatus, String> {

    /**
     * Converts an {@link AccountStatus} enum value to its corresponding Vietnamese translation.
     *
     * @param scoreStatus The {@link AccountStatus} enum value to convert.
     * @return The Vietnamese translation of the account status.
     */
    @Override
    protected String convert(AdmissionScoreStatus scoreStatus) {
        switch (scoreStatus) {
            case COMPLETE:
                return "Hoàn thành";
            case EMPTY:
                return "Chưa cập nhật";
            case PARTIAL:
                return "Đang cập nhật";
            default:
                return "Không biết";
        }
    }
}