package com.demo.admissionportal.dto.entity.chat;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDetailDTO {
    private Integer senderId;
    private String content;
    private String type;
    private Date time;
}
