package com.demo.admissionportal.dto.response.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadMultipleFilesResponse {
    private List<String> url;
}
