package com.demo.admissionportal.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * The type Payment response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateQrResponseDTO {
    private long orderCode;
    private String statusPayment;
    private String checkoutURL;

    /**
     * The type Info transaction dto.
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class InfoTransactionDTO implements Serializable {
        private Integer universityTransactionId;
        private Integer postId;
        private Integer packageId;
    }
}
