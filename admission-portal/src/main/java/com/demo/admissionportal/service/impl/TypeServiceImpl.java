package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.type.TypeListResponseDTO;
import com.demo.admissionportal.entity.StaffInfo;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.StaffInfoRepository;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Type service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TypeServiceImpl implements TypeService {
    private final TypeRepository typeRepository;
    private final StaffInfoRepository staffInfoRepository;

    @Override
    public ResponseData<Type> createTypePost(TypePostRequestDTO typePostRequestDTO) {
        Integer createBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        try {
            if (typePostRequestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            // Validate type existed
            boolean isExisted = validateDuplicatePost(typePostRequestDTO.getName());
            if (isExisted == true) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Loại bài đăng đã tồn tại. Vui lòng nhập tên khác!");
            }
            // Insert type
            Type type = new Type();
            type.setName(typePostRequestDTO.getName());
            type.setCreateBy(createBy);
            type.setCreateTime(new Date());
            type.setStatus(PostPropertiesStatus.ACTIVE);
            Type result = typeRepository.save(type);
            if (result != null) {
                log.info("Tạo tag thành công: {}", result);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo type thành công", result);
            }
            throw new Exception();
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi tạo type: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tạo type");
    }

    @Override
    public ResponseData<Page<TypeListResponseDTO>> getListTypePost(Pageable pageable) {
        try {
            Page<Type> list = typeRepository.findAllType(pageable);
            List<TypeListResponseDTO> typeResponseDTOList = list.stream()
                    .map(this::mapToTypeList)
                    .collect(Collectors.toList());
            Page<TypeListResponseDTO> typeListResponseDTOPage = new PageImpl<>(typeResponseDTOList, pageable, list.getTotalElements());
            if (list != null) {
                log.info("Lấy danh sách thành công: {}", list);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách thành công", typeListResponseDTOPage);
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi lấy danh sách type: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi lấy danh sách type");
    }

    @Override
    public ResponseData<Type> getTypeById(Integer id) {
        try {
            if (id == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Không có typeId");
            }
            Type type = typeRepository.findTypeById(id);
            if (type != null) {
                log.info("Lấy type thành công: {}", type);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy type thành công", type);
            } else {
                log.info("Không tìm thấy type: {}", type);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy type", type);
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi lấy type {}: {}", id, ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi lấy type");
    }

    @Override
    public ResponseData<Type> changeStatus(Integer id) {
        Integer updateBy = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        try {
            if (id == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Không có typeId");
            }
            Type type = typeRepository.findTypeById(id);
            if (type != null) {
                log.info("Lấy type {} thành công", id);
                if (type.getStatus() == PostPropertiesStatus.ACTIVE) {
                    type.setStatus(PostPropertiesStatus.INACTIVE);
                } else {
                    type.setStatus(PostPropertiesStatus.ACTIVE);
                }
                type.setUpdateBy(updateBy);
                type.setUpdateTime(new Date());
                typeRepository.save(type);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Thay đổi trạng thái loại bài đăng thành công", type);
            } else {
                log.info("Không tìm thấy type Id: {}", id);
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy type", type);
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi thay đổi trạng thái loại bài đăng {}: {}", id, ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi thay đổi trạng thái loại bài đăng");
    }

    @Override
    public ResponseData<Type> updateType(Integer postId, TypePostUpdateRequestDTO requestDTO) {
        try {
            if (postId == null || requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Không có typeId");
            }
            Type type = typeRepository.findTypeById(postId);
            boolean isExisted = validateDuplicatePost(requestDTO.getName());
            if (isExisted == true) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "Loại bài đăng đã tồn tại. Vui lòng nhập tên khác!");
            }
            if (type != null) {
                type.setName(requestDTO.getName());
                type.setCreateBy(requestDTO.getCreate_by());
                type.setUpdateBy(requestDTO.getUpdate_by());
                type.setUpdateTime(new Date());
                typeRepository.save(type);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Cập nhật loại bài đăng thành công", type);
            } else {
                log.info("Không tìm thấy type Id");
                return new ResponseData<>(ResponseCode.C203.getCode(), "Không tìm thấy type", type);
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi cập nhật loại bài đăng: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi cập nhật loại bài đăng");
    }

    private boolean validateDuplicatePost(String name) {
        boolean isExisted = false;
        List<Type> list = typeRepository.findTypeByName(name);
        if (list != null && !list.isEmpty()) {
            isExisted = true;
        }
        return isExisted;
    }

    private TypeListResponseDTO mapToTypeList(Type type) {
        StaffInfo createBy = staffInfoRepository.findStaffInfoById(type.getCreateBy());
        StaffInfo updateBy = staffInfoRepository.findStaffInfoById(type.getUpdateBy());
        return TypeListResponseDTO.builder()
                .id(type.getId())
                .name(type.getName())
                .createBy(createBy.getFirstName().trim() + " " + createBy.getMiddleName().trim() + " " + createBy.getLastName().trim())
                .createTime(type.getCreateTime())
                .updateBy(updateBy != null ? updateBy.getFirstName().trim() + " " + updateBy.getMiddleName().trim() + " " + updateBy.getLastName().trim() : null)
                .status(type.getStatus())
                .build();
    }
}
