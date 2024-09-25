package com.demo.admissionportal.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationRequest implements Serializable {
    private List<String> fcmToken;
    private String title;
    private String body;
}
