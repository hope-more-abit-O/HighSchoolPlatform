package com.demo.admissionportal.service;

import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.entity.Tag;

/**
 * The interface Post service.
 */
public interface TagService {
    /**
     * Create tag respones data.
     *
     * @param requestDTO the request dto
     * @return the respones data
     */
    Tag createTag(TagRequestDTO requestDTO);

    /**
     * Check tag existed tag.
     *
     * @param tagName the tag name
     * @return the tag
     */
    Tag checkTagExisted(String tagName);
}
