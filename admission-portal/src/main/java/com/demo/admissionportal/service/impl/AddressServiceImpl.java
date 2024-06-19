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

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ModelMapper modelMapper;
    private final AddressDetailRepository addressDetailRepository;
    private final AddressRepository addressRepository;

    public ResponseData<List<ProvinceResponseDTO>> getAllProvince(){
        List<ProvinceResponseDTO> provinceResponseDTOList = new ArrayList<>();
        provinceRepository.findAll().forEach(pr -> provinceResponseDTOList.add(modelMapper.map(pr, ProvinceResponseDTO.class)));
        return ResponseData.ok("Lấy các tỉnh / thành phố thành công.", provinceResponseDTOList);
    }

    public ResponseData<List<DistrictResponseDTO>> getAllDistrictByProvinceId(Integer id){
        List<DistrictResponseDTO> districtResponseDTOList = new ArrayList<>();
        districtRepository.getDistrictsByProvinceId(id).forEach(dt -> districtResponseDTOList.add(modelMapper.map(dt, DistrictResponseDTO.class)));
        return ResponseData.ok("Lấy các phường / xã thành công.", districtResponseDTOList);
    }

    @Override
    public ResponseData<List<WardResponseDTO>> getAllWardByDistrictId(Integer id) {
        List<WardResponseDTO> wardResponseDTOList = new ArrayList<>();
        wardRepository.getWardsByDistrictId(id).forEach(dt -> wardResponseDTOList.add(modelMapper.map(dt, WardResponseDTO.class)));
        return ResponseData.ok("Lấy các phường / xã thành công.", wardResponseDTOList);
    }

    public ResponseData<Integer> createAddress(String specificAddress, Integer provinceId, Integer districtId, Integer wardId){
        Optional<AddressDetail> optionalAddressDetail = addressDetailRepository.findByProvinceIdAndDistrictIdAndWardId(provinceId, districtId, wardId);
        if (optionalAddressDetail.isEmpty()){
            optionalAddressDetail = Optional.of(addressDetailRepository.save(new AddressDetail(provinceId, districtId, wardId)));
        }
        return ResponseData.ok("Tạo địa chỉ thành công", addressRepository.save(new Address(optionalAddressDetail.get().getId(), specificAddress)).getId());
    }

    public AddressResponseDTO getAddressResponseById(Integer id) throws Exception{
        Address address = getAddressById(id);
        AddressDetail addressDetail = getAddressDetailById(address.getAddressDetailId());
        Province province = getProvinceById(addressDetail.getProvinceId());
        District district = getDistrictById(addressDetail.getDistrictId());
        Ward ward = getWardById(addressDetail.getWardId());

        return new AddressResponseDTO(address, province, district, ward);
    }

    private AddressDetail getAddressDetailById(Integer id) throws Exception {
        Optional<AddressDetail> optionalAddressDetail = addressDetailRepository.findById(id);
        if (optionalAddressDetail.isEmpty()){
            throw new Exception("Không tìm thấy thông tin địa chỉ.");
        }
        return optionalAddressDetail.get();
    }

    private Address getAddressById(Integer id) throws Exception {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()){
            throw new Exception("Không tìm thấy địa chỉ.");
        }
        return optionalAddress.get();
    }

    private Province getProvinceById(Integer id) throws Exception {
        Optional<Province> optionalProvince = provinceRepository.findById(id);
        if (optionalProvince.isEmpty()){
            throw new Exception("Không tìm thấy tỉnh / thành phố.");
        }
        return optionalProvince.get();
    }

    private District getDistrictById(Integer id) throws Exception {
        Optional<District> optionalDistrict = districtRepository.findById(id);
        if (optionalDistrict.isEmpty()){
            throw new Exception("Không tìm thấy quận / huyện.");
        }
        return optionalDistrict.get();
    }

    private Ward getWardById(Integer id) throws Exception {
        Optional<Ward> optionalWard = wardRepository.findById(id);
        if (optionalWard.isEmpty()){
            throw new Exception("Không tìm thấy phường / xã.");
        }
        return optionalWard.get();
    }


}
