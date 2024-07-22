package com.demo.admissionportal.dto.response.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Comment response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentResponseDTO implements Serializable {
    private CommentDetailResponseDTO comment;
}
