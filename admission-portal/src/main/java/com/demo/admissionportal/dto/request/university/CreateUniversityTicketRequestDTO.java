package com.demo.admissionportal.dto.request.university;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUniversityTicketRequestDTO {
    @NotNull(message = "Tên trường đại học không được để trống!")
    private String universityName;
    @NotNull(message = "Mã trường đại học không được để trống")
    private String universityCode;
    @NotNull(message = "Tên các file tài liệu không được trống")
    private String documents;
}
