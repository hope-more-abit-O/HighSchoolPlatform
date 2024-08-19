package com.demo.admissionportal.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResponseABCDTO {
    private Integer a;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer b;

    public TestResponseABCDTO(Integer a) {
        this.a = a;
        this.b = null;
    }
}
