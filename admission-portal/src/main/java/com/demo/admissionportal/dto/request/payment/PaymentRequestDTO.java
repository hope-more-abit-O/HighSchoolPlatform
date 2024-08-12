package com.demo.admissionportal.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * The type Payment request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequestDTO implements Serializable {
    @NotNull(message = "Nhập mã orderCode PayOS")
    private long orderCode;
    private List<PaymentInfo> transaction;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo implements Serializable {
        @NotNull(message = "Nhập mã giao dịch")
        private Integer universityTransactionId;
        @NotNull(message = "Nhập postId")
        private Integer postId;
        @NotNull(message = "Nhập packageId")
        private Integer packageId;
    }
}
