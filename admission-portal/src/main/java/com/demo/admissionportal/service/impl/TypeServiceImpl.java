package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TypePostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostRequestDTO;
import com.demo.admissionportal.dto.request.post.TypePostUpdateRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
            // Validate type existed
            boolean isExisted = validateDuplicatePost(typePostRequestDTO.getName());
            if (isExisted == true) {
                return new ResponseData<>(ResponseCode.C207.getCode(), "Loại bài đăng đã tồn tại. Vui lòng nhập tên khác!");
            }
            // Insert type
            Type type = new Type();
            type.setName(typePostRequestDTO.getName());
            type.setCreateBy(typePostRequestDTO.getCreate_by());
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
    public ResponseData<List<Type>> getListTypePost() {
        try {
            List<Type> list = typeRepository.findAll();
            if (list != null) {
                log.info("Lấy danh sách thành công: {}", list);
                return new ResponseData<>(ResponseCode.C200.getCode(), "Lấy danh sách thành công", list);
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi lấy danh sách type: {}", ex.getMessage());
        }
        return new ResponseData<>(ResponseCode.C207.getCode(), "Xảy ra lỗi khi tạo type");
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
    public ResponseData<Type> changeStatus(Integer id, TypePostDeleteRequestDTO requestDTO) {
        Integer update_by = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
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
                type.setUpdateBy(update_by);
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
}
