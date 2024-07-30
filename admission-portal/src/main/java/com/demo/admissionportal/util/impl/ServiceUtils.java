package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class ServiceUtils {
    public static Integer getId(){
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getId();
    }
}
