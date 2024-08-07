package com.demo.admissionportal.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The type Payment response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQrResponseDTO {
    private long orderCode;
    private String statusPayment;
    private String checkoutURL;
    private Integer universityTransactionId;
    private Integer postId;
    private Integer packageId;
}
