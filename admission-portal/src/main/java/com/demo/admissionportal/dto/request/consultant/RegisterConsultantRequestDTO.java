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
public class RegisterConsultantRequestDTO {
    @NotNull(message = "Id trường đại học không được để trống")
    private Integer universityId;
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String name;
    @NotNull(message = "Email không được để trống")
    private String email;
    @NotNull(message = "Mật khẩu không được để trống")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;

    public void Trim(){
        username = username.trim();
        name = name.trim();
        email = email.trim();
        password = password.trim();
        phone = phone.trim();
    }
}
