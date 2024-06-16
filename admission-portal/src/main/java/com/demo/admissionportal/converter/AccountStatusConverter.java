package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.AccountStatus;
import org.modelmapper.AbstractConverter;

/**
 * Converter class for mapping {@link AccountStatus} enum values to Vietnamese translations.
 *
 * @author hopeless
 * @version 1.0
 * @since 16/06/2024
 */
public class AccountStatusConverter extends AbstractConverter<AccountStatus, String> {

    /**
     * Converts an {@link AccountStatus} enum value to its corresponding Vietnamese translation.
     *
     * @param accountStatus The {@link AccountStatus} enum value to convert.
     * @return The Vietnamese translation of the account status.
     */
    @Override
    protected String convert(AccountStatus accountStatus) {
        switch (accountStatus) {
            case ACTIVE:
                return "Hoạt động";
            case INACTIVE:
                return "Không hoạt động";
            case PENDING:
                return "Chờ duyệt";
            default:
                return "Không biết";
        }
    }
}