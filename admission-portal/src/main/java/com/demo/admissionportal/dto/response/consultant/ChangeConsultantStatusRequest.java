package com.demo.admissionportal.dto.response.consultant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeConsultantStatusRequest {
    @Nullable
    private String note;
}
