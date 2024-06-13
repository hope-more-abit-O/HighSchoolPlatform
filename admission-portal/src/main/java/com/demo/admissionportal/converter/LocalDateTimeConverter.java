package com.demo.admissionportal.converter;

import org.modelmapper.AbstractConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter extends AbstractConverter<LocalDateTime, String> {
    @Override
    protected String convert(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
        return localDateTime.format(formatter);
    }
}