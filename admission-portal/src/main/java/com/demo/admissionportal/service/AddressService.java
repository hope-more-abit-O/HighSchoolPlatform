package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.DistrictResponseDTO;
import com.demo.admissionportal.dto.request.post.WardResponseDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Province;
import com.demo.admissionportal.exception.exceptions.ResourceNotFoundException;

import java.util.List;

/**
 * The interface Province service.
 */
public interface AddressService {

    //TODO: JAVADOC
    Province findProvinceById(Integer id) throws ResourceNotFoundException;

    /**
     * Find province response data.
     *
     * @return the response data
     */
    ResponseData<List<Province>> findProvince(List<String> regions);

    /**
     * Find all response data.
     *
     * @param provinceId the province id
     * @return the response data
     */
    ResponseData<List<DistrictResponseDTO>> findDistrict(Integer provinceId);

    /**
     * Find ward response data.
     *
     * @param districtId the district id
     * @return the response data
     */
    ResponseData<List<WardResponseDTO>> findWard(Integer districtId);
}
