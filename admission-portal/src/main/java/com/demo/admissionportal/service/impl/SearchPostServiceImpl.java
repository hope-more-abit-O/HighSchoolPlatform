package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostStatus;
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
    public ResponseData<Page<PostSearchDTO.PostSearch>> searchPost(String content, Pageable pageable) {
        try {
            log.info("Start retrieve search post");
            List<PostSearchDTO.PostSearch> postRequestDTOS = searchEngineRepository.searchPost(content);
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO.PostSearch> comparator = Comparator.comparing(
                        PostSearchDTO.PostSearch::getCreateTime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                );
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS = postRequestDTOS.stream()
                        .sorted(comparator)
                        .collect(Collectors.toList());
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), postRequestDTOS.size());
            Page<PostSearchDTO.PostSearch> page = new PageImpl<>(postRequestDTOS.subList(start, end), pageable, postRequestDTOS.size());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post ", page);
        } catch (Exception ex) {
            log.info("Error when search post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post");
        }
    }

    /**
     * Search filter post response data.
     *
     * @param content    the content
     * @param typeId     the type id
     * @param locationId the location id
     * @param startDate  the start date
     * @param endDate    the end date
     * @param authorId   the author id
     * @param pageable   the pageable
     * @return the response data
     */
    public ResponseData<Page<PostSearchDTO.PostSearch>> searchFilterPost(String content, List<Integer> typeId, List<Integer> locationId, LocalDate startDate, LocalDate endDate, List<Integer> authorId, Pageable pageable) {
        try {
            log.info("Start retrieve search post by filter");
            List<PostSearchDTO.PostSearch> postRequestDTOS;

            // Use specific filters if applicable
            if (typeId != null && !typeId.isEmpty()) {
                if (typeId.contains(999)) {
                    postRequestDTOS = mapToPostResponseDTO(postRepository.findPost());
                } else if (typeId.contains(1000)) {
                    postRequestDTOS = mapToPostResponseDTO(postRepository.findPost());
                    Collections.shuffle(postRequestDTOS, new Random());
                } else {
                    postRequestDTOS = searchEngineRepository.searchPostByFilter(content, typeId, locationId, startDate, endDate, authorId);
                }
            } else {
                postRequestDTOS = searchEngineRepository.searchPostByFilter(content, typeId, locationId, startDate, endDate, authorId);
            }

            postRequestDTOS = postRequestDTOS.stream()
                    .map(this::enhancePostSearchDTO)
                    .distinct()
                    .collect(Collectors.toList());
            Sort.Order order = pageable.getSort().getOrderFor("create_time");
            if (order != null) {
                Comparator<PostSearchDTO.PostSearch> comparator = Comparator.comparing(PostSearchDTO.PostSearch::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder()));
                if (order.getDirection() == Sort.Direction.DESC) {
                    comparator = comparator.reversed();
                }
                postRequestDTOS.sort(comparator);
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), postRequestDTOS.size());
            Page<PostSearchDTO.PostSearch> page = new PageImpl<>(postRequestDTOS.subList(start, end), pageable, postRequestDTOS.size());

            return new ResponseData<>(ResponseCode.C200.getCode(), "Found posts by filter", page);
        } catch (Exception ex) {
            log.error("Error when searching posts by filter: {}", ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Error when searching posts by filter");
        }
    }

    private PostSearchDTO.InfoUniversitySearchDTO mapToUniversityInfo(UniversityInfo universityInfo) {
        PostSearchDTO.InfoUniversitySearchDTO response = modelMapper.map(universityInfo, PostSearchDTO.InfoUniversitySearchDTO.class);
        User user = userRepository.findUserById(universityInfo.getId());
        response.setAvatar(user.getAvatar());
        return response;
    }

    private List<PostSearchDTO.PostSearch> mapToPostResponseDTO(List<Post> posts) {
        return posts.stream()
                .map(post -> {
                    PostSearchDTO.PostSearch postSearchDTO = modelMapper.map(post, PostSearchDTO.PostSearch.class);
                    String avatar = getAvatarUserDTO(post.getCreateBy());
                    String name = getNameUserDTO(post.getCreateBy());
                    postSearchDTO.setFullName(name);
                    postSearchDTO.setAvatar(avatar);
                    return postSearchDTO;
                })
                .collect(Collectors.toList());
    }

    private PostSearchDTO.PostSearch enhancePostSearchDTO(PostSearchDTO.PostSearch postSearchDTO) {
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

    public ResponseData<Map<String, Object>> searchFilterPostV2(String content, List<Integer> typeId, List<Integer> locationId, LocalDate startDate, LocalDate endDate, List<Integer> authorId, Pageable pageable) {
        try {
            log.info("Start retrieve search post by filter (V2)");
            Page<PostSearchDTO.PostSearch> postPage = searchFilterPost(content, typeId, locationId, startDate, endDate, authorId, pageable).getData();
            List<PostSearchDTO.InfoUniversitySearchDTO> universityDTOs = Collections.emptyList();
            int currentPage = pageable.getPageNumber();
            if (currentPage == 0 && content != null && !content.trim().isEmpty()) {
                List<UniversityInfo> infoUniversity = universityInfoRepository.findByName(content);
                universityDTOs = infoUniversity.stream()
                        .map(this::mapToUniversityInfo)
                        .collect(Collectors.toList());
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("university", universityDTOs);
            result.put("content", postPage.getContent());
            result.put("pageable", postPage.getPageable());
            result.put("totalPages", postPage.getTotalPages());
            result.put("last", postPage.isLast());
            result.put("totalElements", postPage.getTotalElements());
            result.put("size", postPage.getSize());
            result.put("number", postPage.getNumber());
            result.put("sort", postPage.getSort());
            result.put("first", postPage.isFirst());
            result.put("numberOfElements", postPage.getNumberOfElements());
            result.put("empty", postPage.isEmpty());
            return new ResponseData<>(ResponseCode.C200.getCode(), "Search results for posts and universities", result);
        } catch (Exception ex) {
            log.error("Error when searching posts and universities: {}", ex.getMessage(), ex);
            return new ResponseData<>(ResponseCode.C207.getCode(), "Error while searching posts and universities");
        }
    }
}
