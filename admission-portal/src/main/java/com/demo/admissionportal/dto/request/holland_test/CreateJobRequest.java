package com.demo.admissionportal.dto.request.holland_test;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Create job request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class CreateJobRequest implements Serializable {
    @NotNull(message = "Tên nghề nghiệp không được trống")
    private String name;
    @NotNull(message = "Ảnh nghề nghiệp không được trống")
    private String image;
}
