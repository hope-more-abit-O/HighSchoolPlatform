package com.demo.admissionportal.dto.request.create_univeristy_request;

import com.demo.admissionportal.constants.UniversityType;
import com.demo.admissionportal.util.enum_validator.EnumName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUniversityRequestRequest {
    @EnumName(message = "Tên trường đại học không được có số!")
    @NotBlank(message = "Tên trường đại học không được để trống!")
    private String universityName;
    @Email(message = "Email phải đúng định dạng")
    @NotBlank(message = "Email trường đại học không được để trống!")
    private String universityEmail;
    @NotBlank(message = "Tên tài khoản trường đại học không được để trống!")
    private String universityUsername;
    @NotBlank(message = "Mã trường đại học không được để trống")
    private String universityCode;
    @NotBlank(message = "Loại đại học không được để trống")
    private UniversityType universityType;
    @NotBlank(message = "Tên các file tài liệu không được trống")
    private String documents;
    private String note;
}
