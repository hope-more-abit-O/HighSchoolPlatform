package com.demo.admissionportal.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * The type Request subject dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RequestSubjectDTO implements Serializable {
    /**
     * The Name.
     */
    @NotBlank(message = "Tên môn học trống hoặc rỗng")
    @Size(max = 40)
    private String name;
}