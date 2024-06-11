package com.demo.admissionportal.dto.request;

import com.demo.admissionportal.constants.SubjectStatus;
import com.demo.admissionportal.util.EnumValue;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    /**
     * The Status.
     */
    @NotNull(message = "Trạng thái môn học trống hoặc rỗng")
    @EnumValue(name = "type", enumClass = SubjectStatus.class, message = "Trạng thái môn học là ACTIVE hoặc INACTIVE")
    private String status;
}