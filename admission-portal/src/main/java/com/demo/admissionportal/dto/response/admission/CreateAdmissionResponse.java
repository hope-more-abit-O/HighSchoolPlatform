package com.demo.admissionportal.dto.response.admission;

import com.demo.admissionportal.dto.entity.major.InfoMajorDTO;
import com.demo.admissionportal.dto.entity.method.CreateMethodDTO;
import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAdmissionResponse implements Serializable {
    private Integer admissionId;
    private Integer year;
    private List<InfoMethodDTO> methods;
    private List<InfoMajorDTO> majors;
}
