package com.demo.admissionportal.service;

import com.demo.admissionportal.exception.exceptions.DataExistedException;

public interface ValidationService {
    /**
     * Checks if a username is already taken.
     *
     * <p>Queries the database using the `userRepository`
     * to determine if a user with the given username already exists.
     *
     * <p> Example Usage:
     * <pre>
     * {@code
     *  try {
     *     String usernameToCheck = "john.doe";
     *     boolean isValid = validationService.validateUsername(usernameToCheck);
     *
     *     if (isValid) {
     *         // Proceed with registration as the username is available
     *     }
     *  } catch (DataExistedException e) {
     *      // Handle the username already being taken, such as:
     *      // - Informing the user to choose another username.
     *      // - (Error logging is handled within the method)
     *  }
     * }
     * </pre>
     *
     * @param username The username string to check for availability.
     * @return `true` if the username is available (not taken). Throws
     *        a `DataExistedException` otherwise.
     * @throws DataExistedException  If a user with the given username
     *                               is found in the database.
     */
    public boolean validateUsername(String username) throws DataExistedException;
    /**
     * Checks if an email address is already associated with a user account.
     *
     * <p>This method queries the `userRepository` to ensure the
     * provided email is not already registered.
     *
     * <p>Example Usage:
     * <pre>
     * {@code
     *  try {
     *      String emailToValidate = "test@example.com";
     *      boolean isEmailAvailable = validationService.validateEmail(emailToValidate);
     *
     *      if (isEmailAvailable) {
     *          // The email address is available; proceed with account creation.
     *      }
     *  } catch (DataExistedException e) {
     *      // The email is already in use
     *      //  - Handle the exception, typically by notifying the user to choose a different email.
     *      //  - (The error is already logged within this method)
     *  }
     * }
     * </pre>
     *
     * @param email The email address to validate.
     * @return `true` if the email is available (not in use); throws
     *         a `DataExistedException` otherwise.
     * @throws DataExistedException If a user with the specified email already exists.
     */
    public boolean validateEmail(String email) throws DataExistedException;
    /**
     * Checks if a phone number is already associated with any user, consultant, or university account.
     *
     * <p> This method queries multiple repositories (`userInfoRepository`, `consultantInfoRepository`, `universityInfoRepository`)
     * to determine if the given phone number is already in use within the system.
     *
     * <p>Example Usage:
     * <pre>
     * {@code
     *  try {
     *      String phoneNumberToValidate = "1234567890";
     *      boolean isValid = validationService.validatePhoneNumber(phoneNumberToValidate);
     *      if (isValid) {
     *          // Phone number is not already in use. Proceed with account creation.
     *      }
     *  } catch (DataExistedException e) {
     *      // Phone number is already taken
     *      // - Handle the exception, typically by notifying the user to choose another number.
     *      // - You can optionally log the exception (as it's already logged within the method)
     *  }
     * }
     * </pre>
     *
     * @param phone The phone number to validate.
     * @return `true` if the phone number is available;
     *         throws a {@link DataExistedException} if the phone number is already in use.
     * @throws DataExistedException If the provided phone number already exists in any of
     *                              the checked repositories.
     */
    public boolean validatePhoneNumber(String phone) throws DataExistedException;
    /**
     * Validates username, email, and phone number for registration.
     *
     * <p>This method provides a convenient way to validate all
     * three inputs (`username`, `email`, `phone`) for a new
     * user registration.
     *
     * <p>Example Usage:
     * <pre>
     * {@code
     * try {
     *     String username = "jane.doe";
     *     String email = "jane.doe@example.com";
     *     String phone = "9876543210";
     *
     *     boolean isValid = validationService.validateRegister(username, email, phone);
     *
     *     if (isValid) {
     *         // Proceed with the registration process
     *         // ...
     *     }
     * } catch (DataExistedException e) {
     *     // One or more inputs are already in use
     *     //  - Handle the error, providing feedback to the user.
     *     //  - (The specific error message will be in 'e.getMessage()')
     * }
     * }
     * </pre>
     *
     * @param username The desired username for the new user.
     * @param email    The email address of the new user.
     * @param phone    The phone number of the new user.
     * @return `true` if all the inputs (username, email, and phone)
     *         are available for a new user. Otherwise, throws
     *         a {@link DataExistedException}.
     * @throws DataExistedException if any of the provided values
     *                             (username, email, phone) are already in use.
     *
     * @see #validateUsername(String)
     * @see #validateEmail(String)
     * @see #validatePhoneNumber(String)
     */
    public boolean validateRegister(String username, String email, String phone) throws DataExistedException;
    /**
     * Validates username, email, and phone number for registration.
     *
     * <p>This method provides a convenient way to validate all
     * three inputs (`username`, `email`) for a new
     * user registration.
     *
     * <p>Example Usage:
     * <pre>
     * {@code
     * try {
     *     String username = "jane.doe";
     *     String email = "jane.doe@example.com";
     *
     *     boolean isValid = validationService.validateRegister(username, email);
     *
     *     if (isValid) {
     *         // Proceed with the registration process
     *         // ...
     *     }
     * } catch (DataExistedException e) {
     *     // One or more inputs are already in use
     *     //  - Handle the error, providing feedback to the user.
     *     //  - (The specific error message will be in 'e.getMessage()')
     * }
     * }
     * </pre>
     *
     * @param username The desired username for the new user.
     * @param email    The email address of the new user.
     * @return `true` if all the inputs (username, email)
     *         are available for a new user. Otherwise, throws
     *         a {@link DataExistedException}.
     * @throws DataExistedException if any of the provided values
     *                             (username, email, phone) are already in use.
     *
     * @see #validateUsername(String)
     * @see #validateEmail(String)
     */
    public boolean validateRegister(String username, String email) throws DataExistedException;
    boolean validateCreateUniversityRequest(String username, String email, String code) throws DataExistedException;
    boolean validateCreateUniversity(String username, String email, String code) throws DataExistedException;
    boolean validateAddress(Integer provinceId) throws ClassNotFoundException;
}
