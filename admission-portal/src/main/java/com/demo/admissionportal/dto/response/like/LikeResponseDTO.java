package com.demo.admissionportal.dto.response.like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Like response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LikeResponseDTO implements Serializable {
    private String currentStatus;
}