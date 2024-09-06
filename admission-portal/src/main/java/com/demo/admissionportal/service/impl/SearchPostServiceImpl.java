package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.UniversityInfo;
import com.demo.admissionportal.entity.User;
import com.demo.admissionportal.repository.*;
import com.demo.admissionportal.service.SearchPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Search post service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SearchPostServiceImpl implements SearchPostService {
    private final SearchEngineRepository searchEngineRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final StaffInfoRepository staffInfoRepository;
    private final ConsultantInfoRepository consultantInfoRepository;
    private final UserRepository userRepository;
    private final UniversityInfoRepository universityInfoRepository;

    @Override
    public ResponseData<Page<PostSearchDTO>> searchPost(String content, Pageable pageable) {
        try {
            log.info("Start retrieve search post");
            List<PostSearchDTO> postRequestDTOS = searchEngineRepository.searchPost(content);
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO> comparator = Comparator.comparing(PostSearchDTO::getCreateTime);
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS = postRequestDTOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), postRequestDTOS.size());
            Page<PostSearchDTO> page = new PageImpl<>(postRequestDTOS.subList(start, end), pageable, postRequestDTOS.size());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post ", page);
        } catch (Exception ex) {
            log.info("Error when search post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }

    @Override
    public ResponseData<Page<PostSearchDTO>> searchFilterPost(String content, List<Integer> typeId, List<Integer> locationId, LocalDate startDate, LocalDate endDate, List<Integer> authorId, Pageable pageable) {
        try {
            log.info("Start retrieve search post by filter");
            List<PostSearchDTO> postRequestDTOS;
            int currentPage = pageable.getPageNumber();
            if (typeId != null && typeId.contains(999)) {
                postRequestDTOS = mapToPostResponseDTO(postRepository.findPost());
            } else if (typeId != null && typeId.contains(1000)) {
                postRequestDTOS = mapToPostResponseDTO(postRepository.findPost());
                Collections.shuffle(postRequestDTOS, new Random());
            } else {
                postRequestDTOS = searchEngineRepository.searchPostByFilter(content, typeId, locationId, startDate, endDate, authorId);
            }
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO> comparator = Comparator.comparing(
                        PostSearchDTO::getCreateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS = postRequestDTOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            List<PostSearchDTO> resultOfSearch = postRequestDTOS.stream()
                    .map(post -> {
                        PostSearchDTO postSearchDTO = new PostSearchDTO();
                        postSearchDTO.setId(post.getId());
                        postSearchDTO.setTitle(post.getTitle());
                        postSearchDTO.setCreateTime(post.getCreateTime());
                        postSearchDTO.setQuote(post.getQuote());
                        postSearchDTO.setThumnail(post.getThumnail());
                        postSearchDTO.setUrl(post.getUrl());
                        postSearchDTO.setFullName(post.getFullName());
                        postSearchDTO.setAvatar(post.getAvatar());
                        postSearchDTO.setStatus(post.getStatus());
                        if (currentPage == 0 && content != null && !content.trim().isEmpty()) {
                            List<UniversityInfo> infoUniversity = universityInfoRepository.findByName(content);
                            List<PostSearchDTO.InfoUniversitySearchDTO> universityDTOs = infoUniversity.stream()
                                    .map(this::mapToUniversityInfo)
                                    .collect(Collectors.toList());
                            postSearchDTO.setInfoUniversity(universityDTOs);
                        }
                        return postSearchDTO;
                    })
                    .distinct()
                    .collect(Collectors.toList());
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), resultOfSearch.size());
            Page<PostSearchDTO> page = new PageImpl<>(resultOfSearch.subList(start, end), pageable, resultOfSearch.size());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post by filter", page);
        } catch (Exception ex) {
            log.error("Error when search post by filter: {}", ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post by filter");
        }
    }

    private PostSearchDTO.InfoUniversitySearchDTO mapToUniversityInfo(UniversityInfo universityInfo) {
        PostSearchDTO.InfoUniversitySearchDTO response = modelMapper.map(universityInfo, PostSearchDTO.InfoUniversitySearchDTO.class);
        User user = userRepository.findUserById(universityInfo.getId());
        response.setAvatar(user.getAvatar());
        return response;
    }

    private List<PostSearchDTO> mapToPostResponseDTO(List<Post> posts) {
        return posts.stream()
                .map(post -> {
                    PostSearchDTO postSearchDTO = modelMapper.map(post, PostSearchDTO.class);
                    String avatar = getAvatarUserDTO(post.getCreateBy());
                    String name = getNameUserDTO(post.getCreateBy());
                    postSearchDTO.setFullName(name);
                    postSearchDTO.setAvatar(avatar);
                    return postSearchDTO;
                })
                .collect(Collectors.toList());
    }

    private PostSearchDTO enhancePostSearchDTO(PostSearchDTO postSearchDTO) {
        return Optional.ofNullable(postSearchDTO)
                .map(dto -> {
                    String avatar = getAvatarUserDTO(dto.getCreateBy());
                    String name = getNameUserDTO(dto.getCreateBy());
                    dto.setFullName(name);
                    dto.setAvatar(avatar);
                    return dto;
                })
                .orElse(null);
    }

    private String getAvatarUserDTO(Integer createBy) {
        return Optional.ofNullable(staffInfoRepository.findStaffInfoById(createBy))
                .map(staffInfo -> userRepository.findUserById(staffInfo.getId()).getAvatar())
                .orElseGet(() -> Optional.ofNullable(consultantInfoRepository.findConsultantInfoById(createBy))
                        .map(consultantInfo -> userRepository.findUserById(consultantInfo.getUniversityId()).getAvatar())
                        .orElse(null));
    }

    private String getNameUserDTO(Integer createBy) {
        return Optional.ofNullable(staffInfoRepository.findStaffInfoById(createBy))
                .map(staffInfo -> staffInfo.getFirstName() + " " + staffInfo.getMiddleName() + " " + staffInfo.getLastName())
                .orElseGet(() -> Optional.ofNullable(consultantInfoRepository.findConsultantInfoById(createBy))
                        .map(consultantInfo -> universityInfoRepository.findUniversityInfoById(consultantInfo.getUniversityId()).getName())
                        .orElse(null));
    }
}
