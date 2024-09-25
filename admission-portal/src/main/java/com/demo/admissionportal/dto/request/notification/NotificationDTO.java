package com.demo.admissionportal.dto.request.notification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Notification request.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NotificationDTO implements Serializable {
    private MessageRequest message;

    /**
     * The type Message request.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class MessageRequest implements Serializable {
        private DataRequest notification;
        private String token;

        /**
         * The type Data request.
         */
        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @Builder
        public static class DataRequest implements Serializable {
            @NotNull(message = "Title không được trống")
            private String title;
            @NotNull(message = "Body không được trống")
            private String body;
        }
    }
}
