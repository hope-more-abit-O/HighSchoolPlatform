package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.exception.exceptions.BadRequestException;
import com.demo.admissionportal.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Province controller.
 */
@RestController
@RequestMapping("/api/v1/address/")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    /**
     * Gets province.
     *
     * @return the province
     */
    @GetMapping("/province")
    public ResponseEntity<ResponseData<List<Province>>> getProvince(@RequestParam(required = false) String region) {
        List<String> regions = null;
        if (region != null && !region.isEmpty()) {
            regions = Arrays.stream(region.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        try {
            ResponseData<List<Province>> result = addressService.findProvince(regions);
            if (result.getStatus() == ResponseCode.C200.getCode()) {
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new BadRequestException("Region không hợp lệ");
        }
    }

    /**
     * Gets district.
     *
     * @param provinceId the province id
     * @return the district
     */
    @PostMapping("/district/{provinceId}")
    public ResponseEntity<ResponseData<List<DistrictResponseDTO>>> getDistrict(@PathVariable(name = "provinceId") Integer provinceId) {
        ResponseData<List<DistrictResponseDTO>> result = addressService.findDistrict(provinceId);
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
    public ResponseEntity<ResponseData<List<WardResponseDTO>>> getWard(@PathVariable(name = "districtId") Integer districtId) {
        ResponseData<List<WardResponseDTO>> result = addressService.findWard(districtId);
        if (result.getStatus() == ResponseCode.C200.getCode()) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/university/provinces")
    public ResponseEntity<ResponseData<List<Province>>> getProvince(){
        return ResponseEntity.ok(ResponseData.ok("Lấy danh sách các tỉnh có trường đại học thành công", addressService.getUniversityProvinces()));
    }
}
