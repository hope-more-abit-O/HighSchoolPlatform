package com.demo.admissionportal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.io.Serializable;

/**
 * The type Change status user request dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusUserRequestDTO implements Serializable {
    @Nullable
    private String note;
}
