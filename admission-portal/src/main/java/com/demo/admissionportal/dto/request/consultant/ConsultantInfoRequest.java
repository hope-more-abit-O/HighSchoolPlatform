package com.demo.admissionportal.dto.request.consultant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultantInfoRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String phone;
    private String specificAddress;
    private Integer provinceId;
    private Integer districtId;
    private Integer wardId;
}
