package com.demo.admissionportal.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Mã orderCode PayOS bị trống")
    private long orderCode;
}
