package com.demo.admissionportal.dto.request.consultant;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the request for registering a new consultant for a university.
 *
 * @author hopeless
 * @version 1.0
 * @since 12/06/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class UniversityRegisterConsultantRequestDTO {

    /**
     * The ID of the university.
     *
     * @NotNull(message = "Id trường đại học không được để trống")
     */
    @NotNull(message = "Id trường đại học không được để trống")
    private Integer universityId;

    /**
     * The username of the consultant.
     *
     * @NotNull(message = "Tên người dùng không được để trống")
     */
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;

    /**
     * The name of the consultant.
     *
     * @NotNull(message = "Tên của tư vấn viên không được để trống")
     */
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String name;

    /**
     * The email address of the consultant.
     *
     * @NotNull(message = "Email không được để trống")
     */
    @NotNull(message = "Email không được để trống")
    private String email;

    /**
     * The password of the consultant.
     *
     * @NotNull(message = "Mật khẩu không được để trống")
     * @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&\\s]{8,40}$", message = "Mật khẩu phải từ 8 đến 4o ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt, và không được để khoảng trống ở đầu và cuối!")
     */
    @NotNull(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&\\s]{8,40}$", message = "Mật khẩu phải từ 8 đến 4o ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt, và không được để khoảng trống ở đầu và cuối!")
    private String password;

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
        name = name.trim();
        email = email.trim();
        password = password.trim();
        phone = phone.trim();
    }
}