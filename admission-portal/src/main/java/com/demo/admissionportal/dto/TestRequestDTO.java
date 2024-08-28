package com.demo.admissionportal.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "id")
    private int id;
    @NotBlank(message = "name")
    private String name;
    @NotBlank(message = "")
    private String documents;
}
