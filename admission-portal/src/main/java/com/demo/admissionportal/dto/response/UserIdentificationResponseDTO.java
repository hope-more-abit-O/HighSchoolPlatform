package com.demo.admissionportal.dto.response;

import com.demo.admissionportal.dto.RegisteredIdentificationNumberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserIdentificationResponseDTO {
    private Integer userId;
    private String email;
    private List<RegisteredIdentificationNumberDTO> registeredIdentificationNumber;
    private Date createTime;
}
