package com.demo.admissionportal.dto.request.consultant;

import com.demo.admissionportal.constants.Gender;
import com.demo.admissionportal.util.enum_validator.EnumName;
import com.demo.admissionportal.util.enum_validator.EnumPhone;
import com.demo.admissionportal.util.enum_validator.EnumSpecificAddress;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConsultantInfoByIdRequest extends ConsultantInfoRequest{
    @NotNull(message = "Id nhân viên không được để trống.")
    private Integer id;

}
