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
    @NotNull(message = "Mã orderCode PayOS bị trống")
    private long orderCode;
}
