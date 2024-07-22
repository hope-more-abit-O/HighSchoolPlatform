package com.demo.admissionportal.dto.response.type;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeListResponseDTO implements Serializable {
    private Integer id;
    private String name;
    private String createBy;
    private Date createTime;
    private Date updateTime;
    private String updateBy;
    private PostPropertiesStatus status;
}
