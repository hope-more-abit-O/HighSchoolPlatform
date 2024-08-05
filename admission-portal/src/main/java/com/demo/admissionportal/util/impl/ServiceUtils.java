package com.demo.admissionportal.util.impl;

import com.demo.admissionportal.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.module.ResolutionException;

public class ServiceUtils {
    public static Integer getId(){
        try {
            return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                    .getId();
        } catch (Exception e){
            throw new ResolutionException("Could not get id from user");
        }
    }
}
