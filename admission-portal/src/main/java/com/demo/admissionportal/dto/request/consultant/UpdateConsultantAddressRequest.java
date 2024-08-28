package com.demo.admissionportal.dto.request.consultant;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateConsultantAddressRequest {

    private String specificAddress;

    @NotNull(message = "Mã tỉnh không thể để trống!")
    private Integer provinceId;

    @NotNull(message = "Mã huyện không thể để trống!")
    private Integer districtId;

    @NotNull(message = "Mã xã không thể để trống!")
    private Integer wardId;
}
