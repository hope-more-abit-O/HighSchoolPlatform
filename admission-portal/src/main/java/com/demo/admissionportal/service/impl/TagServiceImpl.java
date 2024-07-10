package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Tag;
import com.demo.admissionportal.repository.TagRepository;
import com.demo.admissionportal.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * The type Post service.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag createTag(TagRequestDTO requestDTO) {
        Tag result = null;
        try {
            if (requestDTO == null) {
                return null;
            }
            Tag tag = new Tag();
            tag.setName(requestDTO.getName());
            tag.setCreateBy(requestDTO.getCreate_by());
            tag.setCreateTime(new Date());
            tag.setStatus(PostPropertiesStatus.ACTIVE);
            result = tagRepository.save(tag);
            if (result != null) {
                log.info("Tạo tag thành công: {}", result);
                return result;
            }
        } catch (Exception ex) {
            log.error("Xảy ra lỗi khi tạo tag: {}", ex.getMessage());
        }
        return result;
    }
}
