package com.demo.admissionportal.service.resetPassword;

import com.demo.admissionportal.constants.Role;
import com.demo.admissionportal.dto.request.redis.ResetPasswordAccountRedisCacheDTO;
import com.demo.admissionportal.dto.request.resetPass.request.ConfirmResetPasswordRequest;
import com.demo.admissionportal.dto.request.resetPass.request.ResetPasswordRequest;
import com.demo.admissionportal.dto.response.ResponseData;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * <h1>Reset Password Service Interface</h1>
 * <p>
 * This interface defines the operations related to resetting passwords within the Admission Portal.
 * It provides methods for handling reset password requests, confirming reset password requests, and
 * managing reset password tokens in Redis cache. Implementations of this interface are responsible
 * for the business logic associated with password reset management and interactions with the underlying data layer.
 * </p>
 * <p>
 * The following operations are supported:
 * <ul>
 *     <li>Handling reset password requests</li>
 *     <li>Confirming reset password requests</li>
 *     <li>Retrieving reset password account information from Redis cache</li>
 *     <li>Saving reset password account information to Redis cache</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
@Service
public interface ResetPasswordService {
    /**
     * <h2>Reset Password Request</h2>
     * <p>
     * Handles the initial request to reset a password. This involves generating a reset token and sending
     * an email to the user with instructions on how to reset their password.
     * </p>
     *
     * @param request The {@link ResetPasswordRequest} containing the details of the reset password request.
     * @return A {@link ResponseData} object containing the result of the reset password request operation.
     * @since 1.0
     */
    ResponseData<?> resetPasswordRequest(ResetPasswordRequest request);

    /**
     * <h2>Confirm Reset Password</h2>
     * <p>
     * Confirms the reset password request by validating the reset token and updating the user's password.
     * </p>
     *
     * @param request    The {@link ConfirmResetPasswordRequest} containing the new password details.
     * @param resetToken The reset token to be validated.
     * @return A {@link ResponseData} object containing the result of the confirm reset password operation.
     * @since 1.0
     */
    ResponseData<?> confirmResetPassword(ConfirmResetPasswordRequest request, String resetToken);

    /**
     * <h2>Get Reset Password Account from Redis Cache</h2>
     * <p>
     * Retrieves the reset password account information from the Redis cache using the provided reset token.
     * </p>
     *
     * @param resetToken The reset token used to retrieve the account information.
     * @return A {@link ResetPasswordAccountRedisCacheDTO} containing the account information.
     * @since 1.0
     */
    ResetPasswordAccountRedisCacheDTO getResetPasswordAccountRedisCacheDTO(UUID resetToken);

    /**
     * <h2>Save Reset Password Account to Redis Cache</h2>
     * <p>
     * Saves the reset password account information to the Redis cache with the provided role, account ID, and reset token.
     * </p>
     *
     * @param role       The {@link Role} of the account.
     * @param id         The ID of the account.
     * @param resetToken The reset token to be saved.
     * @since 1.0
     */
    void saveObject(Role role, Integer id, UUID resetToken);
}
