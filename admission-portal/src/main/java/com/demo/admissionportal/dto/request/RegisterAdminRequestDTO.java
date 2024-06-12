package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.AccountStatus;
import com.demo.admissionportal.util.EnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Register admin request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class RegisterAdminRequestDTO {
    @NotNull(message = "Tên không thể để trống !")
    private String name;
    @NotNull(message = "Tên đăng nhập không thể để trống !")
    private String username;
    @NotNull(message = "Tên Email không thể để trống !")
    @Email(message = "Email phải có định dạng hợp lệ!")
    private String email;
    @NotNull(message = "Mật khẩu không thể để trống !")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    private String avatar;
    @NotNull(message = "Số điện thoại không thể để trống !")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return (name == null || name.isEmpty()) && (username == null || username.isEmpty()) && (email == null || email.isEmpty()) && (password == null || password.isEmpty()) && (phone == null || phone.isEmpty());
    }
}
