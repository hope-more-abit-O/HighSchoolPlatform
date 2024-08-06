package com.demo.admissionportal.dto.entity.admission;

import com.demo.admissionportal.dto.entity.method.InfoMethodDTO;
import com.demo.admissionportal.entity.Method;
import com.demo.admissionportal.entity.admission.AdmissionMethod;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdmissionMethodDTO {
    private Integer id;
    private Integer admissionId;
    private InfoMethodDTO method;

    public AdmissionMethodDTO(AdmissionMethod admissionMethod, List<Method> methods) {
        this.id = admissionMethod.getId();
        this.admissionId = admissionMethod.getAdmissionId();
        this.method = methods.stream()
                .filter( (element) -> element.getId().equals(admissionMethod.getMethodId()))
                .map(InfoMethodDTO::new)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phương thức tuyển sinh.", Map.of("admisionMethodId", admissionMethod.getMethodId().toString())));
    }
}
