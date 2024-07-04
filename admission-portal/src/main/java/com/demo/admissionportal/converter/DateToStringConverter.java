package com.demo.admissionportal.converter;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Converts a `java.util.Date` object to a String representation in the format "dd-MM-yyyy".
 *
 * <p> This converter extends the `AbstractConverter` class from Spring Framework's conversion system.
 * It provides a way to convert `Date` objects to Strings during data binding or other conversion scenarios.
 *
 * @see AbstractConverter
 * @see Date
 * @see SimpleDateFormat
 */
@Component
public class DateToStringConverter extends AbstractConverter<Date, String> {

    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Converts a `java.util.Date` object to a String representation in the predefined format.
     *
     * <p>This method implements the core conversion logic of the converter.
     * It checks if the provided `source` Date object is null. If null, it returns null to avoid formatting issues.
     * Otherwise, it uses the pre-configured {@link SimpleDateFormat} instance (`dateFormat`) with the format "dd-MM-yyyy"
     * to format the `source` Date object into a String representation and returns the formatted String.
     *
     * @param source The `java.util.Date` object to be converted to a String.
     * @return The formatted String representation of the `source` Date object in the format "dd-MM-yyyy", or null if the source is null.
     */
    @Override
    protected String convert(Date source) {
        return source == null ? null : dateFormat.format(source);
    }
}