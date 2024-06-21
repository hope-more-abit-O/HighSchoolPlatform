package com.demo.admissionportal.controller;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.RegisterStudentRequestV2DTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.WardResponseDTO;
import com.demo.admissionportal.dto.response.entity.AddressResponseDTO;
import com.demo.admissionportal.dto.response.entity.DistrictResponseDTO;
import com.demo.admissionportal.dto.response.entity.ProvinceResponseDTO;
import com.demo.admissionportal.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling address-related API requests.
 *
 * @author hopeless
 * @since 2024-06-20
 */
@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    /**
     * Retrieves a list of all provinces.
     *
     * @return A ResponseEntity containing a list of provinces.
     */
    @GetMapping("/province")
    public ResponseEntity<ResponseData<List<ProvinceResponseDTO>>> getProvinces() { // Assuming response is a list of strings
        return ResponseEntity.ok(addressService.getAllProvince());
    }

    /**
     * Retrieves a list of districts for a given province ID.
     *
     * @param id The ID of the province (Integer).
     * @return A ResponseEntity containing a list of districts.
     */
    @GetMapping("/district/{id}")
    public ResponseEntity<ResponseData<List<DistrictResponseDTO>>> getDistricts(@PathVariable Integer id) { // Assuming response is a list of strings
        return ResponseEntity.ok(addressService.getAllDistrictByProvinceId(id));
    }

    /**
     * Retrieves a list of wards for a given district ID.
     *
     * @param id The ID of the district (Integer).
     * @return A ResponseEntity containing a list of wards.
     */
    @GetMapping("/ward/{id}")
    public ResponseEntity<ResponseData<List<WardResponseDTO>>> getWards(@PathVariable Integer id) { // Assuming response is a list of strings
        return ResponseEntity.ok(addressService.getAllWardByDistrictId(id));
    }

    /**
     * Creates a new address based on the provided request data.
     *
     * @param request The request containing address information {@link RegisterStudentRequestV2DTO class}.
     *               The request body must be valid according to the defined validation rules.
     * @return A ResponseEntity containing the created address information {@link AddressResponseDTO class}.
     */
    @PostMapping
    public ResponseEntity<ResponseData<Integer>> saveAddress(@Valid @RequestBody RegisterStudentRequestV2DTO request) {
        return ResponseEntity.ok(addressService.createAddress(request.getSpecificAddress(), request.getProvinceId(), request.getDistrictId(), request.getWardId()));
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param id The ID of the address to retrieve (Integer).
     * @return A ResponseEntity containing the address information {@link AddressResponseDTO class}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<AddressResponseDTO>> getAddressById(@PathVariable Integer id) {
        ResponseData<AddressResponseDTO> result = addressService.getFullAddressById(id);
        if (result.getStatus() == ResponseCode.C200.getCode())
            return ResponseEntity.ok(result);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
