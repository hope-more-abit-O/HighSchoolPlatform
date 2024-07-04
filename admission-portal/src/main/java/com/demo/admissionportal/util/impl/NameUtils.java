package com.demo.admissionportal.util.impl;

import org.springframework.stereotype.Component;

@Component
public class NameUtils { // Or any suitable class name

    public static String getFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null) {
            fullName.append(firstName).append(" ");
        }
        if (middleName != null) {
            fullName.append(middleName).append(" ");
        }
        if (lastName != null) {
            fullName.append(lastName);
        }
        return fullName.toString().trim();
    }
}

