package com.demo.admissionportal.converter;

import com.demo.admissionportal.constants.Role;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
public class RoleToStringConverter extends AbstractConverter<Role, String> {

    @Override
    protected String convert(Role role) {
        if (role == null) {
            return null;
        }

        switch (role) {
            case ADMIN:
                return "Quản trị viên";
            case STAFF:
                return "Nhân viên";
            case CONSULTANT:
                return "Tư vấn viên";
            case USER:
                return "Người dùng";
            case UNIVERSITY:
                return "Trường đại học";
            default:
                return role.name(); // Default to English name if not found
        }
    }
}