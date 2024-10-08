package com.demo.admissionportal.dto.request.consultant;

import com.demo.admissionportal.util.enum_validator.EnumNameV2;
import com.demo.admissionportal.util.enum_validator.EnumPhoneV2;
import com.demo.admissionportal.util.enum_validator.EnumWhiteSpace;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
     * @NotNull(message = "Tên người dùng không được để trống.")
     */
    @NotNull(message = "Tên người dùng không được để trống.")
    @EnumWhiteSpace(message = "Tên tài khoản không được có khoảng trắng!")
    private String username;

    /**
     * The first name of the consultant.
     *
     * @NotNull(message = "Họ của tư vấn viên không được để trống.")
     */
    @NotNull(message = "Tên tư vấn viên không được để trống.")
    @EnumNameV2(message = "Tên tư vấn viên chỉ chứa chữ")
    private String firstName;

    /**
     * The middle name of the consultant.
     *
     */
    @EnumNameV2(message = "Tên tư vấn viên chỉ chứa chữ")
    private String middleName;

    /**
     * The last name of the consultant.
     *
     * @NotNull(message = "Tên tư vấn viên không được để trống.")
     */
    @EnumNameV2(message = "Tên tư vấn viên chỉ chứa chữ")
    @NotNull(message = "Tên tư vấn viên không được để trống.")
    private String lastName;

    /**
     * The email address of the consultant.
     *
     * @NotNull(message = "Email không được để trống.")
     */
    @NotNull(message = "Email không được để trống.")
    @Email
    private String email;

    /**
     * The phone number of the consultant.
     *
     * @NotNull(message = "Số điện thoại không được để trống.")
     * @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
     */
    @NotNull(message = "Số điện thoại không được để trống.")
    @EnumPhoneV2(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
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
