package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.WardResponseDTO;
import com.demo.admissionportal.dto.response.entity.AddressResponseDTO;
import com.demo.admissionportal.dto.response.entity.DistrictResponseDTO;
import com.demo.admissionportal.dto.response.entity.ProvinceResponseDTO;

import java.util.List;

import com.demo.admissionportal.entity.address.Address;

/**
 * AddressService - Service interface for managing and retrieving address-related information.
 *
 * @author hopeless
 * @version 1.0
 * @since 22/06/2024
 */
public interface AddressService {

    /**
     * Retrieves a list of all provinces/cities.
     *
     * @return A {@link ResponseData} containing a list of {@link ProvinceResponseDTO} objects,
     *         representing the provinces/cities.
     */
    ResponseData<List<ProvinceResponseDTO>> getAllProvince();

    /**
     * Retrieves a list of districts/towns within a specific province/city.
     *
     * @param id The ID of the province/city.
     * @return A {@link ResponseData} containing a list of {@link DistrictResponseDTO} objects,
     *         representing the districts/towns within the specified province/city.
     */
    ResponseData<List<DistrictResponseDTO>> getAllDistrictByProvinceId(Integer id);

    /**
     * Retrieves a list of wards/communes within a specific district/town.
     *
     * @param id The ID of the district/town.
     * @return A {@link ResponseData} containing a list of {@link WardResponseDTO} objects,
     *         representing the wards/communes within the specified district/town.
     */
    ResponseData<List<WardResponseDTO>> getAllWardByDistrictId(Integer id);

    /**
     * Creates a new address record with the provided details.
     *
     * @param specificAddress The detailed street address.
     * @param provinceId The ID of the province/city.
     * @param districtId The ID of the district/town.
     * @param wardId     The ID of the ward/commune.
     * @return A {@link ResponseData} containing the ID of the newly created address.
     */
    ResponseData<Integer> createAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId);
    /**
     * Creates a new address record.
     *
     * @param specificAddress The specific address string.
     * @param provinceId     The ID of the province/city.
     * @param districtId     The ID of the district/town.
     * @param wardId         The ID of the ward/commune.
     * @return A {@link Address} newly created,
     *         or an error message if unsuccessful.
     */
    Address createAddressReturnAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId);
    /**
     * Retrieves detailed address information by its ID.
     *
     * @param id The ID of the address.
     * @return An {@link AddressResponseDTO} containing the full address details.
     * @throws Exception If there is an error retrieving the address.
     */
    AddressResponseDTO getAddressResponseById(Integer id) throws Exception;

    /**
     * Retrieves a full address by its ID, including province, district, and ward information.
     *
     * @param id The ID of the address.
     * @return A {@link ResponseData} containing the {@link AddressResponseDTO} with the full address details.
     */
    ResponseData<AddressResponseDTO> getFullAddressById(Integer id);
}