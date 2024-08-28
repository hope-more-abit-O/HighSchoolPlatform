package com.demo.admissionportal.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Tên môn học trống hoặc rỗng")
    private String name;

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }
}