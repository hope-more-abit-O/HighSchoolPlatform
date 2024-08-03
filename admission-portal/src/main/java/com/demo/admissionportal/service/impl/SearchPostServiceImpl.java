package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.entity.search_engine.PostSearchDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.dto.response.post.PostResponseDTO;
import com.demo.admissionportal.entity.ConsultantInfo;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.StaffInfo;
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
    public ResponseData<Page<PostSearchDTO>> searchFilterPost(String content, Integer typeId, Integer locationId, LocalDate startDate, LocalDate endDate, Integer authorId, Pageable pageable) {
        try {
            log.info("Start retrieve search post by filter");
            List<PostSearchDTO> postRequestDTOS = new ArrayList<>();
            // Set default Newsiest has type id = 999
            if (typeId != null && typeId == 999) {
                List<Post> post = postRepository.findPost();
                Set<String> filter = new HashSet<>();
                postRequestDTOS = post.stream()
                        .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                        .sorted(Comparator.comparing(Post::getCreateTime).reversed())
                        .map(this::mapToPostResponseDTO)
                        .collect(Collectors.toList());
            }
            // Set default General has type id = 1000
            else if (typeId != null && typeId == 1000) {
                List<Post> post = postRepository.findPost();
                Set<String> filter = new HashSet<>();
                postRequestDTOS = post.stream()
                        .filter(p -> filter.add(p.getId() + p.getTitle() + p.getContent()))
                        .map(this::mapToPostResponseDTO)
                        .collect(Collectors.toList());
                Collections.shuffle(postRequestDTOS, new Random());
            } else {
                postRequestDTOS = searchEngineRepository.searchPostByFilter(content, typeId, locationId, startDate, endDate, authorId);
            }
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
            return new ResponseData<>(ResponseCode.C200.getCode(), "Đã tìm thấy danh sách post by filter", page);
        } catch (Exception ex) {
            log.info("Error when search post by filter: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), "Lỗi khi tìm danh sách post by filter");
        }
    }

    private PostSearchDTO mapToPostResponseDTO(Post post) {
        String avatar = getAvatarUserDTO(post.getCreateBy());
        PostSearchDTO postSearchDTO = modelMapper.map(post, PostSearchDTO.class);
        postSearchDTO.setAvatar(avatar);
        return postSearchDTO;
    }

    /**
     * Gets avatar user dto.
     *
     * @param createBy the create by
     * @return the avatar user dto
     */
    public String getAvatarUserDTO(Integer createBy) {
        StaffInfo staffInfo = staffInfoRepository.findStaffInfoById(createBy);
        ConsultantInfo consultantInfo = consultantInfoRepository.findConsultantInfoById(createBy);
        String avatar = null;
        if (staffInfo != null) {
            User user = userRepository.findUserById(staffInfo.getId());
            avatar = user.getAvatar();
        } else if (consultantInfo != null) {
            User user = userRepository.findUserById(consultantInfo.getUniversityId());
            avatar = user.getAvatar();
        }
        return avatar;
    }
}
