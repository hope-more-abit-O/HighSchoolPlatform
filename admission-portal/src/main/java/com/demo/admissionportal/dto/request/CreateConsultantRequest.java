package com.demo.admissionportal.dto.request;

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

    @NotNull
    private String gender;

    @NotNull
    private String specificAddress;

    @NotNull
    private Integer provinceId;

    @NotNull
    private Integer districtId;

    @NotNull
    private Integer wardId;
    /**
     * Trims whitespace from all fields.
     */
    public void trim() {
        username = username.trim();
        firstName = firstName.trim();
        middleName = middleName.trim();
        lastName = lastName.trim();
        email = email.trim();
        password = password.trim();
        phone = phone.trim();
    }
}
