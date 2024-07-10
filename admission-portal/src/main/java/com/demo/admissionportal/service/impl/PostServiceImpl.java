package com.demo.admissionportal.service.impl;

import com.demo.admissionportal.constants.PostPropertiesStatus;
import com.demo.admissionportal.constants.PostStatus;
import com.demo.admissionportal.constants.ResponseCode;
import com.demo.admissionportal.dto.request.post.PostDeleteRequestDTO;
import com.demo.admissionportal.dto.request.post.PostRequestDTO;
import com.demo.admissionportal.dto.request.post.TagRequestDTO;
import com.demo.admissionportal.dto.response.ResponseData;
import com.demo.admissionportal.entity.Post;
import com.demo.admissionportal.entity.Tag;
import com.demo.admissionportal.entity.Type;
import com.demo.admissionportal.entity.sub_entity.PostTag;
import com.demo.admissionportal.entity.sub_entity.PostType;
import com.demo.admissionportal.entity.sub_entity.PostView;
import com.demo.admissionportal.entity.sub_entity.id.PostTagId;
import com.demo.admissionportal.entity.sub_entity.id.PostTypeId;
import com.demo.admissionportal.entity.sub_entity.id.PostViewId;
import com.demo.admissionportal.repository.PostRepository;
import com.demo.admissionportal.repository.TagRepository;
import com.demo.admissionportal.repository.TypeRepository;
import com.demo.admissionportal.repository.sub_repository.PostTagRepository;
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
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;

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
            // Store list Tags
            List<Integer> tagIds = new ArrayList<>();
            Tag tagsSaved = tagSave(requestDTO.getListTag(), post, tagIds);
            if (tagsSaved == null) {
                throw new Exception("Save tag thất bại!");
            }

            // Insert Tag Tag
            for (Integer tagId : tagIds) {
                PostTag postTagSaved = postTagSave(tagId, post);
                if (postTagSaved == null) {
                    throw new Exception("Save tag post thất bại!");
                }
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Tạo post thành công", postTypes);
        } catch (Exception ex) {
            log.error("Error when create post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private PostTag postTagSave(Integer tagId, Post post) {
        try {

            PostTagId postTagId = new PostTagId();
            postTagId.setPostId(post.getId());
            postTagId.setTagId(tagId);

            PostTag postTag = new PostTag();
            postTag.setId(postTagId);
            postTag.setPost(post);
            Tag findTag = tagRepository.findTagById(tagId);
            postTag.setTag(findTag);
            postTag.setCreateTime(new Date());
            postTag.setCreateBy(post.getCreateBy());
            postTag.setStatus(PostPropertiesStatus.ACTIVE);
            return postTagRepository.save(postTag);
        } catch (Exception ex) {
            log.error("Error when save post tag: {}", ex.getMessage());
            return null;
        }
    }

    private Tag tagSave(List<String> tags, Post post, List<Integer> tagIds) {
        Tag result = null;
        try {
            for (String tagName : tags) {
                TagRequestDTO requestDTO = new TagRequestDTO();
                requestDTO.setName(tagName.trim());
                requestDTO.setCreate_by(post.getCreateBy());
                result = tagService.createTag(requestDTO);
                if (result == null) {
                    return null;
                }
                tagIds.add(result.getId());
            }
            return result;
        } catch (Exception ex) {
            log.error("Error when save tag: {}", ex.getMessage());
            return null;
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
            post.setQuota(0);
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
                postType.setCreateBy(post.getCreateBy());
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

    @Override
    public ResponseData<String> deletePost(PostDeleteRequestDTO requestDTO) {
        try {
            if (requestDTO == null) {
                return new ResponseData<>(ResponseCode.C205.getCode(), "Sai request");
            }
            // Remove post
            Post resultChangeStatusPost = changeStatusPost(requestDTO);
            if (resultChangeStatusPost == null) {
                throw new Exception("Xoá post thất bại");
            }
            // Remove post tag
            List<Integer> tagIds = new ArrayList<>();
            List<PostTag> resultChangeStatusPostTag = changeStatusPostTag(requestDTO, tagIds);
            if (resultChangeStatusPostTag == null) {
                throw new Exception("Xoá post tag thất bại");
            }
            // Remove tag
            boolean resultChangeStatusTag = changeStatusTag(requestDTO, tagIds);
            if (!resultChangeStatusTag) {
                throw new Exception("Xoá tag thất bại");
            }

            // Remove Post Type
            boolean resultChangeStatusPostType = changeStatusPostType(requestDTO);
            if (!resultChangeStatusPostType) {
                throw new Exception("Xoá post type thất bại");
            }

            // Remove Post View
            boolean resultChangeStatusPostView = changeStatusPostView(requestDTO);
            if (!resultChangeStatusPostView) {
                throw new Exception("Xoá post type thất bại");
            }
            return new ResponseData<>(ResponseCode.C200.getCode(), "Xoá thành công");
        } catch (Exception ex) {
            log.error("Error when delete post: {}", ex.getMessage());
            return new ResponseData<>(ResponseCode.C207.getCode(), ex.getMessage());
        }
    }

    private boolean changeStatusPostView(PostDeleteRequestDTO requestDTO) {
        try {
            PostView postView = postViewRepository.findByPostId(requestDTO.getPostId());
            if (postView == null) {
                return false;
            }
            postView.setStatus(PostPropertiesStatus.INACTIVE);
            postView.setUpdateTime(new Date());
            postView.setUpdateBy(requestDTO.getUserId());
            postViewRepository.save(postView);
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post view : {}", ex.getMessage());
            return false;
        }
    }

    private boolean changeStatusPostType(PostDeleteRequestDTO requestDTO) {
        try {
            List<PostType> posts = postTypeRepository.findPostTypeByPostId(requestDTO.getPostId());
            for (PostType post : posts) {
                post.setStatus(PostPropertiesStatus.INACTIVE);
                post.setUpdateTime(new Date());
                post.setUpdateBy(requestDTO.getUserId());
                postTypeRepository.save(post);
            }
            return true;
        } catch (Exception ex) {
            log.error("Error when remove post type : {}", ex.getMessage());
            return false;
        }
    }

    private boolean changeStatusTag(PostDeleteRequestDTO requestDTO, List<Integer> tagIds) {
        try {
            for (Integer tagId : tagIds) {
                Tag tag = tagRepository.findTagById(tagId);
                tag.setStatus(PostPropertiesStatus.INACTIVE);
                tag.setUpdateTime(new Date());
                tag.setUpdateBy(requestDTO.getUserId());
            }
            return true;
        } catch (Exception ex) {
            log.error("Error when remove tag : {}", ex.getMessage());
            return false;
        }
    }

    private Post changeStatusPost(PostDeleteRequestDTO requestDTO) {
        try {
            Post post = postRepository.findFirstById(requestDTO.getPostId());
            if (post == null) {
                return null;
            }
            post.setStatus(PostStatus.INACTIVE);
            post.setUpdateTime(new Date());
            post.setUpdateBy(requestDTO.getUserId());
            return postRepository.save(post);

        } catch (Exception ex) {
            log.error("Error when remove post : {}", ex.getMessage());
            return null;
        }
    }

    private List<PostTag> changeStatusPostTag(PostDeleteRequestDTO requestDTO, List<Integer> tagIds) {
        try {
            List<PostTag> postTagList = postTagRepository.findPostTagByPostId(requestDTO.getPostId());
            if (postTagList == null) {
                return null;
            }
            for (PostTag postTag : postTagList) {
                postTag.setStatus(PostPropertiesStatus.INACTIVE);
                postTag.setUpdateTime(new Date());
                postTag.setUpdateBy(requestDTO.getUserId());
                tagIds.add(postTag.getId().getTagId());
                postTagRepository.save(postTag);
            }
            return postTagList;
        } catch (Exception ex) {
            log.error("Error when remove post tag : {}", ex.getMessage());
            return null;
        }
    }
}
