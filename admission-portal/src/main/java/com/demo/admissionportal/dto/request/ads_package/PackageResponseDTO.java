package com.demo.admissionportal.dto.request.ads_package;

import com.demo.admissionportal.constants.UniversityTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * The type Package response dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PackageResponseDTO implements Serializable {
    private InfoUniversity infoUniversity;
    private InfoPackage infoPackage;
    private Integer postId;
    private Date createTime;
    private Date completeTime;
    private String status;
    private Long orderCode;
    private String description;

    /**
     * The type Info university.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoUniversity implements Serializable {
        private Integer id;
        private String name;
    }

    /**
     * The type Info payment.
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InfoPackage implements Serializable {
        private Integer packageId;
        private String packageName;
    }
}
