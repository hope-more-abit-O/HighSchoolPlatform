package com.demo.admissionportal.dto.request.authen;

import com.demo.admissionportal.constants.EducationLevel;
import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Register user request dto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class RegisterUserRequestDTO implements Serializable {
    @NotBlank(message = "Username không được để trống")
    @EnumWhiteSpace(message = "Username không để khoảng trắng ở giữa")
    private String username;

    @Email(message = "Email phải đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @EnumPassword(message = "Mật khẩu phải từ 8 đến 16 ký tự, bao gồm ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và 1 ký tự đặc biệt!")
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Họ tên không được để trống")
    @EnumName(message = "Họ tên chỉ chứa chữ")
    private String firstName;

    @NotBlank(message = "Tên đệm không được để trống")
    @EnumName(message = "Tên đệm chỉ chứa chữ")
    private String middleName;

    @NotBlank(message = "Tên không được để trống")
    @EnumName(message = "Tên chỉ chứa chữ")
    private String lastName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @EnumPhone(message = "Số điện thoại từ 10 - 11")
    private String phone;

    @NotBlank(message = "Giới tính không thể để trống")
    @EnumValue(name = "type", enumClass = Gender.class, message = "Giới tính phải đúng format")
    private String gender;

    @NotBlank(message = "Số nhà, tên đường không được để trống")
    private String specific_address;

    @NotBlank(message = "Trình độ học vấn không được trống")
    @EnumValue(name = "type", enumClass = EducationLevel.class, message = "Trình độ học vấn phải đúng format")
    private String education_level;

    @NotBlank(message = "Thành phố không được để trống")
    private Integer province_id;

    @NotBlank(message = "Quận không được để trống")
    private Integer district_id;

    @NotBlank(message = "Phường không được để trống")
    private Integer ward_id;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializers.DateDeserializer.class)
    @PastOrPresent(message = "Ngày sinh không được vượt ngày hiện tại")
    @Temporal(TemporalType.DATE)
    @NotBlank(message = "Ngày sinh không được để trống")
    private Date birthday;

    private String provider;
}
