package com.demo.admissionportal.dto.request.consultant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class CreateConsultantRequest {

    /**
     * The username of the consultant.
     *
     * @NotNull(message = "Tên người dùng không được để trống")
     */
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;

    /**
     * The first name of the consultant.
     *
     * @NotNull(message = "Họ của tư vấn viên không được để trống")
     */
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String firstName;

    /**
     * The middle name of the consultant.
     *
     */
    private String middleName;

    /**
     * The last name of the consultant.
     *
     * @NotNull(message = "Tên của tư vấn viên không được để trống")
     */
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String lastName;

    /**
     * The email address of the consultant.
     *
     * @NotNull(message = "Email không được để trống")
     */
    @NotNull(message = "Email không được để trống")
    private String email;

    /**
     * The phone number of the consultant.
     *
     * @NotNull(message = "Số điện thoại không được để trống")
     * @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
     */
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
    /**
     * Trims whitespace from all fields.
     */
    public void trim() {
        username = username.trim();
        firstName = firstName.trim();
        middleName = middleName.trim();
        lastName = lastName.trim();
        email = email.trim();
        phone = phone.trim();
    }
}
