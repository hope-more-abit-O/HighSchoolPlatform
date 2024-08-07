package com.demo.admissionportal.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Payment request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentRequestDTO implements Serializable {
    @NotNull(message = "Nhập mã orderCode PayOS")
    private long orderCode;
    @NotNull(message = "Nhập mã giao dịch")
    private Integer universityIdTransactionId;
    @NotNull(message = "Nhập postId")
    private Integer postId;
    @NotNull(message = "Nhập packageId")
    private Integer packageId;
}
