package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.ads_package.CreatePackageRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.ads_package.DeletePackageResponseDTO;
import com.demo.admissionportal.entity.AdsPackage;
import com.demo.admissionportal.service.PackageService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Package controller.
 */
@RestController
@RequestMapping("/api/v1/package")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class PackageController {

    private final PackageService packageService;

    /**
     * Create package response entity.
     *
     * @param requestDTO the request dto
     * @return the response entity
     */
    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<AdsPackage>> createPackage(@RequestBody @Valid CreatePackageRequestDTO requestDTO) {
        if (requestDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "Request trống"));
        }
        ResponseData<AdsPackage> resultOfCreate = packageService.createPackage(requestDTO);
        if (resultOfCreate.getStatus() == ResponseCode.C201.getCode()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfCreate);
        } else if (resultOfCreate.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(resultOfCreate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfCreate);
    }

    /**
     * Create package response entity.
     *
     * @param packageId the package id
     * @return the response entity
     */
    @PostMapping("/{packageId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseData<DeletePackageResponseDTO>> createPackage(@PathVariable(name = "packageId") Integer packageId) {
        if (packageId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(new ResponseData<>(ResponseCode.C205.getCode(), "Request trống"));
        }
        ResponseData<DeletePackageResponseDTO> resultOfCreate = packageService.deletePackage(packageId);
        if (resultOfCreate.getStatus() == ResponseCode.C203.getCode()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(resultOfCreate);
        } else if (resultOfCreate.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(resultOfCreate);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfCreate);
    }

    /**
     * List package response entity.
     *
     * @return the response entity
     */
    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('ADMIN','UNIVERSITY')")
    public ResponseEntity<ResponseData<List<AdsPackage>>> listPackage(){
        ResponseData<List<AdsPackage>> resultOfList = packageService.getPackages();
        if (resultOfList.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK.value()).body(resultOfList);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(resultOfList);
    }

}
