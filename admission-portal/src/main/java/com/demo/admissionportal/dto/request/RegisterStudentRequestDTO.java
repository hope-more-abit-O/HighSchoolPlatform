package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.EnumPassword;
import com.demo.admissionportal.util.EnumPhone;
import com.demo.admissionportal.util.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Register student request.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RegisterStudentRequestDTO implements Serializable {
    @NotNull(message = "Username không thể để trống")
    private String username;
    @NotNull(message = "Firstname không thể để trống")
    private String firstname;
    @NotNull(message = "Middle_Name không thể để trống")
    private String middle_name;
    @NotNull(message = "Lastname không thể để trống")
    private String lastname;
    @NotNull(message = "Tên Email không thể để trống !")
    @Email(message = "Email phải có định dạng hợp lệ!")
    private String email;
    @NotNull(message = "Mật khẩu không thể để trống !")
    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    private String password;
    @NotNull(message = "Địa chỉ không thể để trống")
    private int address;
    @NotNull(message = "Ngày sinh không thể để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Ngày sinh không được vượt ngày hiện tại")
    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    private Date birthday;
    @NotNull(message = "Lớp không thể để trống")
    private int educationLevel;
    private String avatar;
    @NotNull(message = "Số điện thoại không thể để trống !")
    @EnumPhone(message = "Số điện thoại phải bắt đầu bằng số 0 và chứa 10-11 chữ số!")
    private String phone;
    @NotNull(message = "Giới tính không thể để trống")
    @EnumValue(name = "type", enumClass = Gender.class, message = "Giới tính phải đúng format")
    private String gender;
}
