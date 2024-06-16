package com.demo.admissionportal.dto.request.university;

import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.util.EnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the request for a staff member to register a new university.
 *
 * @author hopeless
 * @version 1.0
 * @since 13/06/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class StaffRegisterUniversityRequestDTO {

    /**
     * The ID of the staff member registering the university.
     *
     * @NotNull(message = "Mã nhân viên không được để trống")
     * @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0")
     */
    @NotNull(message = "Mã nhân viên không được để trống")
    @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0")
    private Integer staffId;

    /**
     * The code of the university.
     *
     * @NotNull(message = "Mã trường không được để trống")
     */
    @NotNull(message = "Mã trường không được để trống")
    private String code;

    /**
     * The username of the university.
     *
     * @NotNull(message = "Tên người dùng không được để trống")
     */
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;

    /**
     * The name of the university.
     *
     * @NotNull(message = "Tên của tư vấn viên không được để trống")
     */
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String name;

    /**
     * The email address of the university.
     *
     * @NotNull(message = "Email không được để trống")
     */
    @NotNull(message = "Email không được để trống")
    private String email;

    /**
     * The description of the university.
     *
     * @NotNull(message = "Mô tả không được để trống")
     */
    @NotNull(message = "Mô tả không được để trống")
    private String description;

    /**
     * The password of the university.
     *
     * @NotNull(message = "Mật khẩu không được để trống")
     * @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&\\s]{8,40}$", message = "Mật khẩu phải từ 8 đến 4o ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt, và không được để khoảng trống ở đầu và cuối!")
     */
    @NotNull(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&\\s]{8,40}$", message = "Mật khẩu phải từ 8 đến 4o ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt, và không được để khoảng trống ở đầu và cuối!")
    private String password;

    /**
     * The phone number of the university.
     *
     * @NotNull(message = "Số điện thoại không được để trống")
     * @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
     */
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    /**
     * The type of the university.
     *
     * @NotNull(message = "Loại trường không được để trống")
     * @EnumValue(name = "type", enumClass = UniversityType.class, message = "Loại trường phải là Công lập hoặc Tư thục")
     */
    @NotNull(message = "Loại trường không được để trống")
    @EnumValue(name = "type", enumClass = UniversityType.class, message = "Loại trường phải là Công lập hoặc Tư thục")
    private String type;

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