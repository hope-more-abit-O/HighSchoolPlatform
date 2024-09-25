package com.demo.admissionportal.dto.request.notification;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Tên thiết bị không được trống")
    private List<String> fcmToken;
    @NotNull(message = "Tiêu đề không được trống")
    private String title;
    @NotNull(message = "Nội dung không được trống")
    private String body;
}
