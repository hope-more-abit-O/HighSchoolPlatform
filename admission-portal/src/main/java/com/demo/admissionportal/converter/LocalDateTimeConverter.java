package com.demo.admissionportal.converter;

import org.modelmapper.AbstractConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Converter class for mapping {@link LocalDateTime} objects to formatted strings.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
public class LocalDateTimeConverter extends AbstractConverter<LocalDateTime, String> {

    /**
     * Converts a {@link LocalDateTime} object to a formatted string using the pattern "HH:mm:ss dd/MM/yyyy".
     *
     * @param localDateTime The {@link LocalDateTime} object to convert.
     * @return The formatted string representation of the date and time.
     */
    @Override
    protected String convert(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDateTime.format(formatter);
    }
}