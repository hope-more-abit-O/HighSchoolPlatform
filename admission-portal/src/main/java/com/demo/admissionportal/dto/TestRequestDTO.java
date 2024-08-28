package com.demo.admissionportal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestDTO {
    @NotNull(message = "id")
    private int id;
    @NotNull(message = "name")
    private String name;
    @NotNull(message = "")
    private String documents;
}
