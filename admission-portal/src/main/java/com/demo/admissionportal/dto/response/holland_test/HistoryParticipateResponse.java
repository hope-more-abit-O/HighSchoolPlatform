package com.demo.admissionportal.dto.response.holland_test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class HistoryParticipateResponse implements Serializable {
    private String name;
    private String code;
    private Date createTime;
    private Integer testResponseId;
}
