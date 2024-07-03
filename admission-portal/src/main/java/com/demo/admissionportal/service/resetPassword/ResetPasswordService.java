package com.demo.admissionportal.service.resetPassword;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.dto.request.resetPass.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.resetPass.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * The interface for the ResetPassword service.
 */
@Service
public interface ResetPasswordService {
    /**
     * Handles the reset password request.
     *
     * @param request the reset password request
     * @return the response data
     */
    ResponseData<?> resetPasswordRequest(ResetPasswordRequest request);

    /**
     * Confirms the reset password request.
     *
     * @param request the confirm reset password request
     * @param resetToken the reset token
     * @return the response data
     */
    ResponseData<?> confirmResetPassword(ConfirmResetPasswordRequest request, String resetToken);

    /**
     * Retrieves the reset password account from Redis cache using the reset token.
     *
     * @param resetToken the reset token
     * @return the reset password account Redis cache DTO
     */
    ResetPasswordAccountRedisCacheDTO getResetPasswordAccountRedisCacheDTO(UUID resetToken);

    /**
     * Saves the reset password account to Redis cache.
     *
     * @param role the role of the account
     * @param id the ID of the account
     * @param resetToken the reset token
     */
    void saveObject(Role role, Integer id, UUID resetToken);
}
