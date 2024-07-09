package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Province controller.
 */
@RestController
@RequestMapping("/api/v1/address/")
@RequiredArgsConstructor
public class AddressController {
    private final ProvinceService provinceService;

    /**
     * Gets province.
     *
     * @return the province
     */
    @GetMapping("/province")
    public ResponseEntity<ResponseData<List<Province>>> getProvince() {
        ResponseData<List<Province>> result = provinceService.findProvince();
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets district.
     *
     * @param provinceId the province id
     * @return the district
     */
    @PostMapping("/district/{provinceId}")
    public ResponseEntity<ResponseData<List<DistrictResponseDTO>>> getDistrict(@PathVariable(name = "provinceId") Integer provinceId) {
        ResponseData<List<DistrictResponseDTO>> result = provinceService.findDistrict(provinceId);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets ward.
     *
     * @param districtId the district id
     * @return the ward
     */
    @PostMapping("/ward/{districtId}")
    public ResponseEntity<ResponseData<WardResponseDTO>> getWard(@PathVariable(name = "districtId") Integer districtId) {
        ResponseData<WardResponseDTO> result = provinceService.findWard(districtId);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
