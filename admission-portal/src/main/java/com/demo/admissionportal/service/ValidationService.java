package com.demo.admissionportal.service;

public interface ValidationService {
    public boolean validateUsernameAndEmailAvailable(String username, String email) throws Exception;
}
