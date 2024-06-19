package com.demo.admissionportal.controller;

import com.demo.admissionportal.dto.request.RegisterStudentRequestV2DTO;
import com.demo.admissionportal.repository.DistrictRepository;
import com.demo.admissionportal.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final DistrictRepository districtRepository;

    @GetMapping("/province")
    public ResponseEntity<?> getProvinces(){
        return ResponseEntity.ok(addressService.getAllProvince());
    }

    @GetMapping("/district/{id}")
    public ResponseEntity<?> getDistricts(@PathVariable Integer id){
        return ResponseEntity.ok(addressService.getAllDistrictByProvinceId(id));
    }

    @GetMapping("/ward/{id}")
    public ResponseEntity<?> getWards(@PathVariable Integer id){
        return ResponseEntity.ok(addressService.getAllWardByDistrictId(id));
    }

    @PostMapping()
    public ResponseEntity<?> saveAddress(@RequestBody RegisterStudentRequestV2DTO request){
        return ResponseEntity.ok(addressService.createAddress(request.getSpecificAddress(), request.getProvinceId(), request.getDistrictId(), request.getWardId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Integer id){
        try{
            return ResponseEntity.ok(addressService.getAddressResponseById(id));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
