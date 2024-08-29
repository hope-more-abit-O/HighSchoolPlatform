package com.demo.admissionportal.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO implements Serializable {
    private long orderCode;
    private Integer postId;
    private String title;
    private String url;
    private String packageName;
    private int price;
    private Date createTime;
    private String status;
}
