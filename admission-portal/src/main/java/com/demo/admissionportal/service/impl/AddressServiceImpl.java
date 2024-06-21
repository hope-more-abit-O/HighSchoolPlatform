package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.WardResponseDTO;
import com.demo.admissionportal.dto.response.entity.AddressResponseDTO;
import com.demo.admissionportal.dto.response.entity.DistrictResponseDTO;
import com.demo.admissionportal.dto.response.entity.ProvinceResponseDTO;
import com.demo.admissionportal.entity.address.*;
import com.demo.admissionportal.repository.AddressRepository;
import com.demo.admissionportal.repository.DistrictRepository;
import com.demo.admissionportal.repository.ProvinceRepository;
import com.demo.admissionportal.repository.WardRepository;
import com.demo.admissionportal.repository.sub_repository.AddressDetailRepository;
import com.demo.admissionportal.service.AddressService;
import com.google.api.gax.rpc.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implement of University Service.
 *
 * @author hopeless
 * @version 1.0
 * @since 22/06/2024
 */
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;
    private final AddressDetailRepository addressDetailRepository;
    private final AddressRepository addressRepository;

    /**
     * Retrieves all provinces/cities.
     *
     * @return A {@link ResponseData} containing a list of {@link ProvinceResponseDTO} objects,
     *         or an error message if unsuccessful.
     */
    @Override
    public ResponseData<List<ProvinceResponseDTO>> getAllProvince(){
        List<ProvinceResponseDTO> provinceResponseDTOList = new ArrayList<>();
        provinceRepository.findAll().forEach(pr -> provinceResponseDTOList.add(modelMapper.map(pr, ProvinceResponseDTO.class)));
        return ResponseData.ok("Lấy các tỉnh / thành phố thành công.", provinceResponseDTOList);
    }

    /**
     * Retrieves all districts/towns within a specific province/city.
     *
     * @param id The ID of the province/city.
     * @return A {@link ResponseData} containing a list of {@link DistrictResponseDTO} objects,
     *         or an error message if unsuccessful.
     */
    @Override
    public ResponseData<List<DistrictResponseDTO>> getAllDistrictByProvinceId(Integer id){
        List<DistrictResponseDTO> districtResponseDTOList = new ArrayList<>();
        districtRepository.getDistrictsByProvinceId(id).forEach(dt -> districtResponseDTOList.add(modelMapper.map(dt, DistrictResponseDTO.class)));
        return ResponseData.ok("Lấy các phường / xã thành công.", districtResponseDTOList);
    }

    /**
     * Retrieves all wards/communes within a specific district/town.
     *
     * @param id The ID of the district/town.
     * @return A {@link ResponseData} containing a list of {@link WardResponseDTO} objects,
     *         or an error message if unsuccessful.
     */
    @Override
    public ResponseData<List<WardResponseDTO>> getAllWardByDistrictId(Integer id) {
        List<WardResponseDTO> wardResponseDTOList = new ArrayList<>();
        wardRepository.getWardsByDistrictId(id).forEach(dt -> wardResponseDTOList.add(modelMapper.map(dt, WardResponseDTO.class)));
        return ResponseData.ok("Lấy các phường / xã thành công.", wardResponseDTOList);
    }

    /**
     * Creates a new address record.
     *
     * @param specificAddress The specific address string.
     * @param provinceId     The ID of the province/city.
     * @param districtId     The ID of the district/town.
     * @param wardId         The ID of the ward/commune.
     * @return A {@link ResponseData} containing the ID of the newly created address,
     *         or an error message if unsuccessful.
     */
    @Override
    public ResponseData<Integer> createAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId){
        Optional<AddressDetail> optionalAddressDetail = addressDetailRepository.findByProvinceIdAndDistrictIdAndWardId(provinceId, districtId, wardId);
        if (optionalAddressDetail.isEmpty()){
            optionalAddressDetail = Optional.of(addressDetailRepository.save(new AddressDetail(provinceId, districtId, wardId)));
        }
        return ResponseData.ok("Tạo địa chỉ thành công", addressRepository.save(new Address(optionalAddressDetail.get().getId(), specificAddress)).getId());
    }

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
    @Override
    public Address createAddressReturnAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId){
        Optional<AddressDetail> optionalAddressDetail = addressDetailRepository.findByProvinceIdAndDistrictIdAndWardId(provinceId, districtId, wardId);
        if (optionalAddressDetail.isEmpty()){
            optionalAddressDetail = Optional.of(addressDetailRepository.save(new AddressDetail(provinceId, districtId, wardId)));
        }
        return addressRepository.save(new Address(optionalAddressDetail.get().getId(), specificAddress));
    }

    /**
     * Retrieves a detailed address response by its ID.
     *
     * @param id The ID of the address.
     * @return An {@link AddressResponseDTO} object containing detailed address information,
     *         or an error message if unsuccessful.
     * @throws Exception If the address is not found or an error occurs during retrieval.
     */
    @Override
    public AddressResponseDTO getAddressResponseById(Integer id) throws Exception{
        Address address = getAddressById(id);
        AddressDetail addressDetail = getAddressDetailById(address.getAddressDetailId());
        Province province = getProvinceById(addressDetail.getProvinceId());
        District district = getDistrictById(addressDetail.getDistrictId());
        Ward ward = getWardById(addressDetail.getWardId());

        return new AddressResponseDTO(address, province, district, ward);
    }

    /**
     * Retrieves a full address by its ID, including province, district, and ward information.
     *
     * @param id The ID of the address.
     * @return A {@link ResponseData} containing the {@link AddressResponseDTO} object with full address details,
     *         or an error message if unsuccessful.
     */
    @Override
    public ResponseData<AddressResponseDTO> getFullAddressById(Integer id){
        try {
            return ResponseData.ok("Lấy thông tin địa chỉ thành công.",getAddressResponseById(id));
        } catch (Exception e){
            return ResponseData.error(e.getMessage());
        }
    }

    /**
     * Retrieves an AddressDetail entity by its ID.
     *
     * @param id The ID of the AddressDetail.
     * @return The AddressDetail entity if found.
     * @throws Exception If the AddressDetail with the given ID is not found.
     */
    private AddressDetail getAddressDetailById(Integer id) throws Exception {
        Optional<AddressDetail> optionalAddressDetail = addressDetailRepository.findById(id);
        if (optionalAddressDetail.isEmpty()){
            throw new Exception("Không tìm thấy thông tin địa chỉ.");
        }
        return optionalAddressDetail.get();
    }

    /**
     * Retrieves an Address entity by its ID.
     *
     * @param id The ID of the Address.
     * @return The Address entity if found.
     * @throws Exception If the Address with the given ID is not found.
     */
    private Address getAddressById(Integer id) throws Exception {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()){
            throw new Exception("Không tìm thấy địa chỉ.");
        }
        return optionalAddress.get();
    }

    /**
     * Retrieves a Province entity by its ID.
     *
     * @param id The ID of the Province.
     * @return The Province entity if found.
     * @throws Exception If the Province with the given ID is not found.
     */
    private Province getProvinceById(Integer id) throws Exception {
        Optional<Province> optionalProvince = provinceRepository.findById(id);
        if (optionalProvince.isEmpty()){
            throw new Exception("Không tìm thấy tỉnh / thành phố.");
        }
        return optionalProvince.get();
    }

    /**
     * Retrieves a District entity by its ID.
     *
     * @param id The ID of the District.
     * @return The District entity if found.
     * @throws Exception If the District with the given ID is not found.
     */
    private District getDistrictById(Integer id) throws Exception {
        Optional<District> optionalDistrict = districtRepository.findById(id);
        if (optionalDistrict.isEmpty()){
            throw new Exception("Không tìm thấy quận / huyện.");
        }
        return optionalDistrict.get();
    }

    /**
     * Retrieves a Ward entity by its ID.
     *
     * @param id The ID of the Ward.
     * @return The Ward entity if found.
     * @throws Exception If the Ward with the given ID is not found.
     */
    private Ward getWardById(Integer id) throws Exception {
        Optional<Ward> optionalWard = wardRepository.findById(id);
        if (optionalWard.isEmpty()){
            throw new Exception("Không tìm thấy phường / xã.");
        }
        return optionalWard.get();
    }
}
