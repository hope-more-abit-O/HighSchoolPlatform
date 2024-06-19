package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.WardResponseDTO;
import com.demo.admissionportal.dto.response.entity.AddressResponseDTO;
import com.demo.admissionportal.dto.response.entity.DistrictResponseDTO;
import com.demo.admissionportal.dto.response.entity.ProvinceResponseDTO;

import java.util.List;

public interface AddressService {
    public ResponseData<List<ProvinceResponseDTO>> getAllProvince();
    public ResponseData<List<DistrictResponseDTO>> getAllDistrictByProvinceId(Integer id);
    public ResponseData<List<WardResponseDTO>> getAllWardByDistrictId(Integer id);
    public ResponseData<Integer> createAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId);
    public AddressResponseDTO getAddressResponseById(Integer id) throws Exception;
}
