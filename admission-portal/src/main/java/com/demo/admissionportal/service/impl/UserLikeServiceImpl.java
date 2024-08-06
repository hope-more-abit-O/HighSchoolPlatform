package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.like.LikeResponseDTO;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.UserLikeRepository;
import com.demo.admissionportal.service.UserLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * The type User like service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserLikeServiceImpl implements UserLikeService {
    private final UserLikeRepository userLikeRepository;
    private final PostRepository postRepository;
    @Override
    public ResponseData<LikeResponseDTO> createLike(Integer postID) {
        Integer userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return null;
    }
}
