package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Tag;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.PostView;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import com.demo.admissionportal.entity.sub_entity.id.PostViewId;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.repository.sub_repository.PostTypeRepository;
import com.demo.admissionportal.repository.sub_repository.PostViewRepository;
import com.demo.admissionportal.service.PostService;
import com.demo.admissionportal.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Post service.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostTypeRepository postTypeRepository;
    private final TypeRepository typeRepository;
    private final PostViewRepository postViewRepository;
    private final TagService tagService;

    @Override
    public ResponseData<List<PostType>> createPost(PostRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            // Insert Post
            Post post = postSave(requestDTO);
            if (post == null) {
                throw new Exception("Save post thất bại!");
            }

            // Insert Type Post
            List<PostType> postTypes = postTypeSave(requestDTO.getListType(), post);
            if (postTypes.isEmpty()) {
                throw new Exception("Save post type thất bại!");
            }

            // Insert Post View
            PostView postView = postViewSave(requestDTO.getCreate_by(), post);
            if (postView == null) {
                throw new Exception("Save post view thất bại!");
            }

            // Insert Tags
            boolean tagsSaved = tagSave(requestDTO.getListTag(), post);
            if (!tagsSaved) {
                throw new Exception("Save tag thất bại!");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo post thành công", postTypes);
        } catch (Exception ex) {
            log.error("Error when create post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private boolean tagSave(List<String> tags, Post post) {
        try {
            for (String tagName : tags) {
                TagRequestDTO requestDTO = new TagRequestDTO();
                requestDTO.setName(tagName.trim());
                requestDTO.setCreate_by(post.getCreateBy());
                Tag checkTag = tagService.createTag(requestDTO);
                if (checkTag == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("Error when check post tag: {}", ex.getMessage());
            return false;
        }
    }

    private Post postSave(PostRequestDTO requestDTO) {
        try {
            Post post = new Post();
            post.setTitle(requestDTO.getTitle());
            post.setContent(requestDTO.getContent());
            post.setThumnail(requestDTO.getThumnail());
            post.setQuote(requestDTO.getQuote());
            post.setCreateBy(requestDTO.getCreate_by());
            post.setCreateTime(new Date());
            post.setStatus(PostStatus.ACTIVE);
            post.setLike(0);
            post.setView(0);
            return postRepository.save(post);
        } catch (Exception ex) {
            log.error("Error when save post: {}", ex.getMessage());
            return null;
        }
    }

    private List<PostType> postTypeSave(List<Integer> typeIdList, Post post) {
        List<PostType> postTypes = new ArrayList<>();
        try {
            for (Integer typeId : typeIdList) {
                PostTypeId postTypeId = new PostTypeId(post.getId(), typeId);
                PostType postType = new PostType();
                postType.setId(postTypeId);
                postType.setPost(post);
                Type findType = typeRepository.findTypeById(typeId);
                postType.setType(findType);
                postType.setCreateTime(new Date());
                postType.setStatus(PostPropertiesStatus.ACTIVE);
                postTypes.add(postTypeRepository.save(postType));
            }
        } catch (Exception ex) {
            log.error("Error when save post type: {}", ex.getMessage());
        }
        return postTypes;
    }

    private PostView postViewSave(Integer createBy, Post post) {
        try {
            PostViewId postViewId = new PostViewId();
            postViewId.setPostId(post.getId());
            postViewId.setCreateTime(new Date());
            PostView postView = new PostView();
            postView.setId(postViewId);
            postView.setViewCount(0);
            postView.setLikeCount(0);
            postView.setCreateBy(createBy);
            postView.setStatus(PostPropertiesStatus.ACTIVE);
            postView.setPost(post);
            return postViewRepository.save(postView);
        } catch (Exception ex) {
            log.error("Error when save post view: {}", ex.getMessage());
            return null;
        }
    }
}
