package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;

    @Override
    public ResponseData<Type> createTypePost(TypePostRequestDTO typePostRequestDTO) {
        try {
            if (typePostRequestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            Type type = new Type();
            type.setName(typePostRequestDTO.getName());
            type.setCreateBy(typePostRequestDTO.getCreate_by());
            type.setCreateTime(new Date());
            type.setStatus(PostPropertiesStatus.ACTIVE);
            Type result = typeRepository.save(type);
            if (result != null) {
                log.info("Tạo tag thành công: {}", result);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo tag thành công", result);
            }
            throw new Exception();
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi tạo tag: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tạo tag");
    }
}
