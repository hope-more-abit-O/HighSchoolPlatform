package com.demo.admissionportal.entity.resetPassword;

import com.demo.admissionportal.constants.Role;

/**
 * Interface representing a reset password.
 */
public interface ResetPassword {
    /**
     * Gets the email.
     *
     * @return the email
     */
    String getEmail();
    /**
     * Sets the new password.
     *
     * @param password the new password
     */
    void setPassword(String password);

    /**
     * Gets the ID.
     *
     * @return the ID
     */
    Integer getId();

    /**
     * Gets the role.
     *
     * @return the role
     */
    Role getRole();
}