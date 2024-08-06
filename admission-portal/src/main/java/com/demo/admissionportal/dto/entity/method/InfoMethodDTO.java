package com.demo.admissionportal.dto.entity.method;

import com.demo.admissionportal.entity.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoMethodDTO implements Serializable {
    private Integer id;
    private String name;
    private String code;
    private String status;

    public InfoMethodDTO(Method method) {
        this.id = method.getId();
        this.name = method.getName();
        this.code = method.getCode();
        this.status = method.getStatus().name();
    }
}
