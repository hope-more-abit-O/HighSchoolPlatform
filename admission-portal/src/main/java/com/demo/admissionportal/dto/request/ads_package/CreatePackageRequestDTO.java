package com.demo.admissionportal.dto.request.ads_package;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Tên gói quảng cáo không được trống")
    private String name;
    @NotNull(message = "Mô tả quảng cáo không được trống")
    private String description;
    @NotNull(message = "Ảnh quảng cáo không được trống")
    private String image;
    @NotNull(message = "Số lượng tiếp cận không được trống")
    private int viewBoostValue;
    @NotNull(message = "Giá tiền gói quảng cáo không được trống")
    private float price;
}
