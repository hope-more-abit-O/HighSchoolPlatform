package com.demo.admissionportal.dto.entity.method;

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
}
