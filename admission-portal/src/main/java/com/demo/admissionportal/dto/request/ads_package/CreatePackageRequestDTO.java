package com.demo.admissionportal.dto.request.ads_package;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The type Create package request dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreatePackageRequestDTO implements Serializable {
    @NotBlank(message = "Tên gói quảng cáo không được trống")
    private String name;
    @NotBlank(message = "Mô tả quảng cáo không được trống")
    private String description;
    @NotBlank(message = "Ảnh quảng cáo không được trống")
    private String image;
    @NotBlank(message = "Số lượng tiếp cận không được trống")
    private int viewBoostValue;
    @NotBlank(message = "Giá tiền gói quảng cáo không được trống")
    private float price;
}
