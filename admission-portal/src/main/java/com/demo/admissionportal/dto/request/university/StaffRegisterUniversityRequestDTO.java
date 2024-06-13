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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Valid
public class StaffRegisterUniversityRequestDTO {
    @NotNull(message = "Mã nhân viên không được để trống")
    @Min(value = 1, message = "Mã nhân viên phải lớn hơn 0")
    private Integer staffId;
    @NotNull(message = "Mã trường không được để trống")
    private String code;
    @NotNull(message = "Tên người dùng không được để trống")
    private String username;
    @NotNull(message = "Tên của tư vấn viên không được để trống")
    private String name;
    @NotNull(message = "Email không được để trống")
    private String email;
    @NotNull(message = "Mô tả không được để trống")
    private String description;
    @NotNull(message = "Mật khẩu không được để trống")
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$", message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    @NotNull(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "0\\d{9,10}", message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
    @NotNull(message = "Loại trường không được để trống")
    @EnumValue(name = "type", enumClass = UniversityType.class, message = "Loại trường phải là Công lập hoặc Tư thục")
    private String type;
    public void trim(){
        username = username.trim();
        name = name.trim();
        email = email.trim();
        password = password.trim();
        phone = phone.trim();
    }
}
