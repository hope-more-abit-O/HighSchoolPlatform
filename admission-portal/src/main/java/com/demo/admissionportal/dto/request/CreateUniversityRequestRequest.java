package com.demo.admissionportal.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUniversityRequestRequest {
    @NotNull(message = "Tên trường đại học không được để trống!")
    private String universityName;
    @NotNull(message = "Email trường đại học không được để trống!")
    private String universityEmail;
    @NotNull(message = "Tên tài khoản trường đại học không được để trống!")
    private String universityUsername;
    @NotNull(message = "Mã trường đại học không được để trống")
    private String universityCode;
    @NotNull(message = "Loại đại học không được để trống")
    private String universityType;
    @NotNull(message = "Tên các file tài liệu không được trống")
    private String documents;
    private String note;
}
