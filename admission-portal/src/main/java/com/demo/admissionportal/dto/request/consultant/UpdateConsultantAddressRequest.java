package com.demo.admissionportal.dto.request.consultant;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateConsultantAddressRequest {

    private String specificAddress;

    @NotBlank(message = "Mã tỉnh không thể để trống!")
    private Integer provinceId;

    @NotBlank(message = "Mã huyện không thể để trống!")
    private Integer districtId;

    @NotBlank(message = "Mã xã không thể để trống!")
    private Integer wardId;
}
