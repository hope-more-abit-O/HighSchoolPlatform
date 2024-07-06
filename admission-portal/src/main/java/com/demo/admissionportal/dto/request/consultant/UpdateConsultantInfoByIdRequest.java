package com.demo.admissionportal.dto.request.consultant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConsultantInfoByIdRequest extends ConsultantInfoRequest{
    private Integer id;
}
